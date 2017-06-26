/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.graphResults;

import edu.syr.sndd.common.util.AppConstant;
import edu.syr.sndd.common.util.ExcelUtil;
import edu.syr.sndd.fitSN.SNGenerator;
import edu.syr.sndd.fitSN.SNParameters;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.text.*;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JTextField;

/**
 * This is the class that handles the graphics using awt. It reads the data file defined in the common.util.AppConstants and creates the GUI for the actions. 
 * Here is a short description of the buttons:
 * <li> Next Sample: simple display the grain size distribution (GSD) data of the next sample. </li>
 * <li> Prev Sample: display GSD of the previous sample. </li>
 * <li> Go to: display GSD of the n'th sample, e.g., go to 100 will display GSD of the 100'th sample. </li>
 * <li> Batch All: partition all samples in a batch mode. The result will be restore in the output specified in the common.util.AppConstants. </li>
 * <li> Auto SNND: SNDD the current sample. </li>
 * <li> Adj SN: 	 manually fit an SN by adjusting the slide bars of SN parameters. This function is created for testing and allows users to refine an SN partition. </li>
 * <li> Refine fit:  use this button to SNDD the current GSD sample after manually fit an SN, i. e., it will honor the manually fitted SN's and partition the residual data. If you want to
 * 					 manually SNDD a sample, follow the following sequence: Auto SNND -> adjust slide bars to get your satisfied result for the dominant component -> Refine SNDD 
 * 					 -> adjust slide bars to manually SNDD next component .... </li>
 * <li> stdz:		 Standardize all samples before processing using the first row as the standard. It is assumed that the first is the average GSD for all samples. This button is rarely used.</li>	
 * <p>
 * slides for skew normal distribution parameters:
 * <li> \u03C9: scale parameter. </li>
 * <li> \u03BE:	   location parameter </li>
 * <li> \u03B1: shape parameter. </li>
 * </p>
 * @author Stoney Q. Gan
 * 
 *
 */
public class SNND extends JFrame {

    protected JLabel label;
    protected JSlider sliderOmega;
    protected JSlider sliderXi;
    protected JSlider sliderAlpha;
    protected JLabel labelOmega;
    protected JLabel labelXi;
    protected JLabel labelAlpha;
    protected JLabel labelSampleNum;
    protected JLabel labelMean;
    protected JLabel labelParam;
    protected JLabel labelVariance;
    protected JLabel labelSkewness;
    protected JTextField jtfInput;
    protected JTextField jtfMSNInput;
    
    final int WIDTH = 800;
    final int HEIGH = 500;
    final int PAD = 20;
    final int BUTTON_WIDTH = 100;
    final int BUTTON_HEIGHT = 40;
    final int OMEGA_INTERNAL_MIN = 0;
    final int OMEGA_INTERNAL_MAX = 1000;
    final int OMEGA_INTERNAL_INTERVAL = 200;
    final int XI_INTERNAL_MIN = 0;
    final int XI_INTERNAL_MAX = 1000;
    final int XI_INTERNAL_INTERVAL = 200;
    final int ALPHA_INTERNAL_MIN = 0;
    final int ALPHA_INTERNAL_MAX = 1000;
    final int ALPHA_INTERNAL_INTERVAL = 200;
    final DecimalFormat df2 = new DecimalFormat( "##0.0" );
    protected GraphingData plotPanel;
    protected double fReal2ScaleOmega = (double) ((double) (AppConstant.OMEGA_MAX - AppConstant.OMEGA_MIN) / (OMEGA_INTERNAL_MAX - OMEGA_INTERNAL_MIN));
    protected double fReal2ScaleXi = (double) ((double) (AppConstant.XI_MAX - AppConstant.XI_MIN) / (XI_INTERNAL_MAX - XI_INTERNAL_MIN));
    protected double fReal2ScaleAlpha = (double) ((double) (AppConstant.ALPHA_MAX - AppConstant.ALPHA_MIN) / (ALPHA_INTERNAL_MAX - ALPHA_INTERNAL_MIN));
    protected int curRowIndex = 1;
    protected DecimalFormat df = new DecimalFormat("#.####");
    protected String OMEGA_UNICODE = "\u03C9";
    protected String XI_UNICODE = "\u03BE";
    protected String ALPHA_UNICODE = "\u03B1";

    protected static final String SNRESULT_FILENAME="SNResults.xlsx";
    protected int gotoIndex;
 
    public SNND() {
    }

    /**
     * This is the method actually creates all GUI components including slide bars and buttons, and connect them to action methods.
     * @return
     */
    public JPanel getUIPanel() {
        getContentPane().setLayout(new FlowLayout());

        JPanel p1 = new JPanel();

        p1.setBorder(BorderFactory.createEtchedBorder());
        //p1.repaint();
        p1.setSize(WIDTH - 2 * PAD, 150);

        JPanel p = new JPanel();
        p.setSize(WIDTH - 2 * PAD, 50);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        sliderOmega = new JSlider(JSlider.HORIZONTAL, OMEGA_INTERNAL_MIN, OMEGA_INTERNAL_MAX, OMEGA_INTERNAL_MAX / 6);
        sliderOmega.setMinorTickSpacing(OMEGA_INTERNAL_INTERVAL / 5);
        sliderOmega.setMajorTickSpacing(OMEGA_INTERNAL_INTERVAL);
        sliderOmega.setPaintTicks(true);
        sliderOmega.setPaintLabels(true);

        OmegaChangeListener listenerOmega = new OmegaChangeListener();
        sliderOmega.addChangeListener(listenerOmega);

        Hashtable<Integer, JLabel> labelTable =
                new Hashtable<Integer, JLabel>();
        for (int i = OMEGA_INTERNAL_MIN; i <= OMEGA_INTERNAL_MAX; i += OMEGA_INTERNAL_INTERVAL) {
            double value = fReal2ScaleOmega * (double) (i - OMEGA_INTERNAL_MIN) + (double) AppConstant.OMEGA_MIN;
            value = new Double(df2.format(value)).doubleValue();
            labelTable.put(i,
                    new JLabel(String.valueOf(value)));
        }

        sliderOmega.setLabelTable(labelTable);
        sliderOmega.setPaintLabels(true);
        sliderOmega.setFont(new Font("Arial", Font.ITALIC, 12));
        p.add(sliderOmega);

        labelOmega = new JLabel("Omega");
        labelOmega.setFont(new Font("Arial", Font.ITALIC, 12));
        p.add(labelOmega);

        p1.add(p);

        // second slider
        sliderXi = new JSlider(JSlider.HORIZONTAL, XI_INTERNAL_MIN,
                XI_INTERNAL_MAX, (XI_INTERNAL_MAX - XI_INTERNAL_MIN) / 4);
        sliderXi.setMinorTickSpacing(XI_INTERNAL_INTERVAL / 5);
        sliderXi.setMajorTickSpacing(XI_INTERNAL_INTERVAL);
        sliderXi.setPaintTicks(true);
        sliderXi.setPaintLabels(true);

        XiChangeListener listenerXi = new XiChangeListener();
        sliderXi.addChangeListener(listenerXi);

        labelTable = new Hashtable<Integer, JLabel>();
        for (int i = XI_INTERNAL_MIN; i <= XI_INTERNAL_MAX; i += XI_INTERNAL_INTERVAL) {
            double value = fReal2ScaleXi * (double) (i - XI_INTERNAL_MIN) + (double) AppConstant.XI_MIN;
            value = new Double(df2.format(value)).doubleValue();
            labelTable.put(i,
                    new JLabel(String.valueOf(value)));
        }
        sliderXi.setLabelTable(labelTable);
        sliderXi.setPaintLabels(true);
        sliderXi.setFont(new Font("Arial", Font.ITALIC, 12));

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setSize(WIDTH - 2 * PAD, 50);
        p.add(sliderXi);

        labelXi = new JLabel("5 Xi");
        labelXi.setFont(new Font("Arial", Font.ITALIC, 12));

        p.add(labelXi);

        p1.add(p);

        // the slider for alpha
        sliderAlpha = new JSlider(JSlider.HORIZONTAL, ALPHA_INTERNAL_MIN, ALPHA_INTERNAL_MAX,
                (ALPHA_INTERNAL_MAX - ALPHA_INTERNAL_MIN) / 2);
        sliderAlpha.setMinorTickSpacing(ALPHA_INTERNAL_INTERVAL / 5);
        sliderAlpha.setMajorTickSpacing(ALPHA_INTERNAL_INTERVAL);
        sliderAlpha.setPaintTicks(true);
        sliderAlpha.setPaintLabels(true);

        AlphaChangeListener listenerAlpha = new AlphaChangeListener();
        sliderAlpha.addChangeListener(listenerAlpha);

        labelTable = new Hashtable<Integer, JLabel>();
        for (int i = ALPHA_INTERNAL_MIN; i <= ALPHA_INTERNAL_MAX; i += ALPHA_INTERNAL_INTERVAL) {
            double value = fReal2ScaleAlpha * (double) (i - ALPHA_INTERNAL_MIN) + (double) AppConstant.ALPHA_MIN;
            value = new Double(df2.format(value)).doubleValue();
            labelTable.put(i,
                    new JLabel(String.valueOf(df.format(value))));
        }
        sliderAlpha.setLabelTable(labelTable);
        sliderAlpha.setPaintLabels(true);
        sliderAlpha.setFont(new Font("Arial", Font.ITALIC, 12));

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(sliderAlpha);

        labelAlpha = new JLabel("5 alpha");
        labelAlpha.setFont(new Font("Arial", Font.ITALIC, 12));
        p.add(labelAlpha);

        p1.add(p);

        listenerOmega.stateChanged(new ChangeEvent(sliderOmega));
        listenerXi.stateChanged(new ChangeEvent(sliderXi));
        listenerAlpha.stateChanged(new ChangeEvent(sliderAlpha));

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JButton buttonNext = new JButton("Next Sample");
        JButton buttonPrev = new JButton("Prev Sample");
        
        buttonNext.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        buttonPrev.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        
        JPanel tmpJP = new JPanel();
        tmpJP.setSize(BUTTON_WIDTH/2, BUTTON_HEIGHT);
        BoxLayout boxLayout = new BoxLayout(tmpJP, BoxLayout.X_AXIS);
        tmpJP.setLayout(boxLayout);

        
        JLabel gotoJL = new JLabel("Go to:");
        gotoJL.setFont(new Font("Arial", Font.BOLD, 12));
                       
        jtfInput = new JTextField(4);
        jtfInput.setSize(BUTTON_WIDTH/2, BUTTON_HEIGHT);
           
        tmpJP.add(gotoJL);
        tmpJP.add(jtfInput);
     
        p.add(buttonNext);
        p.add(buttonPrev);
        p.add(tmpJP);
        
        JButton buttonBatch = new JButton("Batch All");
        p.add(buttonBatch);
        
        JButton buttonAuto = new JButton("Auto SNND");
        buttonAuto.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        p.add(buttonAuto);

        tmpJP = new JPanel();
        tmpJP.setSize(BUTTON_WIDTH/2, BUTTON_HEIGHT);
        boxLayout = new BoxLayout(tmpJP, BoxLayout.X_AXIS);
        tmpJP.setLayout(boxLayout);

        JLabel MSNJL = new JLabel("Adj SN:");
        MSNJL.setFont(new Font("Arial", Font.BOLD, 12));
                       
        jtfMSNInput = new JTextField(4);
        jtfMSNInput.setSize(BUTTON_WIDTH/2, BUTTON_HEIGHT);
           
        tmpJP.add(MSNJL);
        tmpJP.add(jtfMSNInput);
        p.add(tmpJP);
        
        JButton buttonRefine = new JButton("Refine SNND");
        buttonRefine.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        p.add(buttonRefine);
        
        JButton buttonStdz = new JButton("Stdz");
        buttonStdz.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        p.add(buttonStdz);
        
//        JButton buttonWriteSN = new JButton("Write SN");
//        buttonRefine.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
//        p.add(buttonWriteSN);
        
        ActionButtonNext actionButtonNext = new ActionButtonNext();
        buttonNext.addActionListener(actionButtonNext);

        ActionButtonPrev actionButtonPrev = new ActionButtonPrev();
        buttonPrev.addActionListener(actionButtonPrev);
        
        ActionGotoSample actionGotoSample = new ActionGotoSample();
        jtfInput.addActionListener(actionGotoSample);
        
        ActionButtonBatch actionButtonBatch = new ActionButtonBatch();
        buttonBatch.addActionListener(actionButtonBatch);
        
        ActionAutoEstimate actionAuto = new ActionAutoEstimate();
        buttonAuto.addActionListener(actionAuto);
        
        ActionManualAdjustment actionManual = new ActionManualAdjustment();
        jtfMSNInput.addActionListener(actionManual);

        ActionRefine actionRefine = new ActionRefine();
        buttonRefine.addActionListener(actionRefine);

        ActionStdz actionStdz = new ActionStdz();
        buttonStdz.addActionListener(actionStdz);
//        ActionButtonWriteSN   actionWrtieSN = new ActionButtonWriteSN();
//        buttonWriteSN.addActionListener(actionWrtieSN);
        
        p1.add(p);

        p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        labelSampleNum = new JLabel("Sample Name: " + plotPanel.allData[curRowIndex][0]);
        labelSampleNum.setFont(new Font("Arial", Font.ITALIC, 12));
        labelSampleNum.setForeground(Color.MAGENTA);
        labelSampleNum.setBorder(BorderFactory.createEtchedBorder());

        p.add(labelSampleNum);

        labelParam = new JLabel("Param " + String.valueOf(df.format(plotPanel.omega)));
        labelParam.setFont(new Font("Arial", Font.ITALIC, 12));
        labelParam.setForeground(Color.MAGENTA);
        labelParam.setBorder(BorderFactory.createEtchedBorder());

        p.add(labelParam);
        
        labelMean = new JLabel("Estimate Mean = " + String.valueOf(df.format(plotPanel.mean)));
        labelMean.setFont(new Font("Arial", Font.ITALIC, 12));
        labelMean.setForeground(Color.MAGENTA);
        labelMean.setBorder(BorderFactory.createEtchedBorder());

        p.add(labelMean);

        labelVariance = new JLabel("Variance " + String.valueOf(df.format(plotPanel.variance)));
        labelVariance.setFont(new Font("Arial", Font.ITALIC, 12));
        labelVariance.setForeground(Color.MAGENTA);
        labelVariance.setBorder(BorderFactory.createEtchedBorder());

        p.add(labelVariance);

        labelSkewness = new JLabel("Skewness " + String.valueOf(df.format(plotPanel.skewness)));
        labelSkewness.setFont(new Font("Arial", Font.ITALIC, 12));
        labelSkewness.setForeground(Color.MAGENTA);
        labelSkewness.setBorder(BorderFactory.createEtchedBorder());

        p.add(labelSkewness);

        p1.add(p);

        return p1;
    }

    /**
     * Compute and display the statistic parameters on the screen: (scale, location, shape, mean, variance, skewness).
     */
    private void setStatisticValues() {
        DecimalFormat df = new DecimalFormat("#.##");
        edu.syr.sndd.processingSN.PartitionVolume pv = new edu.syr.sndd.processingSN.PartitionVolume();
        
        Iterator itr = plotPanel.snps.iterator();
        
        String strParams = "Params: ";
        String strMeans = "Means: ";
        String strVariances = "Variances: ";
        String strSkewnesses = "Skewnesses: ";

        while (itr.hasNext()) {
            SNParameters nps = (SNParameters) itr.next();
            double om = nps.getOmega();
            double xi = nps.getXi();
            double al = nps.getAlpha();
            
            double volPC = pv.calVolPercent(SNGenerator.generateSN(nps),plotPanel.getGzData());
            
            //double fsd = nps.getfDataSN();
            double mn = SNGenerator.computeMean(om, xi, al);
            double va = SNGenerator.computeVariance(om, xi, al);
            double sk = SNGenerator.computeSkewness(om, xi, al);
            
            strParams += (" (" + String.valueOf(df.format(volPC)) + "," + String.valueOf(df.format(om)) + "," +
                            String.valueOf(df.format(xi)) + "," +
                            String.valueOf(df.format(al))  + ");");
            strMeans += (" " + String.valueOf(df.format(mn)) + ";");
            strVariances += (" " + String.valueOf(df.format(va))+ ";");
            strSkewnesses += (" " + String.valueOf(df.format(sk)) + ";");

        }

        labelParam.setText(strParams);
        labelMean.setText(strMeans);
        labelVariance.setText(strVariances);
        labelSkewness.setText(strSkewnesses);
    }

    /**
     * set computed SN parameter to their slides.
     */
    private void setSlideValues() {
        double omegaVal;
        double xiVal;
        double alphaVal;
        if (plotPanel.snps.isEmpty()) {
            omegaVal = plotPanel.omega;
            xiVal = plotPanel.xi;
            alphaVal = plotPanel.alpha;
        } else {
            int i = plotPanel.adjSN - 1; // set sliders value to the one which will be adjusted manually.
            omegaVal = plotPanel.snps.get(i).getOmega();
            xiVal = plotPanel.snps.get(i).getXi();
            alphaVal = plotPanel.snps.get(i).getAlpha();
            if(plotPanel.debug){
                System.out.println("the ith snp: " + i + " " + omegaVal + " " + xiVal + " " + alphaVal);
            }
        }

        int omegaSlideValue = (int) ((omegaVal - AppConstant.OMEGA_MIN) / fReal2ScaleOmega) + OMEGA_INTERNAL_MIN;
        sliderOmega.setValue(omegaSlideValue);
        int xiSlideValue = (int) ((xiVal - AppConstant.XI_MIN) / fReal2ScaleXi) + XI_INTERNAL_MIN;
        sliderXi.setValue(xiSlideValue);
        int alphaSlideValue = (int) ((alphaVal - AppConstant.ALPHA_MIN) / fReal2ScaleAlpha) + ALPHA_INTERNAL_MIN;
        sliderAlpha.setValue(alphaSlideValue);

        String strLabel = OMEGA_UNICODE + " = " + df.format(plotPanel.omega);
        labelOmega.setText(strLabel);

        strLabel = XI_UNICODE + " = " + df.format(plotPanel.xi);
        labelXi.setText(strLabel);

        strLabel = ALPHA_UNICODE + " = " + df.format(plotPanel.alpha);
        labelAlpha.setText(strLabel);

        plotPanel.manualSN = true;
    }

    /**
     * Button "next" action listener class: get the next sample and load to graph display.
     *
     */
    class ActionButtonNext implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (curRowIndex < plotPanel.numSamples - 1) {
                curRowIndex++;
                plotPanel.sampleName = plotPanel.allData[curRowIndex][0];
                labelSampleNum.setText("Sample " + curRowIndex + " :" +  plotPanel.sampleName);
            }

            try {
                plotPanel.getGZData(curRowIndex);
                plotPanel.retrieveAndCalSnps(curRowIndex);
                //plotPanel.calculateSN(plotPanel.gzData_ymax);     
                plotPanel.myRepaint();

            } catch (Exception ex) {
                Logger.getLogger(SNND.class.getName()).log(Level.SEVERE, null, ex);
            }
            plotPanel.adjSN = 1;
        }
    };


    /**
     * Button "Previous" action listener class: get the previous sample and load to graph display.
     *
     */
    class ActionButtonPrev implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (curRowIndex > 1) {
                curRowIndex--;
                plotPanel.sampleName = plotPanel.allData[curRowIndex][0];
                labelSampleNum.setText("Sample " + curRowIndex + " :" +  plotPanel.sampleName);
            }

            try {      
                plotPanel.getGZData(curRowIndex);
                plotPanel.retrieveAndCalSnps(curRowIndex);     
                //plotPanel.calculateSN(plotPanel.gzData_ymax); 
                plotPanel.myRepaint();
            } catch (Exception ex) {
                Logger.getLogger(SNND.class.getName()).log(Level.SEVERE, null, ex);
            }
            plotPanel.adjSN = 1;
        }
    };
    

    /**
     * Button "Go to" action listener class: get the named sample and load to graph display.
     *
     */
    class ActionGotoSample implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            gotoIndex = Integer.parseInt(jtfInput.getText());
            curRowIndex = gotoIndex;
            plotPanel.sampleName = plotPanel.allData[curRowIndex][0];
            labelSampleNum.setText("Sample " + curRowIndex + " :" +  plotPanel.sampleName);

            try {
                plotPanel.getGZData(curRowIndex);
                plotPanel.retrieveAndCalSnps(curRowIndex);
                //plotPanel.calculateSN(plotPanel.gzData_ymax); 
                plotPanel.myRepaint();
            } catch (Exception ex) {
                Logger.getLogger(SNND.class.getName()).log(Level.SEVERE, null, ex);
            }
            plotPanel.adjSN = 1;
            //plotPanel.manualSN = true;
        }
    };
 
    /**
     * Append the current SN result to the excel file.
     *
     */
     class ActionButtonWriteSN implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                ExcelUtil.appendSNResultToExcel(SNRESULT_FILENAME, plotPanel.snps);
            } catch (Exception ex) {
                Logger.getLogger(SNND.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
     
    /**
     * Action class for run all sample in the batch mode (Batch All).
     *
     */
    class ActionButtonBatch implements ActionListener {
        
        public void actionPerformed(ActionEvent e) {
            plotPanel.partitionSN(false);
        }
    };
 
    /**
     * Action class for single sample automatic partition.
     *
     */
    class ActionAutoEstimate implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            //plotPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            plotPanel.replaySN = false;
            plotPanel.manualSN = false;
            plotPanel.nmodeSNEstimate();
            //plotPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            setStatisticValues();
            setSlideValues();
        }
    }
    
    /**
     * Action class for "adj SN" input field.
     *
     */
    class ActionManualAdjustment implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            //plotPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            plotPanel.adjSN = Integer.parseInt(jtfMSNInput.getText());
            plotPanel.replaySN = false;
            plotPanel.manualSN = true;
            setSlideValues();
            plotPanel.snps.clear();
            plotPanel.snps = plotPanel.SNsnps;
            
            //plotPanel.nmodeSNEstimate();
            //plotPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            //setStatisticValues();
            //setSlideValues();
        }
    }
    
    /**
     * Action class for "refine button" partition after manual fit.
     *
     */
    class ActionRefine implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            plotPanel.manualSN = false;
            plotPanel.nmodeSNRefine();
            plotPanel.adjSN++;
            //plotPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            setStatisticValues();
            setSlideValues();
        }
    }
    
    /**
     * Action class for the button "stdz" to standard GSD.
     *
     */
    class ActionStdz implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                plotPanel.standardizeGZData();
                plotPanel.partitionSN(true);
            } catch (Exception ex) {
                Logger.getLogger(SNND.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * 
     * Action class for scale parameter \u03C9 slide.
     *
     */
    class OmegaChangeListener implements ChangeListener {

        OmegaChangeListener() {
        }

        public synchronized void stateChanged(ChangeEvent e) {
            int omega = sliderOmega.getValue();

            double realValue = fReal2ScaleOmega * ((double) (omega)) + (double) AppConstant.OMEGA_MIN;

            String labelStr = OMEGA_UNICODE + " = " + String.valueOf(df.format(realValue));
            labelOmega.setText(labelStr);

            plotPanel.setOmega(realValue);
            if (plotPanel.manualSN) {
                plotPanel.adjustDominantSN(AppConstant.OMEGA);
                // 2013-02-24 next two lines
                plotPanel.manualSN=true;
                plotPanel.repaint();
            }
        }
    }

    /**
     * Action class for the location parameter \u03BE slide.
     *
     */
    class XiChangeListener implements ChangeListener {

        XiChangeListener() {
        }

        public synchronized void stateChanged(ChangeEvent e) {
            int xi = sliderXi.getValue();

            double realValue = fReal2ScaleXi * ((double) (xi)) + (double) AppConstant.XI_MIN;

            String labelStr = XI_UNICODE + " = " + String.valueOf(df.format(realValue));
            labelXi.setText(labelStr);

            plotPanel.setXi(realValue);
            if (plotPanel.manualSN) {
                plotPanel.adjustDominantSN(AppConstant.XI);
            }
        }
    }

    /**
     * Action class for the shape parameter \u03B1 slide.
     * @author sgan
     *
     */
    class AlphaChangeListener implements ChangeListener {

        AlphaChangeListener() {
        }

        public synchronized void stateChanged(ChangeEvent e) {
            int alpha = sliderAlpha.getValue();

            double realValue = fReal2ScaleAlpha * ((double) (alpha)) + (double) AppConstant.ALPHA_MIN;

            String labelStr = ALPHA_UNICODE + " = " + String.valueOf(df.format(realValue));
            labelAlpha.setText(labelStr);

            plotPanel.setAlpha(realValue);
            if (plotPanel.manualSN) {
                plotPanel.adjustDominantSN(AppConstant.ALPHA);
            }
        }
    }
   
   /**
    * Read the data and prepare all graphics.
    * @return
    */
    private JPanel getContent() {
        plotPanel = new GraphingData();
        return plotPanel;
    }

    /**
     * Initialize the graphics.
     * @param za
     */
    public static void main(String[] za) {
        SNND d = new SNND();
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(d.getContent());
        f.add(d.getUIPanel(), "Last");
        f.setSize(1000, 600);
        f.setLocation(50, 50);
        f.setVisible(true);

    }
    }
