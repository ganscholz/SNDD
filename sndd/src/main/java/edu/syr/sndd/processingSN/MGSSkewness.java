package edu.syr.sndd.processingSN;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.syr.sndd.common.util.AppConstant;


public class MGSSkewness {
	
	public static double evalMGS(double omega, double skewness) {
		   double c=Math.cbrt(2*skewness/(4-Math.PI));

		   double c2 = 1/Math.sqrt(c*c + Math.PI / 2);
		   
//	       double f1 = (4-Math.PI)/2;
//	       double f2 = Math.pow((delta*Math.sqrt(2/Math.PI)), 3);
//	       double f3 = Math.pow((1-2*delta*delta/Math.PI), 1.5);
		   System.out.print(2*skewness/(4-Math.PI) + " " + c + " " + omega + " " + c2 + " " + c*omega*c2 + "\n");
	       return c*omega*c2;
	}

	public static void main(String[] args) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("H:\\dissertation\\alpha.txt"));
		
		for (double i=AppConstant.ALPHA_MIN; i<=AppConstant.ALPHA_MAX; i += AppConstant.ALPHA_INCREMENT) {
			writer.write(i + "\n");
		}
		writer.close();
		
		writer = new BufferedWriter(new FileWriter("H:\\dissertation\\xi.txt"));
		for (double k=AppConstant.XI_MIN; k<=AppConstant.XI_MAX; k+=AppConstant.XI_INCREMENT) {
			writer.write(k  + "\n");
		}
		writer.close();
		
		writer = new BufferedWriter(new FileWriter("H:\\dissertation\\omega.txt"));
		for (double j=AppConstant.OMEGA_MIN; j<= AppConstant.OMEGA_MAX; j+= AppConstant.OMEGA_INCREMENT) {
			writer.write(j + "\n");
		}
		writer.close();
		
		writer = new BufferedWriter(new FileWriter("H:\\dissertation\\skewness.txt"));
		for (double j=-0.995; j<= 0.995; j+= 0.02) {
			writer.write(j + "\n");
		}
		writer.close();
		
//		BufferedWriter writer2 = new BufferedWriter(new FileWriter("H:\\dissertation\\mgs.txt"));
		BufferedWriter writer3 = new BufferedWriter(new FileWriter("H:\\dissertation\\mgs-location.txt"));	
		
		for (double j=AppConstant.OMEGA_MIN; j<= AppConstant.OMEGA_MAX; j+= AppConstant.OMEGA_INCREMENT) {
			for (double k=-0.995; k<=0.995; k+= 0.02) {
				writer3.write(evalMGS(j, k) + " ");
			}
			writer3.write("\n");
		}
//		for (double i=AppConstant.ALPHA_MIN; i<=AppConstant.ALPHA_MAX; i += AppConstant.ALPHA_INCREMENT) {
//				for (double k=AppConstant.XI_MIN; k<=AppConstant.XI_MAX; k+=AppConstant.XI_INCREMENT) {
//					for (double j=AppConstant.OMEGA_MIN; j<= AppConstant.OMEGA_MAX; j+= AppConstant.OMEGA_INCREMENT) {			
//						writer2.write(SNGenerator.computeMean(j, k, i) + "\n");
//						writer3.write(SNGenerator.computeSkewness(j, k, i) + "\n");
//					}
//			}
//		}
		
//		writer2.close();
		writer3.close();
		System.out.println("I am done!");
	}

}
