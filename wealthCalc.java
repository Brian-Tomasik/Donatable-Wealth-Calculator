/* 
   wealthCalc.java
   9 Jan. 2007

   The main method of this program runs simulations using Utilitarian and World
   objects, and then stores and outputs the results.
*/

import java.util.*;
import java.io.*;

public class wealthCalc
{
    private static final int CURR_YEAR = Utilitarian.YEAR_RIGHT_NOW;
    private static final int TIMES_RUN_SIMULATION = 30;
    private static final String OUTPUT_FILE_NAME = "results.txt";
    private static final String TRACK_PROGRESS_FILE_NAME = "track.txt";
    public static final int ERROR_BREAK_LOOP_YEAR = 300;
    private static final boolean TRACK_PROGRESS_THROUGH_THE_YEARS = true;
    private static final int NUM_HISTOGRAM_BARS = 50;
    
    // Simulation stats
    static long minWealth = Integer.MAX_VALUE;
    static long maxWealth = -Integer.MAX_VALUE;
    static int numCatastrophes = 0;
    static long runningSumWealth = 0;
    static String assumptions = "";
    
    public static void main(String[] args)
    {
	Random generator = new Random();
	assumptions = assumptions.concat
	    ("Assumptions:" +
	     "\n-----------\n" +
	     (new World(generator)).assumptionsToString() + "\n" +
	     (new Utilitarian(generator)).
	     assumptionsToString() + "\n");
	// The Utilitarian and World created here are just dummies, not to
	// be used further.

	Report[] values = new Report[TIMES_RUN_SIMULATION];

	for(int runs = 0; runs < TIMES_RUN_SIMULATION; runs ++)
	    { runSimulation(generator, values, runs+1); }
	   
	arrayMethods.sortAscending(values, TIMES_RUN_SIMULATION);
	writeFinalStats(assumptions, values, TIMES_RUN_SIMULATION);
	System.out.println
	    ("\nAverage wealth in " + CURR_YEAR + " dollars = " + 
	     addCommas((int)(runningSumWealth / TIMES_RUN_SIMULATION)) 
	     + ".\n" +
	     "For detailed results, open \"" + OUTPUT_FILE_NAME + ".\"");
        if(TRACK_PROGRESS_THROUGH_THE_YEARS)
	    {
		System.out.println
		    ("See also the file \"" + TRACK_PROGRESS_FILE_NAME + 
		     "\" for a record of progress through time.");
	    }
	System.out.println();
    }	

    public static void runSimulation(Random generator, Report[] values, 
				     int simulationNumber)
    {
	Utilitarian bob = new Utilitarian(generator);
	World world = new World(generator);
	String trackProgress = "";

	int yearsFromStart = 0;
	while(!bob.isAllDoneWithDonations && !world.isDestroyed &&
	      ++yearsFromStart <= ERROR_BREAK_LOOP_YEAR)
	    {
		trackProgress = trackProgress.concat
		    (bob.toString(world.cumInflation));
		world.newYear();
		bob.newYear(world.market, world.cumInflation, world.loanRate);
	    }
	if(yearsFromStart > ERROR_BREAK_LOOP_YEAR)
	    {
		System.out.println("Error! Years from start is greater " +
				   "than " + ERROR_BREAK_LOOP_YEAR + ".");
	    }

	trackProgress = trackProgress.concat(bob.toString(world.cumInflation));
	if(TRACK_PROGRESS_THROUGH_THE_YEARS) 
	    { writeStringToFile(trackProgress); }

	values[simulationNumber-1] =
	    new Report(bob.donationsToDateInCurrDollars, bob.lifespan,
		       world.isDestroyed);
	if(world.isDestroyed) { numCatastrophes ++; }
	if(bob.donationsToDateInCurrDollars > maxWealth)
	    { maxWealth = bob.donationsToDateInCurrDollars; }
	if(bob.donationsToDateInCurrDollars < minWealth)
	    { minWealth = bob.donationsToDateInCurrDollars; }
	runningSumWealth += bob.donationsToDateInCurrDollars;
    }

    public static void writeFinalStats(String assumptions, Report[] values, 
				       int sizeOfValues)
    {
	PrintWriter fileOut = null;
	
	try
	    {
		fileOut = new PrintWriter(new FileWriter(OUTPUT_FILE_NAME));

		fileOut.println
		    (assumptions +
		     "\nStatistics:\n" +
		     "-----------\n" +
		     "Num Trials = " + addCommas(TIMES_RUN_SIMULATION) 
		     + "\n" + 
		     "Num Catastrophes = " + addCommas(numCatastrophes) 
		     + "\n" +
		     "Min = " + addCommas(minWealth) + "\n" +
		     "Max = " + addCommas(maxWealth) + "\n" +
		     "Mean = " +
		     addCommas((int)(runningSumWealth/TIMES_RUN_SIMULATION)) 
		     + "\n\n");
		fileOut.println("Individual Values:\n" +
				"------------------");
		fileOut.println(arrayToString(values, sizeOfValues));
		fileOut.println(theHistogram(values, sizeOfValues,
					     minWealth, maxWealth));
	    } catch(IOException e) { e.printStackTrace(); }

	    finally
		{
		    if(fileOut != null)
			{ fileOut.close(); }
		}
    }

    public static String arrayToString(Report[] array, int size)
    {
	String returnMe = "";
	for(int i = 0; i < size; i ++)
	    { returnMe = returnMe.concat(array[i].toString() + "\n"); }

	return returnMe;
    }

    public static String theHistogram(Report[] values, int sizeOfValues,
					 long min, long max)
    {
	String theHistogram = "";

	// The histArray is twice as big as the number of histogram
	// bars that will be used to allow for the possibility of negative
	// values. (E.g., if there are 20 histogram bars, the 21st array
	// bucket will be used for the first positive value, leaving the
	// previous 20 for negative values.
	int[] histArray = new int[2*NUM_HISTOGRAM_BARS];
	long histInterval = (max - min) / NUM_HISTOGRAM_BARS;
	if(histInterval <= 0) { return theHistogram; }
	else
	    {
		theHistogram = theHistogram.concat
		    ("Histogram:\n" +
		     "----------\n");
	    }

	for(int i = 0; i < 2*NUM_HISTOGRAM_BARS; i ++)
	    { histArray[i] = 0; }
	
	int histBucket;
	int minHistBucket = 2*NUM_HISTOGRAM_BARS - 1;
	for(int j = 0; j < sizeOfValues; j ++)
	    {
		histBucket = ((int)
		     (values[j].wealth / histInterval)) + NUM_HISTOGRAM_BARS-1;
		if(histBucket >= 2*NUM_HISTOGRAM_BARS)
		    { histBucket = NUM_HISTOGRAM_BARS - 1; }
		if(histBucket < 0)
		    { histBucket = 0; }
		histArray[histBucket] ++;

		if(histBucket < minHistBucket) { minHistBucket = histBucket; }
	    }
	
	int numStars;
	for(int k = 0; k <= NUM_HISTOGRAM_BARS; k ++)
	    {
		theHistogram = theHistogram.concat
		    ("" + addCommas(k * histInterval + min) + " - " + 
		     addCommas
		     (k * histInterval + min + histInterval - 1) +
		     ": ");
		
		for(numStars = 0; numStars < histArray[k+minHistBucket]; 
		    numStars ++)
		    {
			theHistogram = theHistogram.concat("*");
		    }
		theHistogram = theHistogram.concat("\n");
	    }
	theHistogram = theHistogram.concat("\n");
		
	return theHistogram;
    }

    public static void writeStringToFile(String writeMe)
    {
	PrintWriter fileOut = null;
	try
	    {
		fileOut = new PrintWriter
		    (new FileWriter(TRACK_PROGRESS_FILE_NAME));
		fileOut.println(writeMe);
	    } catch(IOException e) { e.printStackTrace(); }
	    finally
		{ if(fileOut != null) { fileOut.close(); } }
    }

    public static String addCommas(int number)
    {
	String numAsString = Integer.toString(number);
	int digitsSoFar = 0;
	String returnMe = "";

	for(int i = numAsString.length() - 1; i >= 0; i --)
	    {
		returnMe = returnMe.concat
		    (Character.toString(numAsString.charAt(i)));
		digitsSoFar ++;
		if(digitsSoFar % 3 == 0 && !(digitsSoFar == 0) && !(i == 0))
		    {
			returnMe = returnMe.concat(",");
		    }
	    }

	return reverseString(returnMe);
    }

    public static String addCommas(long number)
    {
	String numAsString = Long.toString(number);
	int digitsSoFar = 0;
	String returnMe = "";

	for(int i = numAsString.length() - 1; i >= 0; i --)
	    {
		returnMe = returnMe.concat
		    (Character.toString(numAsString.charAt(i)));
		digitsSoFar ++;
		if(digitsSoFar % 3 == 0 && !(digitsSoFar == 0) && !(i == 0))
		    {
			returnMe = returnMe.concat(",");
		    }
	    }

	return reverseString(returnMe);
    }
    
    public static String reverseString(String string)
    {
	String returnMe = "";
	for(int i = string.length() - 1; i >= 0; i --)
	    {
		returnMe = returnMe.concat
		    (Character.toString(string.charAt(i)));
	    }

	return returnMe;
    }
}
