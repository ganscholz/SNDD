/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.graphResults;

import java.awt.*;
import java.awt.geom.*;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import edu.syr.sndd.common.util.AppConstant;
import edu.syr.sndd.fitSN.DeviationEstimator;
import edu.syr.sndd.fitSN.SNGenerator;
import edu.syr.sndd.fitSN.SNParameters;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import edu.syr.sndd.processingSN.PartitionVolume;
import edu.syr.sndd.readData.ReadGSData;
/**
 * This is the class to draw graph from the partitioned data.
 * @author Stoney Q. Gan
 */

public class GraphingData extends JPanel {
    //double[] calSN = new double[AppConstant.dataPoint.length];
    double[] nmodeSN = new double[AppConstant.dataPoint.length];

    protected double x_min, x_max, y_min, y_max;
    protected double gzData_xmin, gzData_xmax, gzData_ymin, gzData_ymax;
    protected double fscale;
    
    final int X = 1;
    final int Y = 2;
    
    final int PAD = 80;
    final String XLABEL = "Grain Size (\u03BCm)";  // \u03D5 scale
    final String YLABEL = "Vol(%)";
    
    protected double omega = 1; // scale parameter
    protected double xi = 1; // location parameter
    protected double alpha = 0.5; // shape parameter
    protected static double gzData[]; // an array of grain size data whose size is defined in the AppConstants.
    protected static String [][] allData;  // hold grain size data of all samples.
    protected int rowIndex = 1; // aka sample index
    public int numSamples; // the number of samples read.
    
    protected double omega_min = AppConstant.OMEGA_MIN;
    protected double omega_max = AppConstant.OMEGA_MAX;
    protected  double omega_increment = AppConstant.OMEGA_INCREMENT;
    protected  double xi_min = AppConstant.XI_MIN;
    protected  double xi_max = AppConstant.XI_MAX;
    protected  double xi_increment = AppConstant.XI_INCREMENT;
        
    protected  double alpha_min = AppConstant.ALPHA_MIN;
    protected  double alpha_max = AppConstant.ALPHA_MAX;
    protected  double alpha_increment = AppConstant.ALPHA_INCREMENT;
    protected  final double confident = 0.9999; // 1 - error difference
    protected final double confidLevel = 2; // * standard deviation roughly.
    protected final int decimalPlace = 5; // how many decimal point for parameter precision -- how many level 
    protected java.util.List<java.util.List<SNParameters>> listSnps; // a list of skew normal distribution parameters and their operation definition.
    
    protected java.util.List<SNParameters> snps;
    protected boolean debug = false;
    protected double mean; // statistic mean of an SN
    protected double variance; // statistic variance of an SN
    protected double skewness; // skewness of an SN
    protected String sampleName; 
    
    protected boolean noRepaint; // whether to repaint the canvas.
    protected boolean manualSN; // whether manually fits (adjust parameters) an SN
    protected int adjSN = 0; // index of the SN to be manually adjusted.
    protected edu.syr.sndd. processingSN.PartitionVolume pv = new edu.syr.sndd.processingSN.PartitionVolume();
    protected boolean foundSN = true;
    protected boolean replaySN = true;
    protected java.util.List<SNParameters> SNsnps;
    
    /**
     * This method will read GSD data from the input file and 
     */
    public  GraphingData(){
        String fileName = AppConstant.INPUT_FILENAME;
        snps = new java.util.ArrayList<SNParameters>();
        listSnps = new java.util.ArrayList<java.util.List<edu.syr.sndd.fitSN.SNParameters>>();
        try {
            pv.readAndConvertData(fileName); // read the data to a double [][]
            allData = pv.getGSData(); // allData is a String[][];
     
            numSamples = allData.length - 1;
            getGZData(1);
        } catch (Exception ex) {
            Logger.getLogger(GraphingData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Simply repaint the canvas.
     */
    protected void myRepaint(){
        repaint();
    }
    
    /**
     * This is the method to draw the final result of partitioned sample including original GSD, individual components,
     * aggregation of components and residual GSD (error). It actually draws the graphics from SN's, i. e., it calculates
     * individual SN's from the SN parameters.
     */
    protected void paintComponent(Graphics g) {
        if (debug)
            System.out.println("paintComponent");                
        java.util.List<SNParameters> drawSnps;
        
        if (replaySN){
            if (foundSN){
                drawSnps = this.SNsnps;
                //snps = this.SNsnps;
            } else {
                drawSnps = null;
            }
        } else {
            drawSnps = snps;
        }
        
        try {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
             
            double xInc = (double)(w - 2*PAD)/(x_max-x_min);
            double scale = (double)(h - 2*PAD)/(gzData_ymax- gzData_ymin);
            
            // draw indiviual calSN
            g2.setPaint(Color.GREEN);
            double lastX=0, lastY=0;
            //this.getSnps(rowIndex);
            if (drawSnps == null){
                System.out.println("nothing to paint: snps is empty ...");
                /*
                double [] calSN = calculateSN(gzData_ymax);
                for(int i = 0; i < AppConstant.dataPoint.length; i++) {
                        double x = PAD + (AppConstant.dataPoint[i]-x_min)*xInc;
                        double y = h - PAD - scale*calSN[i];
                        g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
                        if (i > 0){
                            g2.draw(new Line2D.Double(lastX, lastY, x, y));

                        } 

                        lastX = x;
                        lastY = y;
                    }
                 */
            } else {
                Iterator isn = drawSnps.iterator();

                while (isn.hasNext()){
                    SNParameters sn = (SNParameters) isn.next();
                    
                    double [] singleSN = SNGenerator.generateSN(sn);
                    
                    if (debug){
                        System.out.println("draw an sn scale = " + scale);
                        System.out.println(" omage = " + sn.getOmega() + " xi " + sn.getXi()
                                + " alpha " + sn.getAlpha());
                    }
                    
                    for(int i = 0; i < singleSN.length; i++) {
                        double x = PAD + (AppConstant.dataPoint[i]-x_min)*xInc;
                        double y = h - PAD - scale*singleSN[i];
                        g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
                        if (i > 0){
                            g2.draw(new Line2D.Double(lastX, lastY, x, y));

                        } 

                        lastX = x;
                        lastY = y;
                    }
                }
                                         
            // Draw generated nmodeSN
                Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
                g2.setPaint(Color.RED);
                g2.setStroke(dashed);
                lastX=0; lastY=0;
                getNmodeSN(drawSnps);
                for(int i = 0; i < nmodeSN.length; i++) {
                    double x = PAD + (AppConstant.dataPoint[i]-x_min)*xInc;
                    double y = h - PAD - scale*nmodeSN[i];
                    g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
                    if (i > 0){
                        g2.draw(new Line2D.Double(lastX, lastY, x, y));

                    } 

                    lastX = x;
                    lastY = y;

                }
            
                // draw error curve
                g2.setPaint(Color.MAGENTA);
                g2.setStroke(new BasicStroke(1));
                for(int i = 0; i < AppConstant.dataPoint.length; i++) {
                    double x = PAD + (AppConstant.dataPoint[i]-x_min)*xInc;

                    double y = h - PAD - scale*(gzData[i]-nmodeSN[i]);
                    g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
                    if (i > 0){
                        g2.draw(new Line2D.Double(lastX, lastY, x, y));

                    } 

                    lastX = x;
                    lastY = y;     
                }
            }
            
                     // draw gz data
            g2.setPaint(Color.BLACK);
            for(int i = 0; i < AppConstant.dataPoint.length; i++) {
                double x = PAD + (AppConstant.dataPoint[i]-x_min)*xInc;
                
                double y = h - PAD - scale*gzData[i];
                g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
                if (i > 0){
                    g2.draw(new Line2D.Double(lastX, lastY, x, y));
                    
                } 
                
                lastX = x;
                lastY = y;     
            }
            
                    // Draw x_axis.
            g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
            
            double range = (double)(x_max - x_min);
            double fx = range/AppConstant.NUM_TICS;
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            for (int i=0; i<AppConstant.NUM_TICS+1; i++){
                g2.draw(new Line2D.Double((w-2*PAD)*i/AppConstant.NUM_TICS+PAD, h-PAD, (w-2*PAD)*i/AppConstant.NUM_TICS+PAD, h-PAD-5)); 
                String ticLabel = String.valueOf((double)((int)(Math.pow(2, i*fx+ x_min) + 0.5)*10/10));
                g2.drawString(ticLabel, (w-2*PAD)*i/AppConstant.NUM_TICS+PAD, h-PAD+15); 
            }
            
            g2.drawString(XLABEL, w/2-40, h-PAD+35);
            
            // Draw y_axis.
             g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
            
            double yRange = (double)(gzData_ymax-gzData_ymin);
            double fy = yRange/AppConstant.NUM_TICS;

            for (int i=0; i<AppConstant.NUM_TICS+1; i++){
                g2.draw(new Line2D.Double(PAD, h - ((h-2*PAD)*i/AppConstant.NUM_TICS+PAD), 
                                          PAD+5, h-((h-2*PAD)*i/AppConstant.NUM_TICS+PAD))); 
                String ticLabel = String.valueOf((double)((int)(i*fy*100))/100.00);                
                g2.drawString(ticLabel,PAD-30, h-((h-2*PAD)*i/AppConstant.NUM_TICS+PAD)); 
            }
            
            g2.drawString(YLABEL, 10, h/2);
            
            /* transformation will not respond to re-scale of window 
            AffineTransform at = new AffineTransform();
            at.setToRotation(-(Math.PI/2.0), w/2.0, h/2.0);
            g2.setTransform(at);
             * 
             */
            
//            AffineTransform orig = g2.getTransform();          
//            g2.rotate(-Math.PI/2);
//            g2.drawString(YLABEL,  h/2, 100);
//            g2.setTransform(orig);
            
        } catch (Exception ex) {
            Logger.getLogger(GraphingData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
/**
 * Previous process summed up all samples as the first row in the grain size data. This method will standardize all samples using
 * this allAve row. 
 */
protected void standardizeGZData() throws Exception {         
    // open output files
    String outfileName =  AppConstant.OUTPUT_FILENAME + "-stdz";
    PrintStream out=null;
    try {
        out = new PrintStream(new FileOutputStream(outfileName));
    } catch (FileNotFoundException ex) {
        Logger.getLogger(GraphingData.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // print the headings
    for (String colT: AppConstant.COL_TITLE) {
        out.print(colT + "\t");
    }
    out.println();
    
    double [] allAveGZ = new double[AppConstant.dataPoint.length];

    try {
        getGZData(1);
        for (int j=0; j< gzData.length; j++){
             allAveGZ[j] = gzData[j];
        }
    } catch (Exception ex) {
        Logger.getLogger(GraphingData.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    for (int i=2; i< numSamples; i++){                    
       getGZData(i);
       out.print(allData[i][0] + "\t");
       for (int j=0; j< gzData.length; j++){
            gzData[j] -= allAveGZ[j];
            out.print(gzData[j] + "\t");
        }
       out.println();
    }
    out.close();
}

/**
 * This is the method to call nmodeEstimate() to partition a sample GSD to one or more SND's, and store the result as a list of skew normal distribution parameter sets.
 * @param stdz whether to standardize the sample with the profile average.
 */
protected void partitionSN(boolean stdz){
    try {
        DecimalFormat df = new DecimalFormat("#.####");
        
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
        
        // get average GSD
        if (stdz) {
            allAveGZ = new double[AppConstant.dataPoint.length];

            try {
                getGZData(1);
                for (int j=0; j< gzData.length; j++){
                     allAveGZ[j] = gzData[j];
                }
            } catch (Exception ex) {
                Logger.getLogger(GraphingData.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        
        int sample_seq = 1;
        int xx = 1;
        if (stdz)
            xx = 2;
        
        for (int i=xx; i< numSamples; i++){
            System.out.println("sample " + i);                   
            try {
                getGZData(i);
                
                if (stdz) {
                    for (int kk = 0; kk<gzData.length; kk++){
                        gzData[kk] -= (allAveGZ[kk] + gzData_xmin);
                    }
                    
                    getMinMax(gzData, Y);
                }
                
                // partition a single sample.
                nmodeSNEstimate();

                	// a set of SN parameter is added to the list.
                listSnps.add(snps); // 2012-09-22 they will show when go next/prev for refinement....
                Iterator itr = snps.iterator();
                if (allData[i][0].equalsIgnoreCase(prevSampleName)){
                    sample_seq ++;
                } else
                    sample_seq = 1;
                out.print(allData[i][0] + "\t" + sample_seq + "\t" +  snps.size());
                out2.print(allData[i][0]);

                while (itr.hasNext()) {
                    SNParameters nps = (SNParameters) itr.next();
                    nps.setSampleName(allData[i][0]); // need to set in plotPanel.snps.
                    double om = nps.getOmega();
                    double xi = nps.getXi();
                    double al = nps.getAlpha();
                    double mn = SNGenerator.computeMean(om, xi, al);
                    double va = SNGenerator.computeVariance(om, xi, al);
                    double sk = SNGenerator.computeSkewness(om, xi, al);
                    double volPC = pv.calVolPercent(SNGenerator.generateSN(nps), getGzData());
                    out.print("\t" + df.format(om) + "\t" + df.format(xi) + "\t" 
                            + df.format(al) + "\t" + df.format(mn) + "\t" + df.format(va) + "\t" + df.format(sk)
                            + "\t" + nps.getfDataSN()  + "\t" + nps.getDataMax() + "\t" + volPC);
                    out.flush();

                    out2.print("\t" + df.format(mn) + "\t" + df.format(volPC));
                }  
                out.println();
                out2.println();
            } catch (Exception ex) {
                Logger.getLogger(SNFit.class.getName()).log(Level.SEVERE, null, ex);
            }

            prevSampleName = allData[i][0];
        }
        out.close();
        out2.close();
    } catch (IOException ex) {
        Logger.getLogger(SNFit.class.getName()).log(Level.SEVERE, null, ex);
    } 
}
    /**
     *  Using the current set of parameter ranges and their increments to find the best fit between gzData and 
     * calculated sn.
     * @return min SD;
     */
    private double oneLevelEstimate(double [] residualData, double residualYmax){        
        double curMin = 10000000.0;
        double omegaCurMin=1;
        double xiCurMin=0;
        double alphaCurMin=0;
        
        if (debug)
        	System.out.println("OneLevelEstimate residual max: " + y_max);
        
        double [] calSN = new double[AppConstant.dataPoint.length];
 
        // this is necessary because we want to match individual SN instead of overall curve.
        int [] idx = DeviationEstimator.dataVarianceEstimate(residualData);
        int start = idx[0];
        int end = idx[1]; 
//        System.out.println(" start and end: " + start + " " + end);
        
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
        fscale = y_max/gzData_ymax; // not necessary
        
        //calculateSN(residualYmax);
        
        mean = SNGenerator.computeMean(omega, xi, alpha);
        variance = SNGenerator.computeVariance(omega, xi, alpha);
        skewness = SNGenerator.computeSkewness(omega, xi, alpha);
        //System.out.println("omega = " + omega + " xi = " + xi + " alpha = " + alpha);
        return curMin;
    }
  
 /*
   * This is the method to calculate a single best fit SN for the current data. It is actually to get the best dominant SN for the current GSD data. Notice that
   * the returned error curve is the residual data potentially for further processing if the calling method determines that the residual is above the threshold
   * set by the user.
   * return: the errors between the best fit SN and current data.
   */
  private double[] SNEstimate(double [] curData ){
      boolean satisfied = false;
      double curError;
      double prevError=100000000;
    
    setDefaultParameters();
    int level = 0; // 5 level should be precise enough
    getMinMax(curData, Y);
    double curYmax = y_max;
    
    while (!satisfied){        
        curError = oneLevelEstimate(curData, curYmax);
        if (java.lang.Math.abs(curError - prevError) > (1 - confident)*100 &&
                level < decimalPlace){
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
  
  /**
   * This is the method actually partition GSD to one or more SND. It recursively finds one level best fit SND and residual GSD until y_max/gzData_ymax < threshold.
   */
  public void nmodeSNEstimate(){
        boolean satisfied = false;
        double threshold = AppConstant.RES_DATA_VOL_CUTOFF;
        System.out.println("threshold: " + threshold);
        
        double lastOmega=0, lastXi=0, lastAlpha = 0;
        snps.clear();
        
        double [] resData = gzData;
      
        while (!satisfied){
            double [] nextResidualData = SNEstimate(resData);
            if (debug)
                System.out.println("after nmodeAuto Estimate: omega = " + omega + " xi " + xi + " alpha " + alpha + " fdz " + fscale);
         
            //2015-12-19
            double curTot = 0;
            double sn[] = SNGenerator.generateSN(omega, xi, alpha);
        	for (double a: sn) {
               curTot += a *fscale;
            }
            
        	boolean done = true;
        	if (curTot > threshold) {
        		addSNP();
        		done = false;
        	}
        		
        	
//            System.out.println("curSNdata: ");
//            printCurSNData(); // for Bruce 2015-01-02
            
            // printSnps(snps);
            System.out.println(" Next residual data");
            printData(nextResidualData);
            getMinMax(nextResidualData, Y);
            
            double resTot = 0;
            for (double a: nextResidualData)
            	resTot += a;
            
            System.out.println(" Y_MAX: " + y_max + " gzData_ymax " + gzData_ymax + " residual volume: " + resTot);
            
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
                
                //satisfied = true; // temp
            } else {
                satisfied = true;
                System.out.println("Residual data");
                printData(nextResidualData); // for Bruce 2015-01-02
            }
        }
        
        // getNmodeSN();
        repaint();
    }
  
  /**
   * This method is to response to the manual adjustment of the SN parameters using the parameter slides on the GUI, this is similar to nmodeSNEstimate. Notice that
   * this method assumes that the previous SN in the SN set were manually fitted already. 
   */
    public void nmodeSNRefine(){
        // fit SN residual from dominant SN.
        if (snps.isEmpty() ){
            if (debug)
                System.err.println("snps is empty");
            
             calculateSN(gzData_ymax);
             SNParameters snp = new SNParameters();
             snp.setAlpha(alpha);
             snp.setOmega(omega);
             snp.setXi(xi);
             snp.setfDataSN(gzData_ymax/y_max);
             snp.setOmega_increment(omega_increment);
             snp.setXi_increment(xi_increment);
             snp.setAlpha_increment(alpha_increment);  
             snps.add(snp);
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
            double threshold = AppConstant.RES_DATA_VOL_CUTOFF;  
            double lastOmega =0;
            double lastXi = 0;
            double lastAlpha = 0;
            
            while (!satisfied){
                double [] nextResidualData = SNEstimate(resData);

                //2015-12-19
                double curTot = 0;
                double sn[] = SNGenerator.generateSN(omega, xi, alpha);
            	for (double a: sn) {
                   curTot += a *fscale;
                }
                
            	boolean done = true;
            	if (curTot > threshold) {
            		 addSNP();

                     System.out.println("n refine: add one more SNP: ");
                     printSnps(snps);
                     
            		done = false;
            	}
 
                double resTot = 0;
                for (double a: nextResidualData)
                	resTot += a;
                
                System.out.println("n refine: Y_MAX: " + y_max + " gzData_ymax " + gzData_ymax + " residual volume: " + resTot);
                
                getMinMax(nextResidualData, Y);
                //if (y_max/gzData_ymax > threshold ){
                if ( resTot > threshold && !done ){
                    if (Math.abs(lastOmega - omega) < 0.00001 &&
                            Math.abs(lastXi - xi) < 0.00001 &&
                            Math.abs(lastAlpha - alpha ) < 0.00001){
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
 
       repaint();
        
    }
     
    /**
     * Adjust the dominant SN by parameter specified by whichP.
     * @param whichP one of scale, location and shape parameter.
     */
    public void adjustDominantSN(int whichP){        
        if (snps.isEmpty()){
            if (debug)
                System.err.println("snps is empty");
            
            calculateSN(gzData_ymax);
             SNParameters snp = new SNParameters();
             snp.setAlpha(alpha);
             snp.setOmega(omega);
             snp.setXi(xi);
             snp.setfDataSN(gzData_ymax/y_max);
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
            getMinMax(calSN, Y);   
            snp.setfDataSN(snp.getDataMax()/ y_max);

            int s = snps.size()-1;
            while (s >= adjSN -1) {
                snps.remove(s--);  // remove all above the current adjusted.
            }
           
            snps.add(snp);
            
            manualSN = true;
 
            //replaySN = false;

        }
        
        repaint();
    }
  
    /**
     * Set the parameters for the next literation including min, max and increment of each parameter, where min and max is the current value +- 2* increment value, 
     * and the next increment value to 1/10 of current one.
     * @param snp a set of sn parameters.
     */
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
  
  /**
   * add the current snp to a list of snps.
   */
  private void addSNP(){
      SNParameters snp = new SNParameters();
      snp.setSampleName(sampleName);
      snp.setOmega(omega);
      snp.setXi(xi);
      snp.setAlpha(alpha);
      snp.setfDataSN(fscale);
      System.out.println("graphData AddSNP: fscale is the fDataSN " + snp.getfDataSN());
      snp.setAlpha_increment(alpha_increment);
      snp.setOmega_increment(omega_increment);
      snp.setXi_increment(xi_increment); 
      snp.setDataMax(fscale*y_max);
      snps.add(snp);  
  }
  
  /**
   * Get the aggregation of a set of snd specified by their parameters. This will be the total of partitioned GSD data.
   * @param sns a list of SN parameters.
   */
  private void getNmodeSN(java.util.List<SNParameters> sns){
      if (debug)
        System.out.println("getNmodeSN snps size " + snps.size());
      
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
  }
     
  /**
   * calculate an SND from the current set of parameters.
   * @param residualYmax the current y max in residual GSD.
   * @return a SND as an array of double.
   */
    protected double [] calculateSN(double residualYmax){
        omega = this.getOmega();
        xi = this.getXi();
        alpha = this.getAlpha();
        
        double [] calSN = SNGenerator.generateSN(omega, xi, alpha);
        
        getMinMax(calSN, Y);
    
        if (y_max == 0)
            fscale = 0;
        else
            fscale = residualYmax / y_max;
        
        for (int i=0; i<AppConstant.dataPoint.length; i++){
            calSN[i] *= fscale;
        }
        
        return calSN;
    }
    
    /**
     * Get the sample[rowIndex] and store them in gzData.
     * @param rowIndex sample index.
     * @throws Exception
     */
    protected void getGZData(int rowIndex) throws Exception{
        String strGZData[] = allData[rowIndex]; // this is an example

        gzData = new double[strGZData.length-1]; // do not use the sample name

        for (int i=0; i< gzData.length; i++){
            gzData[i] = Double.valueOf(strGZData[i+1]);
        }

        getMinMax(AppConstant.dataPoint, X);
        getMinMax(gzData, Y);
        
        gzData_xmin = x_min;
        gzData_xmax = x_max;
        gzData_ymin = y_min;
        gzData_ymax = y_max;
        
        //snps.clear();       
    }
    
    /**
     * get a max value of an array. 
     * @param xydata input array to find minMax
     * @param xy indicate whether is x or y minmax.
     */
    protected void getMinMax(double [] xydata, int xy){
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
        
        if (xy == X) {
            x_min = min;
            x_max = max;
        } else {
            y_min = min;
            y_max = max;               
        }           
    }

    /**
     * util to print SN parameters.
     * @param asnps
     */
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
    
    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getXi() {
        return xi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    public double getOmega() {
        return omega;
    }

    public void setOmega(double omega) {
        this.omega = omega;
    }

    public double[] getGzData() {
        return gzData;
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
        for (double a: adata) {
            //System.out.println(" data[i" + (i++) + "] :" + a);
            System.out.print(a + "\t");
        }
        System.out.println();
    }
    
    /** 2015-01
     * 
     */
    public void printCurSNData() {
    	double sn[] = SNGenerator.generateSN(omega, xi, alpha);
    	for (double a: sn) {
           System.out.print(a *fscale + "\t");
        }
    	System.out.println();
    }
    
    protected void retrieveAndCalSnps(int rowData){
        String sampleName = this.allData[rowData][0];
        this.replaySN = true;
    
        // find the first of this sample
        int i = rowData;
        while (allData[i][0].equalsIgnoreCase(sampleName)) {
            i--;
        }
        
        
        int sampleSeq = rowData - i; 
        
        sampleName = sampleName.substring(6).toLowerCase() + "-" + sampleSeq; // remove "BOS5C "
        //String snSampleName = sampleName + "-" + (rowData - i + 1);
        
        Iterator itr = this.listSnps.iterator();
//        if (this.SNsnps != null)
//             this.SNsnps.clear();
        boolean notFound = true;
        foundSN = false;
        java.util.List<SNParameters> sns = new ArrayList<SNParameters>();
        while (notFound && itr.hasNext()){
            sns = (java.util.List<SNParameters>)itr.next();
            Iterator it = sns.iterator();
           
            if (it.hasNext()){
                String snSampleName = ((SNParameters)it.next()).getSampleName().toLowerCase();

                if (snSampleName.equalsIgnoreCase(sampleName)){
                    notFound = false;
                    this.foundSN = true;
                    this.SNsnps = sns;                           
                }
            }
        }
 /*    
        if (notFound){
            this.foundSN = false;
        } else {
            this.foundSN = true;
            itr = SNsnps.iterator();
            i=0;
            while (itr.hasNext()){
                SNParameters p = (SNParameters) itr.next();
                System.out.println("graphingData.java: " + p.getSampleName() + " row: " + rowData + " " + (i++) + "th: omega "
                         + p.getOmega() + " " + p.getXi() + " " + p.getAlpha() + " " +  p.getfDataSN()
                         + " maxGS " + p.getDataMax());     
            }
        }
  * 
  */
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //f.getContentPane().add(new GraphingData()); 
        //f.getContentPane().add(new Slider());

        //f.add(new Slider());
       // f.add(new Slider());
        f.add(new GraphingData());
        
        f.setSize(800,600);
        //f.setLocation(200,200);
        f.setVisible(true);
    }
}
