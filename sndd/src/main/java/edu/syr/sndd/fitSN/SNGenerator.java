package edu.syr.sndd.fitSN;

/**
 *   Document   : skewNormalDistribution
 *   Created on : Aug 24, 2011, 7:35:21 PM
 *   Author     : Stoney Q. Gan
 *   copyRight by: Stoney Q. Gan
 */

import java.util.List;
import java.util.LinkedList;
import edu.syr.sndd.common.util.AppConstant;

public class SNGenerator {
    
    private final static double a1 =  0.254829592, a2 = -0.284496736;
    private final static double a3 =  1.421413741, a4 = -1.453152027;    
    private final static double a5 =  1.061405429;    
    private final static double p  =  0.3275911;
    
    private static double CDF(double x){       
       int sign = 1;
        if (x < 0)        
            sign = -1;  
        
        x = java.lang.Math.abs(x)/java.lang.Math.sqrt(2.0);    
        // A&S formula 7.1.26    
        double t = 1.0/(1.0 + p*x);    
        double y = 1.0 - (((((a5*t + a4)*t) + a3)*t + a2)*t + a1)*t*java.lang.Math.exp(-x*x);    
        return 0.5*(1.0 + sign*y);
    }
    
    private static double phiX(double x){
        double phi = java.lang.Math.exp(-x*x/2)/java.lang.Math.sqrt(java.lang.Math.PI*2);
        return phi;
    }
    
    public static double SNpdf(double x, double omega, double eposon, double alpha){
        double phi = phiX((x - eposon)/omega);
        double cdf = CDF(alpha * ( x - eposon)/omega);
        
        double pdf = 2 * phi * cdf / omega;
        return pdf;
    }
    
    public static double [] generateSN(double omega, double xi, double alpha){
        double [] sndata = new double[AppConstant.dataPoint.length];
        for (int i=0; i<AppConstant.dataPoint.length; i++){
            sndata[i] = SNGenerator.SNpdf(AppConstant.dataPoint[i], omega, xi, alpha);
            //System.out.println(" data [" + i + "] = " + data[i] + " dataPoint = " + AppConstant.dataPoint[i]);
        }
  
       return sndata;
    }
    
    public static double [] generateSN(SNParameters snp){
        double [] sn = generateSN(snp.getOmega(),snp.getXi(), snp.getAlpha());
        for (int i=0; i< AppConstant.dataPoint.length; i++)
            sn[i] *= snp.getfDataSN();
        
        return sn;
    }
    
    public static double computeMean(double aomega, double axi,  double alpha){
        return (axi + aomega * alpha * Math.sqrt(2/Math.PI)/Math.sqrt(1+alpha*alpha));
    }
    
    public static double computeVariance(double aomega, double axi, double alpha){
        double delta = alpha / Math.sqrt(1+alpha*alpha);
        return (aomega*aomega *( 1 - 2*delta*delta/Math.PI));
    }
    
    public static double computeSkewness(double aomega, double axi, double alpha){
       double delta = alpha / Math.sqrt(1+alpha*alpha);
       double f1 = (4-Math.PI)/2;
       double f2 = Math.pow((delta*Math.sqrt(2/Math.PI)), 3);
       double f3 = Math.pow((1-2*delta*delta/Math.PI), 1.5);
       return (f1*f2/f3);
    }
    
    public static void main(String [ ] args){
        double y[] = {0.00134989803163, 0.158655253931, 0.5, 0.691462461274, 0.982135579437 };
        System.out.println("CDF (0) should be 0.5: " + CDF(0));
       
        for (int i=0; i< AppConstant.dataPoint.length; i++){
            //System.out.println("data point: " + dataPoint[i] +  " error " + (CDF(dataPoint[i])-y[i]));
            System.out.println("data point (" + i + " )" + AppConstant.dataPoint[i] +  " = " + 
                                SNpdf(AppConstant.dataPoint[i], 1, 17.0, 0) +
                                " phi (x): " + phiX(AppConstant.dataPoint[i]));
        }
        
    }
    
    /*
    public void fittingSN(double data[]){
        List peaks = findPeaks(data);
        int mode = peaks.size();
        double [][] snPdfData = new double[mode][NUM_DATA_POINTS];
        
        boolean satisfactory = false;
        
        SNParameters snp[] = initParameters(peaks);
        // here implement simulated annealing algorithm.
        while (!satisfactory){
            for (int i=0; i< mode; i++){
                snPdfData[i] = generateSN(snp[i].omega, snp[i].eposon, snp[i].alpha);
            }
            
            double error = DeviationEstimator.estimateDeviation(data, snPdfData);
            
            if (error == 0){
                satisfactory = true;
            } else {
                ParameterTuner.tuneParameters(snp);
            }
        }
    }
    
    private SNParameters[] initParameters(List peaks) {
        SNParameters [] snp = new SNParameters[peaks.size()];
        Peak apeak = new Peak();
        
        double total =0.0;
        
        for (int i=0; i< peaks.size(); i++){
             apeak = (Peak)(peaks.get(i));
             total += apeak.value;
        }
        
        for (int i=0; i< peaks.size(); i++){            
            snp[i].omega = total * 100.0 / ((Peak) peaks.get(i)).value;
            snp[i].alpha = 0;
            snp[i].eposon = ((Peak) peaks.get(i)).position;
        }
        return snp;
    }
    
    private List findPeaks( double data[]){
        double curMax = 0;
        double curMin = 2000.0;

        List peak = new LinkedList();
        
        int curTrend = UP_TREND;
        for (int i=0; i< data.length; i++){
            if (curTrend == UP_TREND) {
                if (data[i] > curMax) { // keep up trend
                    curMax = data[i];
                } else { // turn to down trend
                    Peak apeak = new Peak();
                    apeak.position = i;
                    apeak.value = curMax;
                    peak.add(apeak);
                    
                    curMin = data[i];
                    curTrend = DOWN_TREND;
                }
            } else { // curTrend is down
                if (data[i] < curMin){ // keep the down trend
                    curMin = data[i];
                } else {
                    curMax = data[i]; // become up trend;
                    curTrend = UP_TREND;
                }
            }
        }
        
        return peak;
    }
    
    private class Peak {
      public double value;
      public int position;
    }
  */
}
