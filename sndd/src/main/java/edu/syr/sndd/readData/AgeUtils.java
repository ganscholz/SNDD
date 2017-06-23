package edu.syr.sndd.readData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a utility class to read the age model data, converts a depth to its age. It does not implement any constructor but read the
 * age model data in the static block.
 * @author Stoney Q. Gan
 *
 */
public  class AgeUtils {
	protected static double modelDepth[], modelAges[];
	
	private static final String DA_INPUT_FILE = "C:\\Chapter 2 Dissertation\\newAgeEvenDepth.txt";
	/**
	 * Read the depth-age model published by Shanahan et al, 2012.
	 */
	static {
	
			try {
				readDepthAgeData(DA_INPUT_FILE);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
	
	}
	
	/**
	 * This method assumes that the depth-age data file is a text file which only has two columns: first column is depth in cm and second column is age data in years.
	 * @param fileName depth-age model file name.
	 * @throws IOException
	 */
	private static void readDepthAgeData(String fileName) throws IOException {
		int i=0;
		
		List<String> strList = AgeUtils.read2StrList(fileName);
		
	    modelDepth = new double[strList.size()];
	    modelAges = new double[strList.size()];
	    String strCols[] = new String[2];
	    
	    while (i < strList.size())
	    {
	    	strCols = strList.get(i).split("\\s+");
	    	modelDepth[i] = Double.parseDouble(strCols[0]);
	    	modelAges[i] =  Double.parseDouble(strCols[1]);
	    	i++;
	    }
	}
	
	/**
	 * Calculated the age for a given depth using simple linear interpolation. 
	 * @param depth depth in cm
	 * @return the age in years.
	 */
	public static double depth2age(double depth) {
		double age;
		int j = 0;
		while (depth > (int)(modelDepth[j]) && j < modelDepth.length - 1)
		{
			j++;
		}
		
		 if (modelDepth[j] == depth)
	    {
		 	age = modelAges[j];
	    } else
	    {
	    	if (j == modelDepth.length - 1 )
	    		age = modelAges[j];
	    	else {
		        double slope=(modelAges[j]-modelAges[j-1])/ ((double)(modelDepth[j]-modelDepth[j-1]));
	
		        age = modelAges[j-1] + (double)(depth - modelDepth[j-1])* slope; 
	    	}	        
	    }
		 
		 return age;
	}
	
	/**
	 * Read data from "fileName" and store them in a list of string -- each line is an element in the list.
	 * @param fileName assumed it is a text file.
	 * @return a list of String.
	 * @throws IOException
	 */
	public static List<String> read2StrList(String fileName) throws IOException {
		 	BufferedReader br = new BufferedReader(new FileReader(fileName));
		    String line;
		    List<String> strList = new ArrayList<String>();
		    
		    try {
		    	line = br.readLine(); // read the heading line.
		        while ((line = br.readLine()) != null)
		        { 
		        	strList.add(line.trim());
		        }
		    } finally {
		    	br.close();
		    }
		   
		    return strList;
	}
	
	/**
	 * read data from the input file and return as a list of String. Includes the headings as the first element if hd is true, otherwise, read and discard the first line.
	 * @param fileName input file assumed to be a text file.
	 * @param hd indicate whether to return the haeding line.
	 * @return a list of strings each of which is a depth-age pair.
	 * @throws IOException
	 */
	public static List<String> read2StrList(String fileName, boolean hd) throws IOException {
		 	BufferedReader br = new BufferedReader(new FileReader(fileName));
		    String line;
		    List<String> strList = new ArrayList<String>();
		    
		    try {
		    	line = br.readLine(); // read the heading line.
		    	if (hd) // add to the list if it is true.
		    		strList.add(line.trim());
		    	
		        while ((line = br.readLine()) != null)
		        { 
		        	strList.add(line.trim());
		        }
		    } finally {
		    	br.close();
		    }
		   
		    return strList;
	}
	
	/**
	 * get the closet power 2 integer for a given n.
	 * @param n
	 * @return
	 */
	public static int getClosePower2(int n){
		double pow2 = Math.log10(n)/Math.log10(2);
		pow2 = (int)(pow2 + 0.5);
		return (int)(Math.pow(2, pow2));
	}
	
	/**
	 * This method gets a list of evenly age spaced age-depth pairs by resampling with a fixed age interval. Resampling uses the 
	 * simple linear interpolation.
	 * @param idepth input of depth array;
	 * @param iage input of age array;
	 * @param interval age interval;
	 * @return A list of age/depth pair <Age "\t" Depth>
	 */
	public static List<String> linearInterpolate(double idepth[], double iage[], double ageInterval)
	{
		List<String> outDepth = new ArrayList<String>();
		
		double lastAge, nextAge, thisAge;
		double lastDepth, thisDepth, nextDepth;
		
		lastAge= iage[0];
		nextAge = iage[1];
		lastDepth=idepth[0];
		nextDepth = idepth[1];
		thisAge = calFirstAge(iage[0], ageInterval);
		
		int i=2;
	
		while ( thisAge < iage[iage.length-1] && i < iage.length){
			if (thisAge <= nextAge)
			{
				double slope = (nextDepth-lastDepth)/(nextAge-lastAge);
				thisDepth = lastDepth + slope * (thisAge - lastAge);
				outDepth.add(String.valueOf(thisAge) + "\t" +  String.valueOf(thisDepth));
				thisAge += ageInterval;
			} else {			
				lastAge= nextAge;
				nextAge = iage[i];
				lastDepth=nextDepth;
				nextDepth = idepth[i];
				i++;
			}
			
		}
		
		return outDepth;
	}
	
	/**
	 * Write a list of string to a file.
	 * @param fileName to be written into
	 * @param adList a list of String to write
	 * @param heading the heading string that will be written to the first line in the file.
	 * @throws IOException
	 */
	public static void write2File(String fileName, List<String> adList, String heading) throws IOException
	{
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    
	    writer.write(heading+"\n");
	    int i=0;
	    while (i < adList.size())
	    {
	    	writer.write (adList.get(i++).toString() + "\n");
	    }

	    //Close writer
	    writer.close();
	}
	
	/**
	 * Write an array of double to a file.
	 * @param fileName output file name
	 * @param a the array of double to write to the file.
	 * @param heading the heading string.
	 * @throws IOException
	 */
	public static void write2File(String fileName, double a[], String heading) throws IOException
	{
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    
	    writer.write(heading+"\n");
	    int i=0;
	    while (i < a.length)
	    {
	    	writer.write (a[i++] + "\n");
	    }

	    //Close writer
	    writer.close();
	}
	
	/**
	 * Write an array of int to a file.
	 * @param fileName
	 * @param a
	 * @param heading
	 * @throws IOException
	 */
	public static void write2File(String fileName, int a[], String heading) throws IOException
	{
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    
	    writer.write(heading+"\n");
	    int i=0;
	    while (i < a.length)
	    {
	    	writer.write (a[i++] + "\n");
	    }

	    //Close writer
	    writer.close();
	}
	
	/**
	 * Find an first age for a time series by the given original initial age and age interval. This is to prepare the first data in
	 * an evenly spaced time series.
	 * @param initAge the first age value.
	 * @param ageInterval the age interval for the time series.
	 * @return
	 */
	public static double calFirstAge(double initAge, double ageInterval)
	{
		return ((double) (((int)(initAge/ageInterval)) * ageInterval));
	}
	
	/**
	 * return the column colIndex (starting 0) as an array of double;
	 */
	public static double[] getColumn(List<String> strList, int colIndex, int numCols){
		double colData[] = new double[strList.size()];
		String tmpStr[] = new String[numCols];
		for (int i=0; i<strList.size(); i++){
			tmpStr = strList.get(i).split("\\s+");
			colData[i] = Double.valueOf(tmpStr[colIndex]);
		}
		
		return colData;
	}
	/**
	 *  Implemented the input side convolution algorithm by Steven Smith, 1998
	 * @param inputSignals an array of doubles that to be convoluted.
	 * @param aWin the window (system) the input signals will go through.
	 * @return the output signals by convoluted the input signal with the window.
	 */
	public static double [] inputSideConvolution(double [] inputSignals, double [] aWin)
	{
		double [] outSignals = new double[inputSignals.length + aWin.length - 1];
		
		int i, j;
		for (i=0; i< inputSignals.length + aWin.length - 1; i++)
			outSignals[i] = 0.0;
		
		for (i=0; i< inputSignals.length; i++)
		{			
			for (j=0; j<aWin.length; j++)
			{
				outSignals[i+j] = outSignals[i+j] + (inputSignals[i] * aWin[j]);
				//System.out.println(" i " + i + " j " + j + "in " + inputSignals[i] + " awin " + aWin[j] + " out " + outSignals[i+j]);
			}
		}
		
		return outSignals;
	}
	
	/**
	 * set all elements to 0 for this array;
	 * @param anarray
	 * @return
	 */
	public static int [] initIntArray(int anarray[]) {
		int returnedValues [] = new int[anarray.length];
		for (int i=0; i<anarray.length; i++)
			returnedValues[i] = 0;
		return returnedValues;
	}
	
	/**
	 * set all elements to 0 for this array;
	 * @param anarray
	 * @return
	 */
	public static double [] initDoubleArray(double anarray[]) {
		double returnedValues [] = new double[anarray.length];
		for (int i=0; i<anarray.length; i++)
			returnedValues[i] = 0;
		return returnedValues;
	}
	
	/**
	 * convert 5B depth data to Age.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		AgeUtils.readDepthAgeData("C:\\Chapter 2 Dissertation\\newAgeEvenDepth.txt");
		List<String> listCNData = AgeUtils.read2StrList("C:\\Chapter 3 Dissertation\\CNData.txt", true);
		String headings = "age\t" + listCNData.get(0);
		for (int i=1; i<listCNData.size(); i++){
			double age = AgeUtils.depth2age(Double.parseDouble(listCNData.get(i).split("\\s+")[0]));
			
			listCNData.set(i-1, age + "\t" + listCNData.get(i));
		}
		listCNData.remove(listCNData.size() - 1);
		
		AgeUtils.write2File("C:\\Chapter 3 Dissertation\\2016-10-newAge\\AgeCNData.txt", listCNData, headings);
	}
}
