/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.fitSN;

import edu.syr.sndd.common.util.AppConstant;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.syr.sndd.processingSN.PartitionVolume;

/**
 *
 * @author eximlean
 */
public class SNUtils {
    static final int MIN = -1;
    static final int MAX = 1;
    static final DecimalFormat df = new DecimalFormat("#.####");  
    static final Logger logger=Logger.getLogger(SNUtils.class.getName());
    
    private double omega;
    private double xi;
    private double alpha;
    private double fscale;
    private double omega_min = 0.01;
    private double omega_max = AppConstant.OMEGA_MAX;
    private double omega_increment = AppConstant.OMEGA_INCREMENT;
    private double xi_min = AppConstant.XI_MIN;
    private double xi_max = AppConstant.XI_MAX;
    private double xi_increment = AppConstant.XI_INCREMENT;
        
    private double alpha_min = AppConstant.ALPHA_MIN;
    private double alpha_max = AppConstant.ALPHA_MAX;
    private double alpha_increment = AppConstant.ALPHA_INCREMENT;
    
    private double gzDataMax=0.0;
    
    private boolean debug=true;
/**
     * Intention of this method is to standardize GS data to overall average. It is assumed that the first row in the 
     * allGZStrData is the overall GZ average for the dataset. 
     * @param allGZStrData all the GZ samples in string type
     * @throws Exception 
     */
public void standardizeGZData(String allGZStrData[][]) throws Exception {         
    // open output files
    String outfileName =  AppConstant.OUTPUT_FILENAME + "-stdz";
    PrintStream out=null;
    try {
        out = new PrintStream(new FileOutputStream(outfileName));
    } catch (FileNotFoundException ex) {
        throw ex;
    }
    
    // print the headings
    for (String colT: AppConstant.COL_TITLE) {
        out.print(colT + "\t");
    }
    out.println();
    
    double [] allAveGZ = new double[AppConstant.dataPoint.length];
    double [] gzData=null;
    try {
        gzData = getGZData(allGZStrData, 1);
        for (int j=0; j< gzData.length; j++){
             allAveGZ[j] = gzData[j];
        }
    } catch (Exception ex) {
        Logger.getLogger(SNUtils.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    for (int i=2; i< allGZStrData[0].length; i++){                    
       gzData = getGZData(allGZStrData, i);
       out.print(allGZStrData[i][0] + "\t");
       double total = 0;
       for (int j=0; j< gzData.length; j++){
            gzData[j] -= allAveGZ[j];
            total += gzData[j];
        }
        
        for (int j=0; j< gzData.length; j++){
            gzData[j] = (gzData[j]*100/total);
            out.print(df.format(gzData[j]) + "\t");
        }
       out.println();
    }
    out.close();
}

public void partitionSN(String allGZStrData [][], boolean stdz){
    java.util.List<java.util.List<SNParameters>> listSnps=null;
    try {   
        // open an output file
        String outfileName =  AppConstant.OUTPUT_FILENAME;
        String outfile2 = outfileName + "-mean";
        
        if (stdz){
            outfileName += "-stdz";
            outfile2 += "-stdz";
        }
        
        PrintStream out = new PrintStream(new FileOutputStream(outfileName));
        PrintStream out2 = new PrintStream (new FileOutputStream(outfile2));

        out.println("sampleID\tsample seq\tnumber of SNs\tomega\txi\talpha\tmean\tvariance\tskewness\tfdataSN\tdataMax\tvol_percent");
        out2.println("mean\tvol %");

        String prevSampleName="";
        PartitionVolume pv = new PartitionVolume();
        double [] allAveGZ = null;
        double [] gzData = null;
        if (stdz) {
            allAveGZ = new double[AppConstant.dataPoint.length];

            try {
                gzData = getGZData(allGZStrData, 1);
                for (int j=0; j< gzData.length; j++){
                     allAveGZ[j] = gzData[j];
                }
            } catch (Exception ex) {
                Logger.getLogger(SNUtils.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        int sample_seq = 1;
        int xx = 1;
        if (stdz)
            xx = 2;
        
        for (int i=xx; i< allGZStrData[0].length; i++){
            System.out.println("sample " + i);                   
            try {
                gzData = getGZData(allGZStrData,i);
                double y_min = getMinMax(gzData, MIN);
                if (stdz) {
                    for (int kk = 0; kk<gzData.length; kk++){
                        gzData[kk] -= (allAveGZ[kk] + y_min);
                    }
                }
                
                java.util.List<SNParameters> snps = nmodeSNEstimate(allGZStrData[i][0], gzData);
                listSnps.add(snps); // 2012-09-22 they will show when go next/prev for refinement....
                Iterator itr = snps.iterator();
                if (allGZStrData[i][0].equalsIgnoreCase(prevSampleName)){
                    sample_seq ++;
                } else
                    sample_seq = 1;
                out.print(allGZStrData[i][0] + "\t" + sample_seq + "\t" +  snps.size());
                out2.print(allGZStrData[i][0]);

                while (itr.hasNext()) {
                    SNParameters nps = (SNParameters) itr.next();
                    nps.setSampleName(allGZStrData[i][0]); // need to set in plotPanel.snps.
                    double om = nps.getOmega();
                    double xi = nps.getXi();
                    double al = nps.getAlpha();
                    double mn = SNGenerator.computeMean(om, xi, al);
                    double va = SNGenerator.computeVariance(om, xi, al);
                    double sk = SNGenerator.computeSkewness(om, xi, al);
                    double volPC = pv.calVolPercent(SNGenerator.generateSN(nps), gzData);
                    out.print("\t" + df.format(om) + "\t" + df.format(xi) + "\t" 
                            + df.format(al) + "\t" + df.format(mn) + "\t" + df.format(va) + "\t" + df.format(sk)
                            + "\t" + nps.getfDataSN()  + "\t" + nps.getDataMax() + "\t" + volPC);
                    out.flush();

                    out2.print("\t" + df.format(mn) + "\t" + df.format(volPC));
                }  
                out.println();
                out2.println();
            } catch (Exception ex) {
                Logger.getLogger(SNUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            prevSampleName = allGZStrData[i][0];
        }
        out.close();
        out2.close();
    } catch (IOException ex) {
        Logger.getLogger(SNUtils.class.getName()).log(Level.SEVERE, null, ex);
    } 
}
    /**
     *  Using the current set of parameter ranges and their increments to find the best fit between gzData and 
     * calculated sn.
     * @return min SD;
     */
private double oneLevelEstimate(double [] residualData){        
    double curMin = 10000000.0;
    double omegaCurMin=1;
    double xiCurMin=0;
    double alphaCurMin=0;

    double [] calSN = new double[AppConstant.dataPoint.length];

    // this is necessary because we want to match individual SN instead of overall curve.
    int [] idx = DeviationEstimator.dataVarianceEstimate(residualData);
    int start = idx[0];
    int end = idx[1]; 

    double residualYmax = getMinMax(residualData, MAX);
    int ymaxatData = DeviationEstimator.ymaxAt(residualData);

    int closeEnough = 1;
    for (double x = omega_min; x<=omega_max; x+= omega_increment){
        for (double y = xi_min; y<= xi_max; y+= xi_increment){
            for (double z= alpha_min; z<= alpha_max; z+= alpha_increment){
                omega = x;
                xi = y;
                alpha = z;
                calSN = calculateSN(residualYmax);
                    //System.out.println("data peak at: " + ymaxatData + " value: " + residualYmax + " peak is at: " + DeviationEstimator.ymaxAt(calSN)+ " value " + calSN[DeviationEstimator.ymaxAt(calSN)]);
                if (Math.abs(DeviationEstimator.ymaxAt(calSN)-ymaxatData) <= closeEnough){ // add this constraint.
                    double estimateSD = DeviationEstimator.estimateDeviation(residualData, calSN,
                            start, end);
                   // curClosest = Math.abs(DeviationEstimator.ymaxAt(calSN)-ymaxatData);
                    if (estimateSD <= curMin){
                        curMin = estimateSD;
                        omegaCurMin = x;
                        xiCurMin = y;
                        alphaCurMin = z;
                    }  
                } 
            }
        }   
    }

    omega = omegaCurMin;
    xi = xiCurMin;
    alpha = alphaCurMin;
    fscale = residualYmax/gzDataMax;
            
    //calculateSN(residualYmax);

    // should not calc those here. 2013-02-22 XXXX
 //   mean = SNGenerator.computeMean(omega, xi, alpha);
 //   variance = SNGenerator.computeVariance(omega, xi, alpha);
 //   skewness = SNGenerator.computeSkewness(omega, xi, alpha);
    //System.out.println("omega = " + omega + " xi = " + xi + " alpha = " + alpha);
    return curMin;
}
  
 /*
   * 2011-09-30
   * singleAutoEstimate will try to get the best fit curve.
   * return: the errors
   */
private double[] SNEstimate(String sampleName, double [] curData ){
    boolean satisfied = false;
    double curError;
    double prevError=100000000;
    
    setDefaultParameters();
    int level = 0; // 5 level should be precise enough
    
    double curYmax = getMinMax(curData, MAX);
    
    while (!satisfied){        
        curError = oneLevelEstimate(curData);
        if (java.lang.Math.abs(curError - prevError) > (1 - AppConstant.SN_CONFIDENCE)*100 &&
                level < AppConstant.DECIMAL_PLACE){
            level ++;
            if (prevError > curError){
                prevError = curError;

                omega_min = omega - 2*omega_increment;
                if (omega_min < 0)
                    omega_min = 0.00001;

                omega_max = omega + 2*omega_increment;
                if (omega_max > AppConstant.OMEGA_MAX)
                    omega_max = AppConstant.OMEGA_MAX;

                xi_min = xi - 2*xi_increment;
                xi_max = xi + 2*xi_increment;
                if (xi_min < AppConstant.XI_MIN)
                    xi_min = AppConstant.XI_MIN;

                if (xi_max > AppConstant.XI_MAX)
                    xi_max = AppConstant.XI_MAX;

                alpha_min = alpha - 2*alpha_increment;
                alpha_max = alpha + 2*alpha_increment;
                if (alpha_min < AppConstant.ALPHA_MIN)
                    alpha_min = AppConstant.ALPHA_MIN;

                if (alpha_max > AppConstant.ALPHA_MAX)
                    alpha_max = AppConstant.ALPHA_MAX;

                omega_increment /= 10.0;
                xi_increment /= 10.0;
                alpha_increment /= 10.0;
            }
        } else {
            satisfied = true;
        }
    }
 
    double [] calSN = calculateSN(curYmax);
    
    return (DeviationEstimator.getResidue(curData, calSN));
  }
  
  
public List<SNParameters> nmodeSNEstimate(String sampleName, double [] gzData){
        List<SNParameters> snps = new ArrayList<SNParameters>();
        System.out.println("enter nmodeSNEstimate");
        
        boolean satisfied = false;
        double dataThreshold = AppConstant.RES_DATA_VOL_CUTOFF;
          
        double lastOmega=0, lastXi=0, lastAlpha = 0;
        snps.clear();
        
        double [] resData = gzData;
        gzDataMax = getMinMax(gzData, MAX);
        
        while (!satisfied){
            double [] nextResidualData = SNEstimate(sampleName, resData);
         
            addSNP(sampleName, snps);
            // printSnps(snps);
            //printData(nextResidualData);
            
            if (getMinMax(nextResidualData, MAX)/gzDataMax > dataThreshold ){
                if (Math.abs(lastOmega - omega) < AppConstant.SN_PARAMETER_ERROR  &&
                        Math.abs(lastXi - xi) < AppConstant.SN_PARAMETER_ERROR &&
                        Math.abs(lastAlpha - alpha) < AppConstant.SN_PARAMETER_ERROR){
                    satisfied = true;
                } else {
                    setDefaultParameters();
                    resData = nextResidualData;
                    lastOmega = omega;
                    lastXi = xi;
                    lastAlpha = alpha;
                }
                
                //satisfied = true; // temp
            } else {
                satisfied = true;
            }
        }
        
    return snps;
}

public void nmodeSNRefine(String sampleName, double gzData[], List<SNParameters> snps, int adjSN){
        // fit SN residual from dominant SN.
    if (snps.isEmpty() ){            
         calculateSN(getMinMax(gzData, MAX));
         addSNP(sampleName, snps);
    } else {    
        double [] resData = gzData;
        SNParameters snp;
        double [] cumSN = new double[AppConstant.dataPoint.length];

        System.out.println("nmodeSNRefine: ");
        this.printSnps(snps);

        for (int j=0; j< adjSN; j++) {
            snp = snps.get(j);
            double [] curSN = SNGenerator.generateSN(snp);
            double max=0;
            for (int i=0; i<AppConstant.dataPoint.length; i++){
               if (max < curSN[i])
                   max = curSN[i];
               System.out.println(" gzdata: " + i + gzData[i]);
               cumSN[i] += curSN[i]; 
            }
        }

        resData = DeviationEstimator.getResidue(gzData, cumSN);

        double max = 0;
         for (int i=0; i<AppConstant.dataPoint.length; i++){
           if (max < resData[i])
               max = resData[i];   
         }
         System.out.println("res max " + max);
 
 
        boolean satisfied = false;
        double threshold = 0.07;  
        double lastOmega =0;
        double lastXi = 0;
        double lastAlpha = 0;

        while (!satisfied){
            double [] nextResidualData = SNEstimate(sampleName, resData);

            	//2015-12-19
            double curTot = 0;
            double sn[] = SNGenerator.generateSN(omega, xi, alpha);
        	for (double a: sn) {
               curTot += a *fscale;
            }
            
        	boolean done = true;
        	if (curTot > threshold) {
        		 addSNP(sampleName, snps);
        		done = false;
        	}
     
            double resTot = 0;
            for (double a: nextResidualData)
            	resTot += a;
            
            double resMax = getMinMax(nextResidualData, MAX);
          //if (y_max/gzData_ymax > threshold ){
            if ( resTot > threshold && !done ){
                if (Math.abs(lastOmega - omega) < 0.0001 &&
                        Math.abs(lastXi - xi) < 0.0001 &&
                        Math.abs(lastAlpha - alpha ) < 0.0001){
                    satisfied = true;
                } else {
                    setDefaultParameters();
                    resData = nextResidualData;
                    lastOmega = omega;
                    lastXi = xi;
                    lastAlpha = alpha;
                }
            } else {
                satisfied = true;
            }
        } // while
    }
 
// repaint();
        
}
                
public void adjustDominantSN(int whichP, int adjSN, List<SNParameters> snps){        
    if (snps.isEmpty()){
        if (debug)
            System.err.println("snps is empty");

         calculateSN(gzDataMax);
         SNParameters snp = new SNParameters();
         snp.setAlpha(alpha);
         snp.setOmega(omega);
         snp.setXi(xi);
         snp.setfDataSN(1);
         snp.setOmega_increment(omega_increment);
         snp.setXi_increment(xi_increment);
         snp.setAlpha_increment(alpha_increment);

         snps.add(snp);
    } else {
        SNParameters snp = new SNParameters();

        snp = snps.get(adjSN-1);

        if (whichP == AppConstant.OMEGA)
            snp.setOmega(omega);

        if (whichP == AppConstant.XI)
            snp.setXi(xi);

        if (whichP == AppConstant.ALPHA)
            snp.setAlpha(alpha);

        omega = snp.getOmega();
        xi = snp.getXi();
        alpha = snp.getAlpha();

        double [] calSN = SNGenerator.generateSN(snp.getOmega(), snp.getXi(), snp.getAlpha());

        snp.setfDataSN(snp.getDataMax()/ getMinMax(calSN, MAX));
        int s = snps.size()-1;
        while (s >= adjSN -1) {
            snps.remove(s--);  // remove all above the current adjusted.
        }

        snps.add(snp);
    }
}
    
public void printSnps(java.util.List<SNParameters> asnps){
    Iterator itrSN = asnps.iterator();

    int i=0;
    while (itrSN.hasNext()) {
        SNParameters snp = (SNParameters)itrSN.next();
        System.out.println((i++) + "th sample name: " + snp.getSampleName() + " omega " + snp.getOmega()
                + " xi " + snp.getXi() + " alpha " + snp.getAlpha() + " fdataSN " + snp.getfDataSN()
                + " dataMax " + snp.getDataMax());
    }

}
private void setRunParameters(SNParameters snp){
omega = snp.getOmega();
xi = snp.getXi();
alpha = snp.getAlpha();
omega_increment = snp.getOmega_increment();
xi_increment = snp.getXi_increment();
alpha_increment = snp.getAlpha_increment();

omega_min = omega - 2*omega_increment;
if (omega_min < 0)
    omega_min = 0.0001;

omega_max = omega + 2*omega_increment;

xi_min = xi - 2*xi_increment;
xi_max = xi + 2*xi_increment;

alpha_min = alpha - 2*alpha_increment;
alpha_max = alpha + 2*alpha_increment;

omega_increment /= 10.0;
xi_increment /= 10.0;
alpha_increment /= 10.0;      
}

private void addSNP(String sampleName, List<SNParameters> snps){
      SNParameters snp = new SNParameters();
      snp.setSampleName(sampleName);
      snp.setOmega(omega);
      snp.setXi(xi);
      snp.setAlpha(alpha);
      snp.setfDataSN(fscale);
      System.out.println("in AddSNP: fscale is the fDataSN " + snp.getfDataSN());
      snp.setAlpha_increment(alpha_increment);
      snp.setOmega_increment(omega_increment);
      snp.setXi_increment(xi_increment); 
      snp.setDataMax(gzDataMax);
      snps.add(snp);  
  }
  
private double [] getNmodeSN(java.util.List<SNParameters> sns){
  double[] nmodeSN = new double[AppConstant.dataPoint.length];
          
  for (int i=0; i<AppConstant.dataPoint.length; i++){
      nmodeSN[i] = 0;
  }

  Iterator sn = sns.iterator();
  while(sn.hasNext()){
      SNParameters snp = (SNParameters)sn.next();
      if (debug)
        System.out.println(" omega " + snp.getOmega() + " xi " + snp.getXi() + " alpha " + snp.getAlpha() + " fdz " + snp.getfDataSN());

      double [] singleSN = SNGenerator.generateSN(snp);

      for (int i=0; i<AppConstant.dataPoint.length; i++){          
          nmodeSN[i] += singleSN[i];                   
      }

    if (debug)
      System.out.print("\n");
  }
  
  return nmodeSN;
}

private double [] calculateSN(double residualYmax){       
        double [] calSN = SNGenerator.generateSN(omega, xi, alpha);
            
        if (getMinMax(calSN, MAX) == 0)
            fscale = 0;
        else
            fscale = residualYmax / gzDataMax;
        
        for (int i=0; i<AppConstant.dataPoint.length; i++){
            calSN[i] *= fscale;
        }
        
        return calSN;
    }
    
private double [] getGZData(String [][] allGZStrData, int rowIndex) throws Exception{
        String strGZData[] = allGZStrData[rowIndex]; // this is an example

        double [] gzData = new double[strGZData.length-1]; // do not use the sample name

        for (int i=0; i< gzData.length; i++){
            gzData[i] = Double.valueOf(strGZData[i+1]);
        } 
        
        return gzData;
    }
    
static double getMinMax(double [] xydata, int mm){
    double max = xydata[0];
    double min = xydata[0];

    for (int i=1; i<xydata.length; i++){
        //System.out.println("xydata[" + i + "] = " + xydata[i]);
        if (xydata[i] > max)
            max = xydata[i];
        else {
            if (xydata[i] < min)
                min = xydata[i];
        }
    }

    if (mm == MIN) 
        return min;
    else
        return max;
}

private void setDefaultParameters(){
      omega_min = AppConstant.OMEGA_MIN;
      if (omega_min == 0)
          omega_min = 0.1;

      omega_max = AppConstant.OMEGA_MAX;
      omega_increment = AppConstant.OMEGA_INCREMENT;

      xi_min = AppConstant.XI_MIN;
      xi_max = AppConstant.XI_MAX;
      xi_increment = AppConstant.XI_INCREMENT;

      alpha_min = AppConstant.ALPHA_MIN;
      alpha_max = AppConstant.ALPHA_MAX;
      alpha_increment = AppConstant.ALPHA_INCREMENT;
}
    
public void printData(double [] adata){
    int i=0;
    for (double a: adata)
        System.out.println(" data[i" + (i++) + "] :" + a);
}

public List<SNParameters> retrieveAndCalSnps(String allGZStrData[][], int rowData, List<java.util.List<SNParameters>> listSnps){
    String sampleName = allGZStrData[rowData][0];
// XXXX   this.replaySN = true;

    // find the first of this sample
    int i = rowData;
    while (allGZStrData[i][0].equalsIgnoreCase(sampleName)) {
        i--;
    }


    int sampleSeq = rowData - i; 

    sampleName = sampleName.substring(6).toLowerCase() + "-" + sampleSeq; // remove "BOS5C "
    //String snSampleName = sampleName + "-" + (rowData - i + 1);

    Iterator itr = listSnps.iterator();
    boolean notFound = true;
    boolean foundSN = false;
    java.util.List<SNParameters> sns = new ArrayList<SNParameters>();
    while (notFound && itr.hasNext()){
        sns = (java.util.List<SNParameters>)itr.next();
        Iterator it = sns.iterator();

        if (it.hasNext()){
            String snSampleName = ((SNParameters)it.next()).getSampleName().toLowerCase();

            if (snSampleName.equalsIgnoreCase(sampleName)){
                notFound = false;
                foundSN = true;                           
            }
        }
    }
    
    return sns;
}

}
