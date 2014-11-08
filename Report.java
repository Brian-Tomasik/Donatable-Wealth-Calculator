/*
  Report.java
  9 Jan. 2007

  Report objects store information about how a utilitarian fared during a
  particular simulation: How much donatable wealth was accumulated and how
  long the utilitarian lived. Because Report objects implement Comparable,
  an array of them can be sorted.
*/

public class Report implements Comparable
{
    public long wealth;
    public int lifespan;
    public boolean worldDestroyed;

    public Report(long w, int l, boolean dest)
    {
	wealth = w;
	lifespan = l;
	worldDestroyed = dest;
    }

    public int compareTo(Object other)
    { 
	int wealthDiff = (int) (wealth - ((Report) other).wealth);
	int lifespanDiff = lifespan - ((Report) other).lifespan;
	if(wealthDiff != 0) { return wealthDiff; }
	else
	    {
		if(lifespanDiff != 0) { return lifespanDiff; }
		else
		    {
			if(worldDestroyed) { return 1; }
			else { return -1; }
		    }
	    }
    }

    public String toString()
    {
	return "$" + wealthCalc.addCommas(wealth) + ", lifespan = " + lifespan 
	    + ", catastrophe? " + worldDestroyed;
    }
}
