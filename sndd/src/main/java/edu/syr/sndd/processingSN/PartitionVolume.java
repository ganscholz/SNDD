/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.processingSN;

import edu.syr.sndd.common.util.AppConstant;
import edu.syr.sndd.common.util.ExcelUtil;
import edu.syr.sndd.fitSN.DeviationEstimator;
import edu.syr.sndd.fitSN.SNParameters;
import edu.syr.sndd.fitSN.SNGenerator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.syr.sndd.readData.ReadGSData;
/**
 *
 * @author Stoney Q. Gan
 */
public class PartitionVolume {
   //private static final String SNFileName = "C:\\Users\\eximlean\\Desktop\\chapter3\\grainSize\\SN results validated minus bottom cores.xlsx";
   private static final String OriginalGSFileName = edu.syr.sndd.common.util.AppConstant.INPUT_FILENAME;
  // private static final String CleanedGSFileName = "C:\\Users\\eximlean\\Desktop\\chapter3\\grainSize\\bosumtwi-clean-sns.xlsx";
   protected String [][] SNData;
   protected String [][] GSData;
   protected double [][] GSNData; 
   protected List<List<edu.syr.sndd.fitSN.SNParameters>> listSnps;
   public static final int IOmega = 6; // column index of omega 1
   public static final int IPSN = 6; // how many column for each sn
   public static final int NSN = 4;  // number of SN's
   public static final int SAMPLE_NAME_COL = 2; // col contains sample name in SN file.
   public static final double PRACTICAL_ZERO = 0.0001; // PRACTICAL ZERO;
   
   public static final double SN_THRESHOLD = 0.05; // 3%
   
   /**
    *  find row number in the GS data file.
    * @param sampleName the sample to be found
    * @return the row number in the GS data file.
    */
   public int findRowNumber(String sampleName) {
       String sampleNameStem = sampleName;
       int    sampleSeq = 2;
       
       if (sampleName.charAt(sampleName.length()-2) == '-'){
           sampleSeq = Character.getNumericValue(sampleName.charAt(sampleName.length() - 1));
           sampleNameStem = sampleName.substring(0, sampleName.length()-2);
       }
       
       int i = 1, j = 0;
       boolean notFound = true;
       boolean sampleFound = false;
       int sampleRow=0; // to handle only one sample with unmatched sequence
       while (notFound && i < GSData.length){
           String gsSampleName = GSData[i][0].toLowerCase().substring(6);
           if (gsSampleName.equalsIgnoreCase(sampleNameStem.toLowerCase())){
               j++;
               sampleFound = true;
               sampleRow = i;
               if (j == sampleSeq){
                   notFound = false;
               }    
           }
           
           i++;
       }
       
       if (notFound){
           /*
           if (sampleFound){
               System.out.println("wrong sequence");
               return sampleRow;
           }
            */
           
           System.out.println("did not find this sample: " + sampleName);
           return -1;
       }
       
       return i-1;
   }
  
   /**
    * Since each data point of SN is percent already, we could simply sum up to the volume percent of each SN.
    * @param snData volume percent of the SN
    * @param gsData	original data.
    * @return volume percent of snData.
    */
   public double calVolPercent(double [] snData, double [] gsData){
       double volSum = 0; 
       double volData = 0.0;
       DecimalFormat df = new DecimalFormat("#.##");
       for (int i=0; i<AppConstant.dataPoint.length; i++){
           volSum += (snData[i]);
           volData += gsData[i];
       }
       System.out.println("cal vol percent: " + df.format(volSum * 100/volData));
       return (volSum * 100/volData);
   }
   
   /**
    * Calculate the volume percent of an SN in a sample.
    * @param asnp a set of parameters defining an SN.
    * @return volume percent of asnp.
    */
   private double calSNPercent(SNParameters asnp){
       double [] sndata = SNGenerator.generateSN(asnp);
       int rowData = findRowNumber(asnp.getSampleName());

       double snTotal = 0;
       double dataTotal = 0;

       for (int i=0; i<AppConstant.dataPoint.length; i++){
           snTotal += sndata[i];
           dataTotal += GSNData[rowData][i];
       }

       System.out.println(" dataTotal should be 100 " + dataTotal + " sn total: " + snTotal);
       return (100* snTotal/dataTotal);
   }
   
   /**
    * This method gets a volume percent of the fine grain portion of a GSD specified by asnp using the size cut off.
    * @param asnp a set of SN parameters.
    * @param sizeCutoff the value separating fine grain portion.
    * @return volume percent of fine grain portion.
    */
   private double calFineGrainPercent(SNParameters asnp, double sizeCutoff){
       double [] sndata = SNGenerator.generateSN(asnp);
       int rowData = findRowNumber(asnp.getSampleName());
       
       double max = -10;
       int peak=0;
       for (int i=0; i<AppConstant.dataPoint.length; i++){
           if (sndata[i] < max){
               peak = i;
               max = sndata[i];
           }
       }
       
       // make left wing = right wing
       if (asnp.getAlpha() < 0){
           int k=1;
           while (peak+k < AppConstant.dataPoint.length && 
                    sndata[peak + k] > 0){
               if (peak - k >= 0)
                sndata[peak-k] = sndata[peak+k];
               
               k++;
           }
       }
 
       double snTotal = 0;
       int i=0;
       while (AppConstant.dataPoint[i] < sizeCutoff){
           if (GSNData[rowData][i] - sndata[i] > 0)
                snTotal += (GSNData[rowData][i] - sndata[i]);
           
           i++;
       }
       
       double gstotal = 0.0;
       for (i=0; i<AppConstant.dataPoint.length; i++){
           gstotal += GSNData[rowData][i];
           
       }
       double snPercent = 100*snTotal/gstotal;
       return snPercent;
   }
   
   /**
    * read GSD data from the input file and stored them in the GSNdata which is a two dimensional array of double.
    * @param inputfile
    * @throws Exception
    */
   public void readAndConvertData(String inputfile) throws Exception{
       this.GSData = ReadGSData.readInputData(inputfile);
           
       double [][] GSNData0 = new double[GSData.length][AppConstant.dataPoint.length];
       
       for (int i=0; i< GSData.length; i++){
           for (int j=0; j<AppConstant.dataPoint.length; j++){
               GSNData0[i][j] = Double.valueOf(GSData[i][j+1]);
           }
       }
       
       GSNData = GSNData0;
   }
 
   /**
    * Get the maximum value of an array.
    * @param indata double array to be processed.
    * @return max of indata.
    */
   public double getMax(double [] indata){
       double max = -1000000000.0;
       for (int i=0; i<indata.length; i++){
           if (indata[i] > max)
               max = indata[i];
       }
       
       return max;
   }
   
   /**
    * A rough implementation of binary tree search.
    * @param gap
    * @param beginP
    * @param endP
    * @param precision
    * @param asnp
    * @return
    */
   private double bsearch(double gap, int beginP, int endP, double precision, SNParameters asnp){
       double beginX = AppConstant.dataPoint[beginP];
       double endX = AppConstant.dataPoint[endP];
       double rightX = (beginX + endX)/2.0;
       double tryX = SNGenerator.SNpdf(rightX, asnp.getOmega(), asnp.getXi(), asnp.getAlpha()) * asnp.getfDataSN();
       while (Math.abs(tryX - gap) > precision
               && (endX - beginX) > precision){
           if (tryX - gap > 0){ // tryX too big
               endX = (beginX + endX)/2.0;
           } else {
               beginX = (beginX + endX)/2.0;
           }
           rightX = (beginX + endX)/2.0;
           
           tryX = SNGenerator.SNpdf(rightX, asnp.getOmega(), asnp.getXi(), asnp.getAlpha())*asnp.getfDataSN();
       }
       
       return rightX;
   }
   
   /**
    * calculate the median of SN.
    * @param snData SN data
    * @param asnp SN parameters
    * @return the median.
    */
   private double calMedian(double [] snData, SNParameters asnp){
       double m =0;
       double CDV = 0.0;
       double CDT = 0.0;
       for (int i=0; i<AppConstant.dataPoint.length; i++){
           CDT += snData[i];
       }
       
       int i = 0;
       while (i<AppConstant.dataPoint.length && CDV < CDT/2){
           CDV += snData[i++];
       }
       
       double gap = CDT/2 - CDV;
       double precision = 0.001;
       
       if (gap < 0 ){ // go to far, 2012-02-05 need to have a better loop
           i--;
       }
       
       m = bsearch(gap, i-1, i, precision, asnp);
       
       return m;
   }
   
   private void calMedians(){
    Iterator listIterator = listSnps.iterator();
    int i=0;
    while (listIterator.hasNext()){
        Iterator snpIterator = ((List<edu.syr.sndd.fitSN.SNParameters>)listIterator.next()).iterator();
        int j=0;
        double [] gsData=null;
        while (snpIterator.hasNext()){
            SNParameters snp = (SNParameters)snpIterator.next();
            double [] snData = SNGenerator.generateSN(snp.getOmega(), snp.getXi(), snp.getAlpha());
            
            if (snp.getSampleName().contains("10H-2 8CM-2")){
                System.out.println("stop to examine");
                System.out.println("this is median for row: " + i + " SN " + j +
                        " omega " + snp.getOmega() +
                        " xi: " + snp.getXi() +
                        " alpha: " + snp.getAlpha() +
                        " mean = " + SNData[i][j*IPSN + IOmega + 3] +
                        " median: ");
            }
            
            if (snp.getOmega() == 0){
                System.out.println("have a problem with this SN: ");
                System.out.println( i + " SN " + j +
                    " omega " + snp.getOmega() +
                    " xi: " + snp.getXi() +
                    " alpha: " + snp.getAlpha());
            } else {
                if (j== 0){
                    gsData = GSNData[findRowNumber(snp.getSampleName())];
                }

                double maxGS = getMax(gsData);
                double maxSN = getMax(snData);
                double f = maxGS/maxSN;
                for (int ii=0; ii<AppConstant.dataPoint.length; ii++){
                    snData[ii] *= f;
                }

                double m = this.calMedian(snData, snp);

                gsData = DeviationEstimator.getResidue(gsData, snData);
                /*
                System.out.println("this is median for row: " + i + " SN " + j +
                        " omega " + snp.getOmega() +
                        " xi: " + snp.getXi() +
                        " alpha: " + snp.getAlpha() +
                        " mean = " + SNData[i][j*IPSN + IOmega + 3] +
                        " median: " + m);
                 */
                
                if (j==0){
                    System.out.println(snp.getSampleName() + "\t" + m);
                }
            }
            j++;                
         }
        i++;
       }
    }
   
   /**
    * 
    */
   private void calDominantSNAndFineGrainPercent(){
       Iterator listIterator = listSnps.iterator();
        int i=0;
        while (listIterator.hasNext()){
            Iterator snpIterator = ((List<SNParameters>)listIterator.next()).iterator();
            if (snpIterator.hasNext()){
                SNParameters snp = (SNParameters)snpIterator.next();
                double snPercent = calSNPercent(snp);
                double fineGrainPercent = calFineGrainPercent(snp, AppConstant.FINE_GRAIN_CUTOFF);
                System.out.println("dominant SN\t" + snPercent + " fine grain percent\t" + fineGrainPercent);
            } 
            i++;
           }
   }
   
   /**
    * There is something missing in this class: the listSnps is not set. Need to read from the processed file containing all SNP's instead of the original GSD.
    * 2015-06-10.
    * @throws Exception
    */
   private void process() throws Exception{
       this.readAndConvertData(OriginalGSFileName);
       this.calMedians();
       this.calDominantSNAndFineGrainPercent();
   }
   public static void  main(String[] argv){
        try {
            PartitionVolume pv = new PartitionVolume();
            pv.process();
        } catch (Exception ex) {
            Logger.getLogger(PartitionVolume.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public String[][] getGSData() {
        return GSData;
    }

    public void setGSData(String[][] GSData) {
        this.GSData = GSData;
    }

    public String[][] getSNData() {
        return SNData;
    }

    public void setSNData(String[][] SNData) {
        this.SNData = SNData;
    }

    public List<List<SNParameters>> getListSnps() {
        return listSnps;
    }
}
