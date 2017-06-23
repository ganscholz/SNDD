/*
 *   Document   : skewNormalDistribution
 *   Created on : Aug 24, 2011, 7:35:21 PM
 *   Author     : Stoney Q. Gan
 *   copyRight by: Stoney Q. Gan
 */
package edu.syr.sndd.fitSN;

public class ParameterTuner {
    public static SNParameters tuneParameters(double [] gzData){
        double omega_min = 0.01;
        double omega_max = 4.0;
        double omega_increment = 0.1;
        double xi_min = 0.0;
        double xi_max = 5.0;
        double xi_increment = 0.1;
        
        double alpha_min = -10.0;
        double alpha_max = 10.0;
        double alpha_increment = 0.1;
        
        double curMin = 10000000.0;
        double omegaCurMin=1;
        double xiCurMin=0;
        double alphaCurMin=0;
      /*  
        for (double x = omega_min; x<=omega_max; x+= omega_increment){
            for (double y = xi_min; y<= xi_max; y+= xi_increment){
                for (double z= alpha_min; z<= alpha_max; z+= alpha_increment){
                    double [] snData = SNGenerator.generateSN(x, y, z);
                    double estimateSD = DeviationEstimator.estimateDeviation(gzData, snData,
                            );
                    if (estimateSD < curMin){
                        curMin = estimateSD;
                        omegaCurMin = x;
                        xiCurMin = y;
                        alphaCurMin = z;
                    }                    
                }
            }
            
        }
        */
        SNParameters snp = new SNParameters();
        snp.setAlpha(alphaCurMin);
        snp.setOmega(omegaCurMin);
        snp.setXi(xiCurMin);
        
        return snp;
    }

    
}
