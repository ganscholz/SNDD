package edu.syr.sndd.fitSN;

import edu.syr.sndd.common.util.AppConstant;

public class SNPartWithPivot {

	/**
	 * calculate SN parameters from mean, stdev, skewness
	 * @param mean
	 * @param stdev
	 * @param skewness
	 * @return
	 */
	public SNParameters calculateSNParams(double mean, double stdev, double skewness) {	
     double c1 = 2 - Math.PI/2;
     double c2 = 2/Math.PI;
     double c3 = Math.cbrt(c1);
     double c4 = Math.cbrt(skewness)/c3;
     double c5 = c4/c2;
     double c6 = 1/(c2 + c5);
     double alpha = Math.sqrt(c6/(1-c6));   
	 double d = alpha * Math.sqrt(2/Math.PI)/Math.sqrt(1+alpha*alpha);
	 double delta = alpha / Math.sqrt(1+alpha*alpha);
	 double aomega = Math.sqrt(1/(1 - 2*delta*delta/Math.PI));
	 double axi = mean - aomega * d;
 
	 return new SNParameters(aomega, axi, alpha, 1);
	}
	
	/** get parameter ranges for mean (mean-stdev, mean+stdev)
	 * @since 2015-12-27
	 * @param mean
	 * @param stdev it is sqrt of variance
	 * @param skewness
	 */
	public SNParameters[] getParamRange(double mean, double stdev, double skewness) {
		double lowrange = mean - stdev;
		double highrange = mean + stdev;
		
		SNParameters SNPRange[] = new SNParameters[2];
		SNPRange[0] = calculateSNParams(lowrange, stdev, skewness);
		SNPRange[1] = calculateSNParams(highrange, stdev, skewness);
		
		return SNPRange;
	}
	
	public double [] bestFitComponent(double data[], SNParameters pivotSnp, final SNParameters[] SNPRange) {
		double estimateSN[]=null;
		for (double a = SNPRange[0].getOmega(); a<= SNPRange[1].getOmega(); a += AppConstant.OMEGA_INCREMENT) {
			for (double b = SNPRange[0].getXi(); b<= SNPRange[1].getXi(); b += AppConstant.XI_INCREMENT) { 
				for (double c=SNPRange[0].getAlpha(); c<=SNPRange[1].getAlpha(); b+=AppConstant.ALPHA_INCREMENT){
					estimateSN = SNGenerator.generateSN(a, b, c);
					double max = SNUtils.getMinMax(data, SNUtils.MAX);
					double fdata = max / SNUtils.getMinMax(estimateSN, SNUtils.MAX);
					for (int i=0; i<=estimateSN.length; i++)
						estimateSN[i] = fdata*estimateSN[i];
					
					double residual[] = DeviationEstimator.getResidue(data, estimateSN);
				}
			}
		}
		
		return estimateSN;
	}
}
