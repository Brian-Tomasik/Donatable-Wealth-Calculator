/*
 Utilitarian.java
  9 Jan. 2007

  This class defines Utilitarian objects, which store values related to
  age, lifespan, age of retirement, costs of living, and donations that will
  be made. It provides methods for advancing the utilitarian a year in time,
  finding income taxes, and returning a Stringn of numbers/assumptions related
  to the utilitarian.
*/

import java.util.*;

public class Utilitarian
{
    // Career choices
    // private static final String CAREER = "simple example";
    //private static final String CAREER = "average property/casualty actuary";
    // private static final String CAREER = "slow property/casualty actuary";
    // private static final String CAREER = "fast property/casualty actuary";
    //  private static final String CAREER = "average life/health actuary";
     private static final String CAREER = "average pension actuary";
    // private static final String CAREER = "wall street quant";
    // private static final String CAREER = "bum";
    
    // Grad school choices
    private static final String TYPE_OF_GRAD_SCHOOL = "math or science";
    // private static final String TYPE_OF_GRAD_SCHOOL = "business or law";
    
    // Other parameters
    private static final int SIZE_ARRAYS = 200;
    private static final int AVERAGE_LIFESPAN = 78;
    private static final int PLUS_OR_MINUS_LIFESPAN = 15;
    public static final int YEAR_RIGHT_NOW = 2007;
    private static final int STARTING_WEALTH = 0;
    public static final int AGE = 19;
    private static final int TIMES_DO_CLT = 2;
    private static final int NUM_DONATIONS = 1;
    private static final int MIN_YEARS_RETIREMENT = 4;
    private static final int MAX_YEARS_RETIREMENT = 20;
    private static final int RETIREMENT_DIV = 3;
    private static final int RETIREMENT_INTERCEPT = 10;

    // Utilitarian data fields
    public String name;
    public int age;
    public int ageAtStart;
    public int lifespan;
    public int yearsOfRetirement;
    public int startingWealth;
    public long stockMarketBalance;
    public long donationsToDateInCurrDollars;
    public String[] statusOption;
    public Status[] statusArray;
    public int[] numValuesToShowInStatusToString;
    public int[] statusAtAge;
    private Random generator;
    public Donation[] donations;
    public int numDonationsSoFar;
    public boolean isAllDoneWithDonations;

    // Constructor
    public Utilitarian(Random gen)
    {
	generator = gen;
	name = "Bob";

	age = ageAtStart = AGE;
	lifespan = randIntCLT
	    ((AVERAGE_LIFESPAN - PLUS_OR_MINUS_LIFESPAN), 
	     (AVERAGE_LIFESPAN + PLUS_OR_MINUS_LIFESPAN), TIMES_DO_CLT);

	// yearsOfRetirement formula
	yearsOfRetirement = 
	    ((lifespan - (AVERAGE_LIFESPAN - PLUS_OR_MINUS_LIFESPAN)) /
	    RETIREMENT_DIV) + RETIREMENT_INTERCEPT;
	if(yearsOfRetirement < MIN_YEARS_RETIREMENT) 
	    { yearsOfRetirement = MIN_YEARS_RETIREMENT; }
	if(yearsOfRetirement > MAX_YEARS_RETIREMENT) 
	    { yearsOfRetirement = MAX_YEARS_RETIREMENT; }

	statusOption = new String[Status.NUM_STATUSES];
	for(int i = 0; i < Status.NUM_STATUSES; i ++)
	    { statusOption[i] = Status.FILLER; }
	statusOption[Status.WORKING] = CAREER;
	statusOption[Status.GRAD_SCHOOL] = TYPE_OF_GRAD_SCHOOL;

	numValuesToShowInStatusToString = new int[Status.NUM_STATUSES];
	numValuesToShowInStatusToString[Status.UNDERGRAD] = 4;
	numValuesToShowInStatusToString[Status.GRAD_SCHOOL] = 6;
	numValuesToShowInStatusToString[Status.WORKING] = 35;
	numValuesToShowInStatusToString[Status.RETIRED] = 15;
	numValuesToShowInStatusToString[Status.DEAD] = 5;

	statusArray = new Status[Status.NUM_STATUSES];
	for(int i = 0; i < Status.NUM_STATUSES; i ++)
	    {
		statusArray[i] = 
		    new Status(i, SIZE_ARRAYS, statusOption[i],
			       numValuesToShowInStatusToString[i]);
	    }
	
	statusAtAge = new int[SIZE_ARRAYS];
	for(int i = ageAtStart; i < SIZE_ARRAYS; i ++)
	    { statusAtAge[i] = arrayMethods.NONSENSE; }
	statusAtAge[ageAtStart] = Status.UNDERGRAD;
	statusAtAge[23] = Status.WORKING;
	// add more values to statusAtAge, if desired
	statusAtAge[lifespan - yearsOfRetirement] = Status.RETIRED;
	statusAtAge[lifespan] = Status.DEAD;
	arrayMethods.fillInMissingValues
	    (statusAtAge, SIZE_ARRAYS, ageAtStart, "no change");

	startingWealth = STARTING_WEALTH;
	stockMarketBalance = startingWealth;
	donationsToDateInCurrDollars = 0;
	donations = makeDonations();
	numDonationsSoFar = 0;
        isAllDoneWithDonations = false;
    }

    public int randIntCLT(int min, int max, int clt)
    {
	if(max > min)
	    {
		int runningSum = 0;
		for(int i = 0; i < clt; i ++)
		    { runningSum += (generator.nextInt(max - min) + min); }
		
		return runningSum / clt;
	    }
	else
	    { return max; }
    }

    private Donation[] makeDonations()
    {
	Donation[] donations = new Donation[NUM_DONATIONS];
	donations[0] = new Donation(1, lifespan - 2);
	// add more donations here, if desired
	
	arrayMethods.sortAscending(donations, NUM_DONATIONS);
	
	return donations;
    }
    
    public void newYear(double market, double cumInflation,
 			double loanRate)
    {
 	age ++;
 	if(stockMarketBalance >= 0) { stockMarketBalance *= market; }
 	else { stockMarketBalance *= loanRate; }
	
 	int costOfLiving = (int)
 	     (statusArray[statusAtAge[age]].getCostOfLiving() * cumInflation);
 	stockMarketBalance -= costOfLiving;
	    
 	int income = (int) 
 	    (statusArray[statusAtAge[age]].getIncome() * cumInflation);
 	stockMarketBalance += 
 	    (income - (int) taxesOn((double)income, cumInflation));

 	statusArray[statusAtAge[age]].incrementYears();
	
 	if(donations[numDonationsSoFar].ageWhenMade == age)
	    {
		long donationAmt = (long) 
		    (donations[numDonationsSoFar].proportionCurrWealth * 
		     stockMarketBalance);
		if(donationAmt <= stockMarketBalance)
		    {
			stockMarketBalance -= donationAmt;
			donationsToDateInCurrDollars 
			    += (long) (donationAmt / cumInflation);
		    }
		else
		    {
			if(stockMarketBalance > 0)
			    {
				// give what there is available
				donationsToDateInCurrDollars += 
				    (int) (stockMarketBalance / cumInflation);
				stockMarketBalance = 0;
			    }
			else
			    {
				// do nothing; this donation will be skipped 
			    }
		    }
		
		numDonationsSoFar ++;
		if(numDonationsSoFar >= NUM_DONATIONS)
	            { isAllDoneWithDonations = true; }
	    }
    }

    public double taxesOn(double income, double cumInflation)
    {
	// Tax rates taken from 
	// www.irs.gov/formspubs/article/0,,id=133517,00.html
	// under the assumption of being single.
	if(income > 326450*cumInflation) 
	    { return (income - 326450*cumInflation)*.35 
		  + taxesOn(326450*cumInflation, cumInflation); }
	else if(income > 150150*cumInflation)
	    { return (income - 150150*cumInflation)*.33 
		  + taxesOn(150150*cumInflation, cumInflation); }
	else if(income > 71950*cumInflation)
	    { return (income - 71950*cumInflation)*.28 
		  + taxesOn(71950*cumInflation, cumInflation); }
	else if(income > 29700*cumInflation)
	    { return (income - 29700*cumInflation)*.25 
		  + taxesOn(29700*cumInflation, cumInflation); }
	else if(income > 7300*cumInflation)
	    { return (income - 7300*cumInflation)*.15 
		  + taxesOn(7300*cumInflation, cumInflation); }
	else if(income > 0*cumInflation)
	    { return (income - 0*cumInflation)*.10 
		  + taxesOn(0*cumInflation, cumInflation); }
	else
	    { return 0; }
    }

    public String toString(double cumInflation)
    {
	String returnMe = "\n------------------------------\n";
	returnMe = returnMe.concat
	    ("Utilitarian " + name + "\n" + 
	     "Age = " + age + "\n" +
	     "Age at start = " + ageAtStart + "\n" +
	     "Lifespan = " + lifespan + "\n" +
	     "Years of retirement = " + yearsOfRetirement + "\n" +
	     "Starting wealth = " + startingWealth + "\n" +
	     statusArray[statusAtAge[age]].toString() + "\n" +
	     "Stock market balance (" + YEAR_RIGHT_NOW + " dollars) = " + 
	     wealthCalc.addCommas((long) (stockMarketBalance / cumInflation)) 
	     + "\n" +
	     "Wealth donated to date (in " + YEAR_RIGHT_NOW + " dollars) = " + 
	     wealthCalc.addCommas(donationsToDateInCurrDollars) + "\n" +
	     "Number of donations so far = " + numDonationsSoFar + "\n");
	returnMe = returnMe.concat("------------------------------\n");

	return returnMe;
    }

    // This method outputs the assumptions for the Utilitarian being used
    // in the calculations.
    public String assumptionsToString()
    {
	String string = "Year right now = " + YEAR_RIGHT_NOW + ".\n" +
	    "Age right now = " + ageAtStart + ".\n" +
	    "Lifespans are determined as the average of " + 
	    TIMES_DO_CLT + " random draws\nfrom a uniform distribution of " +
	    "integers between " + (AVERAGE_LIFESPAN - PLUS_OR_MINUS_LIFESPAN) +
	    " and " + (AVERAGE_LIFESPAN + PLUS_OR_MINUS_LIFESPAN) + ".\n" +
	    "Years of retirement is determined by this formula:\n" +
	    "(lifespan - " + (AVERAGE_LIFESPAN - PLUS_OR_MINUS_LIFESPAN) +
	    ")/" + RETIREMENT_DIV + " + " + RETIREMENT_INTERCEPT + ",\n" +
	    "except that years of retirement cannot be lower than " +
	    MIN_YEARS_RETIREMENT + " or higher than " + MAX_YEARS_RETIREMENT +
	    ".\n" +
	    "Amount of wealth utilitarian starts with = " 
	    + STARTING_WEALTH + ".\n" + 
	    "\nHere is a list of values for what income and cost of living\n" +
	    "would be (in " + YEAR_RIGHT_NOW + " dollars) if the utilitarian "
	    + "had the listed status:\n";
	    
	for(int i = 0; i < Status.NUM_STATUSES; i ++)
	    { string = string.concat(statusArray[i].assumpToString()); }

	string = string.concat
	    ("\nFor someone with a lifespan of " + lifespan + " who retires " +
	     "at age " + (lifespan - yearsOfRetirement) + ",\nhere's a " +
	     "list of their statuses at various ages:\n");
	
	for(int i = ageAtStart; i < lifespan + 1; i ++)
	    {
		string = string.concat
		    ("  At age " + i + ", status = " + 
		     statusArray[statusAtAge[i]].statusString + ".\n");
	    }

	string = string.concat
	    ("\nIncome tax rates are based on \"Schedule X - Single\" at " +
	     "\nwww.irs.gov/formsbups/article/0,,id=133517,00.html\n" +
	     "I assume the income levels where the rates increase keep\n" +
	     "exact pace with inflation.\n" +
	     "Number of donations the utilitarian will make = " +
	     NUM_DONATIONS + ".\n" +
	     "List of donations:");
	
	for(int i = 0; i < NUM_DONATIONS; i ++)
	    { string = string.concat("\n  " + donations[i].toString()); }

	return string;
    }
}
