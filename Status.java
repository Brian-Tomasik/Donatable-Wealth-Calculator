/*
  Status.java
  11 Jan. 2007

  A Status object keeps track of the income and cost streams that the
  utilitarian will pay when he has a particular status.
*/

public class Status
{
    public static final String FILLER = "no value specified";
    public static final int NUM_STATUSES = 5;
    public static final int UNDERGRAD = 0;
    public static final int GRAD_SCHOOL = 1;
    public static final int WORKING = 2;
    public static final int RETIRED = 3;
    public static final int DEAD = 4;

    public int status;
    public String[] statusLabels;
    public String statusString;
    public int[] income;
    public int[] costOfLiving;
    public String option;
    public int currArrayBucket;
    public int arraySizes;
    public int numValuesShowInToString;

    public void defineStatuses()
    {
	statusLabels = new String[NUM_STATUSES];
	statusLabels[UNDERGRAD] = "undergrad";
	statusLabels[GRAD_SCHOOL] = "gradSchool";
	statusLabels[WORKING] = "working";
	statusLabels[RETIRED] = "retired";
	statusLabels[DEAD] = "dead";
    }

    public Status(int stat, int arrSize, String opt, int numVals)
    {
	defineStatuses();
	status = stat;
	arraySizes = arrSize;
	statusString = statusLabels[status];
	currArrayBucket = 0;
	numValuesShowInToString = numVals;
	option = opt;
	
	income = Applications.makeIncome(arraySizes, status, option);
	costOfLiving = Applications.makeCostOfLiving
	    (arraySizes, status, option);
    }

    public void incrementYears()
    {
	if(currArrayBucket + 1 < arraySizes) { currArrayBucket ++; }
	else { System.out.println
		   ("You can't increment the year past " + arraySizes +
		    ". Error!"); }
    }
    public int getIncome()
    { return income[currArrayBucket]; }
    
    public int getCostOfLiving()
    { return costOfLiving[currArrayBucket]; }

    public int yearsInStatus()
    { return currArrayBucket + 1; }

    public String toString()
    {
	String returnMe = "Status = " + statusString;
	if(option != FILLER) { returnMe = returnMe.concat(", " + option); }
	returnMe = returnMe.concat("\ncurrent income = " 
				   + wealthCalc.addCommas(getIncome()) +
				   "\ncurrent cost of living = " +
				   wealthCalc.addCommas(getCostOfLiving()));

	return returnMe;
    }

    public String assumpToString()
    {
	String returnMe = "Status = " + statusString + "\n";
	if(option != FILLER) { returnMe = returnMe.concat(option + "\n"); }
	for(int i = 0; i < numValuesShowInToString; i ++)
	    {
		returnMe = returnMe.concat
		    ("  In year " + (i+1) + " of this status, income = " +
		     wealthCalc.addCommas(income[i]) 
		     + " and cost of living = " + 
		     wealthCalc.addCommas(costOfLiving[i]) + ".\n");
	    }

	return returnMe;
    }
}
