package edu.syr.sndd.utils;

import java.util.ArrayList;
import java.util.List;

import edu.syr.sndd.readData.AgeUtils;
import edu.syr.sndd.readData.ReadGSData;


public class calStats {
	static final String inFileName = "H:\\dissertation\\chapter2\\bosumtwi-proc-data-sorted.xlsx";
	static final String outFileName = "H:\\dissertation\\chapter2\\bosumtwi-outdata-stats.txt";
	static String inputData[][];
	static int R, C;
	static double sizeLog2 [];
	static {
		  
	    try {
			inputData = ReadGSData.readInputData(inFileName);
			R = inputData.length;
		    C = inputData[0].length;
		    sizeLog2 = new double[C];
		    
		    for (int i=1; i<C; i++) {
				sizeLog2[i] = Math.log10(Double.parseDouble(inputData[0][i])) / Math.log10(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static double [] calDataMean() {
		double mean[] = new double[R];
		
		for (int i=1; i<R; i++) {
			double total = 0;
			for (int j=1; j< C; j++) {
				total += Double.parseDouble(inputData[i][j])/100 * sizeLog2[j];
			}
			
			mean[i] = total;
			System.out.println("This is the mean for sample" + inputData[i][0] + " " + mean[i]);
		}
		
		return mean;
	}
	
	private static double [] calDataVariance(double mean[]) {
		double variance[] = new double[R];
		for (int i=1; i<R; i++) {
			double total = 0;
			for (int j=1; j< C; j++) {
				total += Double.parseDouble(inputData[i][j])/100 * sizeLog2[j] * sizeLog2[j];
			}
			
			variance[i] = total - mean[i]*mean[i];
			System.out.println("This is the variance for sample" + inputData[i][0] + " " + variance[i]);
		}
		
		return variance;
	}
	
	private static double [] calData3Moment(double mean[], double variance[]) {
		double m3[] = new double[R];
		
		for (int i=1; i<R; i++) {
			double total = 0;
			for (int j=1; j< C; j++) {
				total += Double.parseDouble(inputData[i][j])/100 * Math.pow((sizeLog2[j] - mean[i])/Math.sqrt(variance[i]), 3);
			}
			
			m3[i] = total;
			System.out.println("This is the skewness for sample" + inputData[i][0] + " " + m3[i]);
		}
		
		return m3;		
	}
	public static void main(String[] args) throws Exception {
		 
	     double mean[] = calDataMean();
	     double var[] = calDataVariance(mean);
	     double skew[] = calData3Moment(mean, var);
	     List<String> listStat = new ArrayList<String>();
	     
	     for (int i=1; i < R; i++) {
	    	 skew[i] = skew[i]/Math.pow(var[i], 1.5);
	    	 listStat.add(inputData[i][0] + "\t" + mean[i] + "\t" + var[i] + "\t" + skew[i]);
	    	 System.out.println("This is the skewness for sample" + inputData[i][0] + " " + skew[i]);
	     }
	     AgeUtils.write2File(outFileName, listStat, "sample name\tSample mean\tSample variance\tSample skewness");
	     System.out.println("I am done!");
	}

}
