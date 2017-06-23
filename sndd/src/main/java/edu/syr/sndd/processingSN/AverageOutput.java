/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.processingSN;

import edu.syr.sndd.common.util.AppConstant;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stoney Q. Gan
 */
public class AverageOutput {
     private static List<Integer> maxSizeSN = new ArrayList<Integer>();
     private static DecimalFormat df = new DecimalFormat("#.####");
     
     /**
      * sort samples by their sample name in ascending order.
      * @param unsortSamples
      * @return
      */
     private static List<List> sortSamples(List<List> unsortSamples){        
         for (int i=0; i< unsortSamples.size(); i++){
             String tsn = unsortSamples.get(i).get(0).toString().split("\t")[0];
             int iH = tsn.toUpperCase().indexOf("H");
             int minCore = Integer.parseInt(tsn.substring(6, iH));
             int iDash = tsn.indexOf("-");
             int iss = tsn.indexOf(" ", iH);
             int minSection = Integer.parseInt(tsn.substring(iDash + 1, iss));
             
             int iCM = tsn.toUpperCase().indexOf("CM");
             String strS = tsn.substring(iss+1, iCM);
             int minDepth;
             if (strS.toLowerCase().contains("cc")) 
                 minDepth = 152;
             else  
                minDepth = Integer.parseInt(tsn.substring(iss+1, iCM));
             
             for (int j=i; j<unsortSamples.size(); j++){
                 String sampleName = unsortSamples.get(j).get(0).toString().split("\t")[0];
                 int idxH = sampleName.toUpperCase().indexOf("H");
                 int core = Integer.parseInt(sampleName.substring(6, idxH));
                 int idxDash = sampleName.indexOf("-");
                 int secondSpace = sampleName.indexOf(" ", idxH);
                 int section = Integer.parseInt(sampleName.substring(idxDash+1, secondSpace));
                 int idxCM = sampleName.toUpperCase().indexOf("CM");
                 String strSec = sampleName.substring(secondSpace+1, idxCM);
                 int depth;
                 if (strSec.toLowerCase().contains("cc")) 
                     depth = 152;
                 else  
                    depth = Integer.parseInt(sampleName.substring(secondSpace+1, idxCM));

                 if (minCore > core || (minCore == core && minSection > section) ||
                         (minCore == core && minSection == section && minDepth > depth)){
                     minCore = core;
                     minSection = section;
                     minDepth = depth;
                     List<String> tmp = unsortSamples.get(i);
                     unsortSamples.set(i, unsortSamples.get(j));
                     unsortSamples.set(j, tmp);
                     int itmp = maxSizeSN.get(i);
                     maxSizeSN.set(i, maxSizeSN.get(j));
                     maxSizeSN.set(j, itmp);
                 } 
             }
         }
         return unsortSamples;
     }
     
     /**
      * This class is to sort all samples in ascending order by their sample names, and sort SN's within a sample by their volume percents. Thus, the samples will
      * be in ascending order as their depth.
      * @param argv
      * @throws FileNotFoundException
      * @throws IOException
      */
     public static void  main(String[] argv) throws FileNotFoundException, IOException{

        BufferedReader br = new BufferedReader(new FileReader(AppConstant.OUTPUT_FILENAME + "-stdz"));
        String line;

        List<List> origSamples = new ArrayList<List>();
            
        String prevSampleName = "";
        int maxSize = 0 ;
        int maxAll = 0;
        List<String> listSample = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
           String [] sampleData = line.split("\t");        
           if (prevSampleName.equals(sampleData[0])){
               listSample.add(line);
               if (maxSize < Integer.parseInt(sampleData[2])) {
                   maxSize = Integer.parseInt(sampleData[2]);
               }
           } else {
               if (! listSample.isEmpty()){
                    origSamples.add(listSample);
                    maxSizeSN.add(maxSize);
                    listSample = new ArrayList<String>();
                    if (maxAll < maxSize)
                        maxAll = maxSize;

               }
               prevSampleName = sampleData[0];
               listSample.add(line);
               maxSize = Integer.parseInt(sampleData[2]);
           }
        }
        br.close();
        
        List<List> allSample = sortSamples(origSamples);
        
        PrintStream out = new PrintStream (new FileOutputStream(AppConstant.OUTPUT_FILENAME + "-stdz-sorted"));
        out.print("sampleID\tnumber of SNs");
        for (int ii = 0; ii< maxAll; ii++)
            out.print("\tomega\txi\talpha\tmean\tvariance\tskewness\tfdataSN\tdataMax\tvol_percent");
        out.println();
      
        for (int i=0; i<allSample.size(); i++){
            List<String> listS = allSample.get(i); 
            int maxSN = maxSizeSN.get(i);
            double dataTotal[][] = new double[maxSN][9];
            for (int j=0; j<maxSN; j++) {
                for (int k=0; k< 9; k++){
                    dataTotal[j][k] = 0.0;
                }
            }
            
            // calculate totals
            String sampleName="";
            for ( String strS: listS){
//                System.out.println("this is strS: " + strS);
                String [] sampleData = strS.split("\t");
                sampleName = sampleData[0];
                int snN = Integer.parseInt(sampleData[2]);
                for (int j=0; j<snN; j++) {
                    for (int k=0; k < 9; k++) {
                         dataTotal[j][k] += Double.parseDouble(sampleData[3 + j*9 + k]);
                    }
                }
            }
            // sort by volume within a sample
            for ( String strS: listS){
                String [] sampleData = strS.split("\t");
                sampleName = sampleData[0];
                int snN = Integer.parseInt(sampleData[2]);
                for (int j=0; j<snN; j++) {
                    double bigV = dataTotal[j][8];
                    int bigVI = j;
                    for (int k = j; k < snN; k++) {
                        if (dataTotal[k][8] > bigV ) {
                            bigV = dataTotal[j][8];
                            bigVI = k;
                        }
                    }
                    // swap
                    for (int k = 0; k< 9; k++) {
                        double tmp = dataTotal[j][k];
                        dataTotal[j][k] = dataTotal[bigVI][k];
                        dataTotal[bigVI][k] = tmp;
                    }
                }
            }
                        
            // write out results
            out.print(sampleName + "\t" + maxSN);
            for (int j=0; j<maxSN; j++) {
                for (int k=0; k < 9; k++) {
                     out.print("\t" + df.format(dataTotal[j][k]/listS.size()));
                }
            }
            out.println();
        }
    }
}
