/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.graphResults;

/**
 *
 * @author Stoney Q. Gan
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DecimalFormat;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Playing class for slide GUI.
 * @author Stoney Q. Gan
 *
 */
public class Slider extends JPanel 
                    implements ActionListener,
                               WindowListener,
                               ChangeListener {
    final int MIN = 0;
    final int MAX = 200;
    final int INTERVAL = 20;
    final int MIN_LABEL = -10;
    final int MAX_LABEL = 10;
    double fReal2Scale = (double)(MAX_LABEL - MIN_LABEL)/(MAX-MIN);
    final DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.0" );
    
   public Slider() {
        super(true);
        
        this.setLayout(new BorderLayout());
        JSlider slider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, (MAX-MIN)/2);

        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(INTERVAL);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        // create label table
        Hashtable<Integer, JLabel> labelTable = 
            new Hashtable<Integer, JLabel>();
        for (int i=MIN; i<=MAX; i+=INTERVAL){
            double value = new Double(df2.format(fReal2Scale*((double)(i-MIN)) + (double)MIN_LABEL)).doubleValue();
            labelTable.put(i,
                       new JLabel(String.valueOf(value)));
        }
        
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        
        Font font = new Font("Serif", Font.ITALIC, 15);
        slider.setFont(font);

        
        slider.addChangeListener(this);
        
        //Put everything together.
         // add(graph...);
        add(slider);
        

        //setBorder(BorderFactory.createEmptyBorder(500,10,10,10));
  
        //add(slider, BorderLayout.CENTER);
    }
    /** Add a listener for window events. */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }

    //React to window events.
    public void windowDeiconified(WindowEvent e) {
        //startAnimation();
        // redraw???
    }
    public void windowOpened(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    /** Listen to the slider. */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            double scaleValue = source.getValue();
            double realValue = fReal2Scale*((double)(scaleValue-MIN)) + (double)MIN_LABEL;
            System.out.println("get value: " + realValue);
            // calculate SN and re-draw diagram
        }
    }

    public static void main(String s[]) {
         JFrame frame = new JFrame("SN fitting");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setSize(800,600);
        // frame.add(new GraphingData());
         frame.add(new Slider());
         //frame.setContentPane(new Slider());
         frame.pack();
         frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowIconified(WindowEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
