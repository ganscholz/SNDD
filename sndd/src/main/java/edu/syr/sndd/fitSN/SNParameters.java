/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.fitSN;

/**
 *
 * @author eximlean
 */
public class SNParameters {
    private double omega;
    private double xi;
    private double alpha;
    private double fDataSN;
    private double dataMax;
    private double omega_increment;
    private double xi_increment;
    private double alpha_increment;
    private String sampleName;

    public SNParameters() {
    	
    }
    public SNParameters(double om, double xi, double al, double fd) {
    	this.setOmega(om);
    	this.setXi(xi);
    	this.setAlpha(al);
    	this.setfDataSN(fd);
    }
    
    public double getAlpha_increment() {
        return alpha_increment;
    }

    public void setAlpha_increment(double alpha_increment) {
        this.alpha_increment = alpha_increment;
    }

    public double getOmega_increment() {
        return omega_increment;
    }

    public void setOmega_increment(double omega_increment) {
        this.omega_increment = omega_increment;
    }

    public double getXi_increment() {
        return xi_increment;
    }

    public void setXi_increment(double xi_increment) {
        this.xi_increment = xi_increment;
    }
    

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getOmega() {
        return omega;
    }

    public void setOmega(double omega) {
        this.omega = omega;
    }

    public double getXi() {
        return xi;
    }

    public void setXi(double xi) {
        this.xi = xi;
    }

    public double getfDataSN() {
        return fDataSN;
    }

    public void setfDataSN(double fDataSN) {
        this.fDataSN = fDataSN;
    }

    public double getDataMax() {
        return dataMax;
    }

    public void setDataMax(double dataMax) {
        this.dataMax = dataMax;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
       
}
