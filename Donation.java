/*
  Donation.java
  9 Jan. 2007
  
  This class defines Donation objects, which store information about a 
  donation that the utilitarian intends to make. They tell what proportion
  of the utilitarian's wealth she intends to give away at what particular
  age. Donation objects are Comparable so that they can be sorted in an
  array.
*/

public class Donation implements Comparable
{
    private static final int CURR_YEAR = Utilitarian.YEAR_RIGHT_NOW;

    public double proportionCurrWealth;
    public int ageWhenMade;

    public Donation(double prop, int age)
    {
	proportionCurrWealth = prop;
	ageWhenMade = age;
    }

    public int compareTo(Object other)
    { return this.ageWhenMade - ((Donation) other).ageWhenMade; }

    public String toString()
    {
	return "In " + yearWhenThisAge(ageWhenMade) + ", at age " 
	    + ageWhenMade + ", donate " +
	    ((int) (proportionCurrWealth*100)) + "% of the wealth held " +
	    "at that time.";
    }

    public int yearWhenThisAge(int age)
    {
	int yearsSinceRightNow = age - Utilitarian.AGE;
	return Utilitarian.YEAR_RIGHT_NOW + yearsSinceRightNow;
    }
}
