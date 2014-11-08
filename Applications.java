/*
  Applications.java
  9 Jan. 2007

  This file initializes Utilitarian's income and costOfLiving arrays.
*/

public class Applications
{
    public static int[] makeIncome(int size, int status, String option)
    {
	int[] array = new int[size];
	for(int i = 0; i < size; i++)
	    { array[i] = arrayMethods.NONSENSE; }
	
	if(status == Status.UNDERGRAD)
	    {
		array[0] = 0;
		array[4] = 0;

		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "linear");
	    }
	else if(status == Status.GRAD_SCHOOL)
	    {
		array[0] = 10000;
		array[4] = 15000;

		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "linear");
	    }
	else if(status == Status.WORKING)
	    {
		if(option.equalsIgnoreCase("simple example"))
		    {
			array[0] = 80000;
			array[40] = 3*array[0];
		    }
		else if(option.equalsIgnoreCase("bum"))
		    {
			array[0] = 0;
		    }
		else if(option.equalsIgnoreCase
			("average property/casualty actuary"))
		    {
			// The following values are roughly based on data from
			// www.dwsimpson.com/salary.html
			array[0] = 53000; // passed 2 exams
			array[1] = 60000; // passed 3 exams
			array[3] = 70000; // passed 4 exams
			array[5] = 86000; // passed 5 exams
			array[7] = 105000; // passed 6 exams
			array[9] = 120000; // passed ACAS
			array[12] = 158000; // passed 8 exams
			array[14] = 200000; // passed ACAS
			array[20] = 270000;
			array[24] = 330000;
		    }
		else if(option.equalsIgnoreCase
			("slow property/casualty actuary"))
		    {
			// Again, these are roughly based on 
			// www.dwsimpson.com/salary.html
			array[0] = 44000; // 1 exam
			array[3] = 47000; // 1 exam
			array[5] = 60000; // 2 exams
			array[7] = 69000; // 3 exams
			array[10] = 83000; // 4 exams
			array[13] = 102000; // 6 exams
			array[15] = 120000; // ACAS
			array[19] = 150000; 
			array[22] = 170000;
		    }
		else if(option.equalsIgnoreCase
			("fast property/casualty actuary"))
		    {
			// Again, these are roughly based on 
			// www.dwsimpson.com/salary.html
			array[0] = 66000; // 4 exams
			array[2] = 83000; // 6 exams
			array[4] = 105000; // ACAS
			array[6] = 130000; // 8 exams
			array[8] = 150000; // FCAS
			array[11] = 200000;
			array[16] = 280000;
			array[20] = 350000;
			array[24] = 400000;
		    }
		else if(option.equalsIgnoreCase("average life/health actuary"))
		    {
			// Again, these are roughly based on 
			// www.dwsimpson.com/salary.html
			array[0] = 52000; // 2 courses
			array[1] = 54000; // 3 courses
			array[3] = 60000; // 4 courses
			array[5] = 70000; 
			array[6] = 81000; // 5 courses
			array[8] = 100000; // ASA
			array[9] = 115000; // 7 courses
			array[12] = 130000; // 8 courses
			array[15] = 180000; // FSA
			array[20] = 220000;
		    }
		else if(option.equalsIgnoreCase("average pension actuary"))
		    {
			// Again, these are roughly based on 
			// www.dwsimpson.com/salary.html
			array[0] = 54000; // 2 courses
			array[1] = 56000; // 3 courses
			array[3] = 65000; // 4 courses
			array[5] = 77000; 
			array[6] = 83000; // 5 courses
			array[8] = 93000; // ASA
			array[9] = 98000; // 7 courses
			array[12] = 130000; // 8 courses
			array[15] = 180000; // FSA
			array[20] = 220000;
			array[23] = 224000;
		    }
		else if(option.equalsIgnoreCase("wall street quant"))
		    {
			// these are very rough guesses!
			array[0] = 120000;
			array[3] = 190000;
			array[12] = 350000;
		    }
		else
		    {
			System.out.println
			    ("There was an error with your chosen " +
			     "career.");
		    }
		
		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "linear");
	    }
	else if(status == Status.RETIRED)
	    {
		array[0] = 0;
		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "linear");
	    }
	else if(status == Status.DEAD)
	    {
		array[0] = 0;
		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "linear");
	    }
	
	return array;
    }

    public static int[] makeCostOfLiving(int size, int status, String option)
    {
	int[] array = new int[size];
	for(int i = 0; i < size; i++)
	    { array[i] = arrayMethods.NONSENSE; }
	
	if(status == Status.UNDERGRAD)
	    {
		array[0] = 0;

		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "no change");
	    }
	else if(status == Status.GRAD_SCHOOL)
	    {
		if(option.equalsIgnoreCase("math or science"))
		    { array[0] = 0; }
		else if(option.equalsIgnoreCase("business or law"))
		    { array[0] = 30000; }

		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "no change");
	    }
	else if(status == Status.WORKING)
	    {
		array[0] = 33000;

		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "no change");
	    }
	else if(status == Status.RETIRED)
	    {
		array[0] = 18000;
		array[5] = 28000; // medical expenses

		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "linear");
	    }
	else if(status == Status.DEAD)
	    {
		array[0] = 5000; // costs for others to take care of
		                 // the utilitarian's wealth

		array = arrayMethods.fillInMissingValues
		    (array, size, 0, "no change");
	    }

	return array;
    }
}
