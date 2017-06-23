/**
 * Class Name:   AppConstant.java
 * Copyright:    Copyright (c) 2011
 * Date:         09-06-2011
 * Company:      Syracuse University
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:
 * </p>
 * <p>
 *       This class defines the common used constants used in SNDD project. It is self-explanatory for each constant. 
 * </p>
 */

package edu.syr.sndd.common.util;

public class AppConstant {    
    public static String DATETIME_DISPLAY_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_DISPLAY_FORMAT = "yyyy-MM-dd";
    public static String EXCEL_DISPLAY = "EXCEL";
    public static String HTML_DISPLAY = "HTML";
    
    public static final double SN_CONFIDENCE = 0.999;
    public static final double SN_PARAMETER_ERROR=0.0001;
    
    public static final int DECIMAL_PLACE=4;
    
    public static double DT_THRESHOLD = 0.8;
    
    public static String INPUT_FILENAME = "src\\test\\bosumtwi-proc-data-sorted.xlsx";    
    public static String OUTPUT_FILENAME = "src\\test\\bosumtwi-output-2014.txt";
//    public static String INPUT_FILENAME = "H:\\dissertation\\chapter2\\Bruce-samples-proc-data.xlsx";    
//    public static String OUTPUT_FILENAME = "H:\\dissertation\\chapter2\\Bruce-samples-output.txt";
//    public static String INPUT_FILENAME = "H:\\dissertation\\chapter2\\VolPartitionResult.xlsx";    
//    public static String OUTPUT_FILENAME = "H:\\dissertation\\chapter2\\VolPartitionResult-output.txt";
    
    public static String EXCEL_TYPE_STRING = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    public static final double dataPointDec[] = {0.375, 0.412,0.452, 0.496, 0.545, 0.598, 0.656, 0.721, 0.791, 0.868, 
                    0.953, 1.047, 1.149, 1.261, 1.385, 1.52, 1.668, 1.832, 2.011, 2.207, 2.423, 2.66, 2.92, 3.205,
                    3.519, 3.863, 4.24, 4.655, 5.11, 5.61, 6.158, 6.76, 7.421, 8.147, 8.943, 9.817, 10.78, 11.83, 
                    12.99, 14.26, 15.65, 17.18, 18.86, 20.7, 22.73, 24.95, 27.39, 30.07, 33.01, 36.24, 39.78, 43.67, 
                    47.94, 52.62, 57.77, 63.41, 69.61, 76.42, 83.89, 92.09, 101.1, 111, 121.8, 133.7, 146.8, 161.2,
                    176.9, 194.2, 213.2, 234.1, 256.9, 282.1, 309.6, 339.9, 373.1, 409.6, 449.7, 493.6, 541.9, 594.9, 
                    653, 716.8, 786.9, 863.9, 948.3, 1041, 1143, 1255, 1377, 1512, 1660};
    public static final double [] dataPoint = getDatapointLog();
    
    public static final String [] COL_TITLE = getColTitle();
     //public static double dataPoint[] = new double[1000];
    public final static int NUM_TICS = 10;
    public static String SUCCESS = "success";
    public static String FAILURE = "failure";
    
    public static final int OMEGA = 1;
    public static final int XI = 2;
    public static final int ALPHA = 3;
    
    public static final double OMEGA_MIN = 0.01;
    public static final double OMEGA_MAX = 3;
    public static final double OMEGA_INCREMENT = 0.06; // 0.5; 0.06; 0.15
    
    public static final double XI_MIN = -2;
    public static final double XI_MAX = 10.0;
    public static final double XI_INCREMENT = 0.2; //2; 0.2; 0.5
    
    public static final double ALPHA_MIN = -6;
    public static final double ALPHA_MAX = 6;
    public static final double ALPHA_INCREMENT = 0.12; // 1.0; 0.12; 0.3
    
    public static final double FINE_GRAIN_CUTOFF = 4;
  
    public static final double RES_DATA_VOL_CUTOFF = 0.5; //max volume in the residual / max volume in data.
    private static String [] getColTitle(){
        String [] strColTitle = new String[AppConstant.dataPointDec.length + 1];
        strColTitle[0] = "Sample Name";
        for (int i=0; i< AppConstant.dataPointDec.length; i++){
         strColTitle[i+1] = String.valueOf(AppConstant.dataPointDec[i]);
        }
        
        return strColTitle;
    }
    
    private static double [] getDatapointLog(){
       double [] dataPointLog = new double[AppConstant.dataPointDec.length];
       for (int i=0; i< AppConstant.dataPointDec.length; i++)
            dataPointLog[i] = Math.log10(AppConstant.dataPointDec[i])/Math.log10(2);
       
       return dataPointLog;
    }
}