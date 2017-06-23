/*
 *   Document   : skewNormalDistribution
 *   Created on : Aug 24, 2011, 7:35:21 PM
 *   Author     : Stoney Q. Gan
 *   copyRight by: Stoney Q. Gan
 */
package edu.syr.sndd.fitSN;

import edu.syr.sndd.common.util.AppConstant;

public class DeviationEstimator {
        
    public static double estimateDeviation (double [] originalData, double [] estimatedData,
            int start, int end){       
        
        /* we like the estimated SN is generally below the original curve. Therefore, we separate
         * the negative error (estimated value above the original value) from positive error.
         * if the negative error is over 3%, we will reject this estimate.
         */
        double negativeError = 0;
        double positiveError = 0;
        double error;
        double negativeErrorAllowed = 0.1;
        double maxError = 2000000;
        double total = 0;
        
        //for (int i=1; i<AppConstant.dataPoint.length; i++){
        for (int i=start; i<=end; i++){
            total += originalData[i];
           // if (originalData[i]*100.0/curYmax > 5){ // ignore the bottom 5% of data
               
               /* Although the error should be area between two curve, there is no reason to proportion error to
                * the size of grains. Therefore, sum of difference of pdf of two curve is more proper for the error.
                * However, if original data has a negative value, the error should be estimate curve value.
                */
            if (originalData[i] > estimatedData[i]){
                positiveError += (originalData[i] - estimatedData[i]); // * 100 / originalData[i];
            } else {
                if (estimatedData[i] > 0.0){
                    negativeError += (estimatedData[i] - originalData[i]); // * 100 / estimatedData[i];
                }
            }
            
            /*
               if (originalData[i] < 0 )
                   total += originalData[i] * estimatedData[i];
               else
                   total += (originalData[i] * Math.abs(originalData[i] - estimatedData[i]));
             */
              //double x1 = Math.abs(originalData[i-1] * estimatedData[i-1]);
              // double x2 = Math.abs(originalData[i] * estimatedData[i]);
               //double high = AppConstant.dataPoint[i] - AppConstant.dataPoint[i-1];
               //total += (x1+x2)*high/2.0;
        }
       
        if (negativeError*100/total > negativeErrorAllowed)
            error = error = positiveError + negativeError * 10;
        else
            error = positiveError + negativeError;
        
        return error;
    }
   
    public static int [] dataVarianceEstimate(double [] data){
        int mean = ymaxAt(data);
        //double ccdp = data[mean];
        //double cdt = 99;

        int [] index = new int[2];
        int i = mean - 1;
        int j = mean + 1;
        double prevLeft = data[mean];
        double prevRight = data[mean];
        
        // 2012-09-09 look both sides to find ends before turn around 
        while ( i > 0 && data[i] <=  prevLeft ) {
                prevLeft = data[i--];
        }
        
        while (j < data.length - 1 && data[j] <= prevRight) {
           prevRight = data[j++];
        }
        
        /*
        while (ccdp < cdt && i > 0 && j < data.length - 1){
            if (data[i] > data[j])
                ccdp += data[i--];
            else 
                ccdp += data[j++];
        }
        */
        
        index[0] = i+1;
        index[1] = j-1;

        return index;
        
    }
        /*
         * define a weight function: weight of a point is a function of point distance from the mean. Thought is that
         * the point should have more weight near the mean, and less weight as distance away from the mean.
         * How about: exp(distance)?
 
    private static double getWeight(int intx){
        int distance = (int) Math.abs(intx - ymaxAt);
        if (distance == 0)
            return 1;
        
        return (1.0/(double)distance);
    }
     * 
     */
        /*
         * 10-01-2011
         */
    public static int ymaxAt(double data[]){
       int i =0;
       double ymax = 0;
       int ymaxAt=0;
       for (i=0; i<AppConstant.dataPoint.length; i++){
           if(data[i] > ymax ){
               ymax = data[i];
               ymaxAt = i;
           }
       }
       return ymaxAt;
    }
    
    public static double estimateDeviation (double [] originalData, double [] estimatedData, double confidLevel,
            double mean, double sd){
        
        double error;
        double total=0.0;
        
        double leftStart = mean - confidLevel*sd;
        double rightEnd = mean + confidLevel*sd;
        int leftIndex =0;
        while (AppConstant.dataPoint[leftIndex] < leftStart &&
                leftIndex < AppConstant.dataPoint.length -1)
            leftIndex++;
        
        int rightIndex = leftIndex;
        while (AppConstant.dataPoint[rightIndex] < rightEnd &&
                rightIndex < AppConstant.dataPoint.length - 1)
            rightIndex++;
        
        for (int i=leftIndex; i<rightIndex; i++){
            if (originalData[i] < 0)
                total += (estimatedData[i] * estimatedData[i]);
            else
                total += (originalData[i] - estimatedData[i])*(originalData[i] - estimatedData[i]);
        }
        
        error = total/AppConstant.dataPoint.length;
        
        return error;
    }
    
    public static double[] getResidue(double [] originalData, double [] estimatedData){
        double [] residualData = new double[originalData.length];
        
        for (int i=0; i<AppConstant.dataPoint.length; i++){
            residualData[i] = originalData[i] - estimatedData[i];
            if (residualData[i] < 0.0)
                residualData[i] = 0.0;
        }
        
        return residualData; 
    }
    
      public static void main(String[] args) {
    	  edu.syr.sndd.graphResults.GraphingData gd = new edu.syr.sndd.graphResults.GraphingData();
          DeviationEstimator.ymaxAt(gd.getGzData());
          ymaxAt(gd.getGzData());
          for (int i = 0; i< AppConstant.dataPoint.length; i++){
              //System.out.println(getWeight(i));
          }
    }
}
