/*
  arrayMethods.java
  11 Jan. 2007

  This class provides static methods for (1) sorting an array of Comparable and
  (2) filling in intermediate numbers in an array, given how those
  intermediate values should grow.
*/

public class arrayMethods
{
    public static final int NONSENSE = -1;

    public static Comparable[] sortAscending(Comparable[] array, int size)
    {
	Comparable temp;
	int indexOfMin;
	for(int i = 0; i < size; i ++)
	    {
		for(int j = i; j < size; j ++)
		    {
			indexOfMin = findIndexOfMin(array, j, size-1);
			temp = array[indexOfMin];
			array[indexOfMin] = array[j];
			array[j] = temp;
		    }
	    }

	return array;
    }

    public static int findIndexOfMin
	(Comparable[] array, int startBucket, int endBucket)
    {
	int index = startBucket;
	for(int i = startBucket; i <= endBucket; i ++)
	    {
		if(array[i].compareTo(array[index]) < 0)
		    { index = i; }
	    }
	
	return index;
    }

    public static int[] fillInMissingValues
	(int[] array, int size, int startingPosition, String growth)
    {
	int curr = startingPosition;
	
	// Assume that array[startingPosition] always has a value
	int currVal = array[curr];
	int nextActualValue, changePerBucket;

	while( 
	      (nextActualValue = findNextLocationWithNonNonsenseValue
	       (array, size, curr+1)) != NONSENSE
	      )
	    {
		if(growth.equalsIgnoreCase("linear"))
		{
		    int bucketDistance = nextActualValue - curr;
		    int valueDifference = array[nextActualValue] - array[curr];
		    changePerBucket = valueDifference / bucketDistance;
		}
		else if(growth.equalsIgnoreCase("no change"))
		    {
			changePerBucket = 0;
		    }
		else
		    {
			changePerBucket = NONSENSE;
			System.out.println("Your growth choice isn't one of " +
					   "the available options.");
		    }
		   
		currVal = array[curr];
		while(curr < nextActualValue - 1)
		    {
			curr ++;
			currVal += changePerBucket;
			array[curr] = currVal;
		    }
		
		curr = nextActualValue;
		currVal = array[curr];
	    }
	
	while(curr < size) { array[curr++] = currVal; }
	
	return array;
    }

    public static int findNextLocationWithNonNonsenseValue
	(int[] array, int size, int probe)
    {
	while(probe < size && array[probe] == NONSENSE)
	    { probe ++; }
	if(probe >= size) { return NONSENSE; }
	else { return probe; }
    }
}
