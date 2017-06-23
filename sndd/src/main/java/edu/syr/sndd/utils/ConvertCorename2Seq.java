package edu.syr.sndd.utils;

import edu.syr.sndd.common.util.AppConstant;
import edu.syr.sndd.readData.ReadSNData;

public class ConvertCorename2Seq {

	/**
	 * This is an one time usage utility. Do not commit to github.
	 * @param args
	 * @throws Exception
	 * @since 2017-06-20
	 * @author sgan
	 */
	public static void main(String[] args) throws Exception {
		String outSortFileName = "C:\\dropbx\\Dropbox\\Stoney thesis docs\\chapter 2\\results-from-2012-03\\bosumtwi-proc-data-sort.txt";

	     String inputData[][] = ReadSNData.readInputData(AppConstant.INPUT_FILENAME);
	     
	     String coreName;
	     int coreNum;
	     int coreSeq;
	     int coreDepth;
	     String splitStr[] = new String[3];
	     String splitStr2[] = new String[2];
	     String depth;
	     
	     String coreFullSeq[] = new String[inputData.length];
	     
	     for (int i=2; i<= inputData.length; i++) {
	    	 coreName = inputData[i][0]; // BOS5C 1H-1 8CM
	    	 splitStr = coreName.split(" "); //1H-1
	    	 splitStr2 = splitStr[1].split("H-"); //1
	    	 coreNum = Integer.parseInt(splitStr2[0]); //
	    	 coreSeq = Integer.parseInt(splitStr2[1]);
	    	 depth = splitStr[2].split("(?i)CM")[0];
	    	 if (depth.toLowerCase().equals("cc")) {
	    		 coreDepth = 152;
	    	 } else
	    		 coreDepth = Integer.parseInt(depth);
	    	
	    	 coreFullSeq[i] = String.format("%02d%d%03d", coreNum, coreSeq, coreDepth);	
	    	 System.out.println(coreFullSeq[i]);
	     }
	     
	     
	}

}
