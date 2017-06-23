package edu.syr.sndd.processingSN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.syr.sndd.common.util.AppConstant;
import edu.syr.sndd.fitSN.SNGenerator;
import edu.syr.sndd.readData.AgeUtils;

public class SortSN {
	private static final int firstVolIndex = 11;
	private static final int firstOmegaIndex = 3;
	private static final int firstXiIndex = 4;
	private static final int firstAlphaIndex = 5;
	private static final int firstMeanIndex = 6;
	private static final int firstFscaleIndex = 9;
	private static final int numValues = 9;
	
	private static double fGSD [];
	private static double cGSD [];
	
	public static double[] getfGSD() {
		return fGSD;
	}

	public static void setfGSD(double[] fGSD) {
		SortSN.fGSD = fGSD;
	}

	public static double[] getcGSD() {
		return cGSD;
	}

	public static void setcGSD(double[] cGSD) {
		SortSN.cGSD = cGSD;
	}

	private static String sortSNByVol(String sns) {
		//sampleID	sample seq	number of SNs	omega	xi	alpha	mean	variance	skewness	fdataSN	dataMax	vol_percent
		String [] elements = sns.split("\t");
		int numSN = Integer.parseInt(elements[2]);
		
		String sorted ="";
		
		for (int i=0; i<numSN; i++) {
			double max = Double.valueOf(elements[firstVolIndex + i * numValues]);
			int maxI = firstVolIndex + i * numValues;
			for (int j=i; j<numSN; j++) {
				int jj = firstVolIndex + j * numValues;
				double v = Double.valueOf(elements[jj]);
				if (max < v) {
					max = v;
					maxI = jj;
				}
			}
			
			if (maxI != firstVolIndex + i * numValues) {
				// swap
				for (int k=0; k<numValues; k++) {
					String tmp = elements[firstOmegaIndex + i * numValues + k];
					elements[firstOmegaIndex + i * numValues+k] = elements[maxI-(numValues - k) + 1];
					elements[maxI - (numValues - k) + 1] = tmp;
				}
			}
		}
		
		for (int i=0; i< elements.length; i++)
			sorted += elements[i] + "\t";
		
		return sorted;
	}
	
	/**
	 * partition to dominant, finer and coarse grain portion percentage. 	
	 * input format: sampleID	sample seq	number of SNs	omega	xi	alpha	mean	variance	skewness	fdataSN	dataMax	vol_percent
	 * 
	 * @since 2015-01-21
	 * @param sns sorted SN's, i.e., the first one is dominant one.
	 * @return String of volume percentages: finer%, dominant% and coarse grain% after number SN's
	 */
	private static String tripleVolPartition(String sns) {
		
		double finerGSD [] = new double[AppConstant.COL_TITLE.length];
		double coarseGSD [] = new double[AppConstant.COL_TITLE.length];

		String [] elements = sns.split("\t");
		int numSN = Integer.parseInt(elements[2]);
		
		double fpct = 0.0;
		double cpct = 0.0;
		double dsize = Double.parseDouble(elements[firstMeanIndex]);
		for (int i=1; i<numSN; i++) {	
			double omega = Double.parseDouble(elements[firstOmegaIndex + i*numValues]);
			double xi = Double.parseDouble(elements[firstXiIndex + i*numValues]);
			double alpha = Double.parseDouble(elements[firstAlphaIndex + i*numValues]);
			double fscale = Double.parseDouble(elements[firstFscaleIndex + i*numValues]);
			double sn[] = SNGenerator.generateSN(omega, xi, alpha);
			
			if (Double.parseDouble(elements[firstMeanIndex + i * numValues]) > dsize) {
				cpct += Double.parseDouble(elements[firstVolIndex + i * numValues]);
		    	int j=0;
				for (double a: sn) {
		            coarseGSD[j++] += a *fscale;
		         }
			} else {
				fpct += Double.parseDouble(elements[firstVolIndex + i * numValues]);
				int j=0;
		    	for (double a: sn) {
		            finerGSD[j++] += a *fscale;
		         }
			}
		}
		
		String result = elements[0] + "\t" + elements[1] + "\t" + elements[2];
		result += "\t" + fpct + "\t" + elements[firstVolIndex] + "\t" + cpct;
		for (int i=3; i<elements.length; i++){
			result += "\t" + elements[i];
		}
		
		setfGSD(finerGSD);
		setcGSD(coarseGSD);
		
		return result;
	}
	
	/**
	 * cal gs distribution for dominant component
	 * @since 2015-01-21
	 */
	private static String getDominantGSD(String sns){
		String [] elements = sns.split("\t");
		double omega = Double.parseDouble(elements[3]);
		double xi = Double.parseDouble(elements[4]);
		double alpha = Double.parseDouble(elements[5]);
		double fscale = Double.parseDouble(elements[9]);
		
		String result = "";
		double sn[] = SNGenerator.generateSN(omega, xi, alpha);
    	for (double a: sn) {
           result += "\t" + a *fscale;
        }
    	
    	return result;
	}
	
	/**
	 * calculate gsd for the 2nd components
	 * @param sns
	 * @return
	 */
	private static String get2GSD(String sns){
		String [] elements = sns.split("\t");
		double omega = 0;
		double xi = 0;
		double alpha = 0;
		double fscale =0;
		String result = "";
		
		if (Double.parseDouble(elements[2]) > 1) { // check number of sns
			 omega = Double.parseDouble(elements[3+numValues]);
			 xi = Double.parseDouble(elements[4+numValues]);
			 alpha = Double.parseDouble(elements[5+numValues]);
			 fscale = Double.parseDouble(elements[9+numValues]);
	

			double sn[] = SNGenerator.generateSN(omega, xi, alpha);
	    	for (double a: sn) {
	           result += "\t" + a *fscale;
	        }
		} else {
			for (int i=0; i<AppConstant.dataPoint.length; i++) {
		          result += "\t" + 0;
		    }
		}
    	return result;
	}
	
	private static String getFinerGSD(String sns){
		String [] elements = sns.split("\t");
		double omega = Double.parseDouble(elements[3]);
		double xi = Double.parseDouble(elements[4]);
		double alpha = Double.parseDouble(elements[5]);
		double fscale = Double.parseDouble(elements[9]);
		
		String result = "";
		double sn[] = SNGenerator.generateSN(omega, xi, alpha);
    	for (double a: sn) {
           result += "\t" + a *fscale;
        }
    	
    	return result;
	}
		
	/**
	 * Read from SN output and sorted by their volmne percentage.
	 * @param args
	 * @throws IOException
	 * @since 2015-01.
	 */
	public static void main(String[] args) throws IOException {
		String fileName = "H:\\dissertation\\chapter2\\bosumtwi-output-2014.txt";
		String outFileName = "H:\\dissertation\\chapter2\\bosumtwi-output-2014-vol-sorted-parted.txt";
		String outFileNamedGSD = "H:\\dissertation\\chapter2\\bosumtwi-output-2014-dGSD.txt";
		String outFileName2GSD = "H:\\dissertation\\chapter2\\bosumtwi-output-2014-2GSD.txt";
		String outFileNamefGSD = "H:\\dissertation\\chapter2\\bosumtwi-output-2014-fGSD.txt";
		String outFileNamecGSD = "H:\\dissertation\\chapter2\\bosumtwi-output-2014-cGSD.txt";
		
		List<String> inData = AgeUtils.read2StrList(fileName, true);
		List<String> outData = new ArrayList<String>();
		
		List<String> listdGSD = new ArrayList<String>();
		List<String> list2GSD = new ArrayList<String>();
		
		List<String> listfGSD = new ArrayList<String>();
		List<String> listcGSD = new ArrayList<String>();
		String str = "";
		
		for (int i=1; i<inData.size(); i++) {
//			System.out.println("original: " + inData.get(i));
//			System.out.println("sorted  : " + sortSNByVol(inData.get(i)));
//			System.out.println("triple P: " + tripleVolPartition(sortSNByVol(inData.get(i))));
			outData.add(tripleVolPartition(sortSNByVol(inData.get(i))));

			str = "";
			for (double a: getfGSD()){
				str += "\t" + String.valueOf(a);
			}
			
			listfGSD.add(str);
			
			str = "";
			for (double a: getcGSD()){
				str += "\t" + String.valueOf(a);
			}
			listcGSD.add(str);
			
			listdGSD.add(getDominantGSD(sortSNByVol(inData.get(i))));
			list2GSD.add(get2GSD(sortSNByVol(inData.get(i))));
		}
		
		String heading = "sampleID	sample_seq	number_SNs\tfiner vol\tdominant-vol\tcourser vol\t";
		for (int i=0; i<12; i++) {
			heading += "omega	xi	alpha	mean	variance	skewness	fdataSN	dataMax	vol_percent\t";
		}
		
		AgeUtils.write2File(outFileName, outData, heading);
		
		heading="";
		for (String a: AppConstant.COL_TITLE){
			heading += "\t" + a;
		}
		
		AgeUtils.write2File(outFileNamedGSD, listdGSD, heading);
		AgeUtils.write2File(outFileName2GSD, list2GSD, heading);
		
		AgeUtils.write2File(outFileNamefGSD, listfGSD, heading);
		AgeUtils.write2File(outFileNamecGSD, listcGSD, heading);
		
		System.out.println("I am done!");
	}

}
