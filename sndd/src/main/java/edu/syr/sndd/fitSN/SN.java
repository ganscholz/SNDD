/*
 *   Document   : skewNormalDistribution
 *   Created on : Aug 24, 2011, 7:35:21 PM
 *   Author     : Stoney Q. Gan
 *   copyRight by: Stoney Q. Gan
 */
package edu.syr.sndd.fitSN;

public class SN {
   private double skewness;
   private double kurtosis;
   private double mean;
   private double median;
   private double miu; // standard deviation
   
    public double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(double kurtosis) {
        this.kurtosis = kurtosis;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMedian() {
        return median;
    }

    public void setMedian(double median) {
        this.median = median;
    }

    public double getMiu() {
        return miu;
    }

    public void setMiu(double miu) {
        this.miu = miu;
    }

    public double getSkewness() {
        return skewness;
    }

    public void setSkewness(double skewness) {
        this.skewness = skewness;
    } 
   
}
