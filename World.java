/*
  World.java
  9 Jan. 2007

  This class defines World objects, which store data values like the rate
  of inflation, the annual rate of return from the stock market, and the 
  probability that catastrophe will happen by next year. It has methods for 
  updating to a new year and for creating a String of its assumptions.
*/

import java.util.*;

public class World
{
    private final static int CURR_YEAR = Utilitarian.YEAR_RIGHT_NOW;
    private final static double PROB_CATASTROPHE_NEXT_YEAR = .01;
    private final static double AVERAGE_INFL = 1.03;
    private final static double PLUS_OR_MINUS_INFL = 0;
    // I have set this and the corresponding value for market to 0 temporarily.
    // The reason is that a regular "plus or minus" amount fails to work in
    // this case, becase the geometric mean of values from that distribution
    // will be bigger than the arithmetic mean. Hence, I have to adjust the
    // upper and lower ranges separately. I leave that for a later day.
    // Note that, as things stand, the variation in final results is 
    // understated.
    private final static double AVERAGE_MARKET = 1.12;
    private final static double PLUS_OR_MINUS_MARKET = 0;
    private final static int NUM_TIMES_FOR_CLT = 1;
    // I'm using the Central Limit Theorem to help make my numbers for
    // inflation and market more normal than just being draws from a 
    // uniform distribution. NUM_TIMES_FOR_CLT is how many draws I'll make
    // to get CLT effects.
    private final static double LOAN_RATE = 1.06; 
    // Interest payments on a loan to borrow money

    public double inflation; // annual rate of inflation (e.g., 1.03)
    public double market; // annual rate of return on stock market (e.g., 1.12)
    public double cumInflation;
    public double cumMarket;
    public double probCatastropheNextYear; 
    // A catastrophe would be something like human extinction, permanent
    // collapse of the stock market, or the utilitarian's decision to stop
    // caring about the world and to instead spend all of his money 
    // frivilously.
    public int year;
    public int startYear;
    public boolean isDestroyed; // Has a catastrophe happened?
    public Random generator;
    public double loanRate;

    public World(Random gen)
    {
	generator = gen;
	cumInflation = 1;
	cumMarket = 1;
	market = getRandDoubleCLT(AVERAGE_MARKET - PLUS_OR_MINUS_MARKET,
				  AVERAGE_MARKET + PLUS_OR_MINUS_MARKET,
				  NUM_TIMES_FOR_CLT);
	inflation = getRandDoubleCLT(AVERAGE_INFL - PLUS_OR_MINUS_INFL,
				     AVERAGE_INFL + PLUS_OR_MINUS_INFL,
				     NUM_TIMES_FOR_CLT);
	probCatastropheNextYear = PROB_CATASTROPHE_NEXT_YEAR;
	year = startYear = CURR_YEAR;
	isDestroyed = false;
	loanRate = LOAN_RATE;
    }

    public void newYear()
    {
	year ++;
	market = getRandDoubleCLT(AVERAGE_MARKET - PLUS_OR_MINUS_MARKET,
			       AVERAGE_MARKET + PLUS_OR_MINUS_MARKET,
			       NUM_TIMES_FOR_CLT);
	inflation = getRandDoubleCLT(AVERAGE_INFL - PLUS_OR_MINUS_INFL,
				     AVERAGE_INFL + PLUS_OR_MINUS_INFL,
				     NUM_TIMES_FOR_CLT);

	cumInflation *= inflation;
	cumMarket *= market;
	
	// See if the world has been destroyed
	double randBtw0And1 = getRandDoubleCLT(0, 1, 1);
	if(randBtw0And1 < probCatastropheNextYear)
	    { isDestroyed = true; }
    }

    public double getRandDoubleCLT(double min, double max, int clt)
    {
	double runningSum = 0;
	
	for(int i = 0; i < clt; i ++)
	    {
		double range = max - min;
		double randBtw0AndRange = range * (generator.nextDouble());
		runningSum += (randBtw0AndRange + min);
	    }

	return (runningSum / clt);
    }

    public String assumptionsToString()
    {
	return "A catastrophe is an event that prevents the utilitarian\n" +
	    "from making a donation. Examples:\n" +
	    "  - World is destroyed\n" +
	    "  - Stock market crashes permanently\n" + 
	    "  - Utilitarian becomes apathetic and stops caring.\n" +
	    "In any given year, the probability that a catastrophe\n" +
	    "happens by next year is " + PROB_CATASTROPHE_NEXT_YEAR + ".\n" +
	    "Annual rate of inflation = " + 
	    (int)((AVERAGE_INFL - 1)*100) + "%.\n" +
	    "Annual rate of return on stock-market investments = " + 
	    (int)((AVERAGE_MARKET-1)*100) + "%.\n" +
	    "Interest rate on loans = " + (int)((loanRate-1)*100) + "%.\n";
    }
}
