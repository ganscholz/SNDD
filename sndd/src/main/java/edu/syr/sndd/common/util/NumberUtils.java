/**
 * Class Name:   NumberUtils
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/08
 * Company:      exportSolution, Inc
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  This is the utility class used to manipulate numbers.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.util;

import java.math.BigDecimal;

public class NumberUtils {
  private NumberUtils() {
  }

  /**
   * <p>Convert a <code>String</code> to a <code>Float</code>.</p>
   *
   * @param val  a <code>String</code> to convert
   * @return converted <code>Float</code>
   * @throws NumberFormatException if the value cannot be converted
   */
  public static Float createFloat(String val) {
    return NumberUtils.createFloat(val);
  }

  /**
   * <p>Convert a <code>String</code> to a <code>Double</code>.</p>
   *
   * @param val  a <code>String</code> to convert
   * @return converted <code>Double</code>
   * @throws NumberFormatException if the value cannot be converted
   */
  public static Double createDouble(String val) {
    return NumberUtils.createDouble(val);
  }

  /**
   * <p>Convert a <code>String</code> to a <code>Integer</code>, handling
   * hex and octal notations.</p>
   *
   * @param val  a <code>String</code> to convert
   * @return converted <code>Integer</code>
   * @throws NumberFormatException if the value cannot be converted
   */
  public static Integer createInteger(String val) {
    return NumberUtils.createInteger(val);
  }

  /**
   * <p>Convert a <code>String</code> to a <code>Long</code>.</p>
   *
   * @param val  a <code>String</code> to convert
   * @return converted <code>Long</code>
   * @throws NumberFormatException if the value cannot be converted
   */
  public static Long createLong(String val) {
    return NumberUtils.createLong(val);
  }

  /**
   * <p>Convert a <code>String</code> to a <code>BigDecimal</code>.</p>
   *
   * @param val  a <code>String</code> to convert
   * @return converted <code>BigDecimal</code>
   * @throws NumberFormatException if the value cannot be converted
   */
  public static BigDecimal createBigDecimal(String val) {
    return NumberUtils.createBigDecimal(val);
  }

  /**
   * round or trunc a double number
   * @param dOrigin double
   * @param nCount int
   * @param bDischarge boolean
   * @return double
   */
  private static double getDouble(double dOrigin, int nCount,
                                  boolean bDischarge) {
    long lTemp = (long) Math.pow(10, nCount);
    if (bDischarge == true)
      return (long) (dOrigin * lTemp) / (double) lTemp;
    else
      return Math.round(dOrigin * lTemp) / (double) lTemp;
  }

  /**
   * 
   * @param amount double
   * @return double
   */
  public static double round(double amount) {
    return round(amount, 2);
  }

  /**
   * 
   * @param amount double
   * @param digits int
   * @return double
   */
  public static double round(double amount, int digits) {
    return getDouble(amount, digits, false);
  }

  /**
   * round a BigDecimal amount
   * @param amount BigDecimal
   * @param digits int
   * @return BigDecimal
   */
  public static BigDecimal round(BigDecimal amount, int digits){
    BigDecimal one=new BigDecimal("1");
    return amount.divide(one,digits,BigDecimal.ROUND_HALF_UP);
  }

  /**
   * round a String amount
   * @param amount String
   * @param digits int
   * @return BigDecimal
   */
  public static BigDecimal round(String amount,int digits){
    return round(new BigDecimal(amount),digits);
  }

  /**
   * round a BigDecimal amount
   * @param amount BigDecimal
   * @return BigDecimal
   */
  public static BigDecimal round(BigDecimal amount){
    return round(amount,2);
  }

  /**
   * round a String amount
   * @param amount String
   * @return BigDecimal
   */
  public static BigDecimal round(String amount){
    return round(amount,2);
  }

  /**
   * truncate a BigDecimal amount
   * @param amount BigDecimal
   * @param digits int
   * @return BigDecimal
   */
  public static BigDecimal truncate(BigDecimal amount,int digits){
    BigDecimal one=new BigDecimal("1");
    return amount.divide(one,digits,BigDecimal.ROUND_DOWN);
  }

  /**
   * truncate a String amount
   * @param amount String
   * @param digits int
   * @return BigDecimal
   */
  public static BigDecimal truncate(String amount,int digits){
    return truncate(new BigDecimal(amount),digits);
  }

  /**
   * truncate a BigDecimal amount
   * @param amount BigDecimal
   * @return BigDecimal
   */
  public static BigDecimal truncate(BigDecimal amount){
    return truncate(amount,2);
  }

  /**
   * truncate a String amount
   * @param amount String
   * @return BigDecimal
   */
  public static BigDecimal truncate(String amount){
    return truncate(amount,2);
  }

  /**
   * truncate an double amount with 2 digits
   * @param amount double
   * @return double
   */
  public static double truncate(double amount) {
    return truncate(amount, 2);
  }

  /**
   * truncate a double amount with the given digits
   * @param amount double
   * @param digits int
   * @return double the truncated double amount
   */
  public static double truncate(double amount, int digits) {
    return getDouble(amount, digits, true);
  }

  /**
   * check if the value is zero
   * @param value the value to be judged
   * @param digits the digits to be rounded
   * @return whether the value is zero
   */
  public static boolean isZero(double value, int digits) {
    return round(value, digits) == 0;
  }

  /**
   * get the minimum long number among the three long numbers
   * @param a long
   * @param b long
   * @param c long
   * @return long the minimum long value
   */
  public static long minimum(long a, long b, long c) {
    return NumberUtils.minimum(a,b,c);
  }

  /**
   * get the minimum int number among the three int numbers
   * @param a int
   * @param b int
   * @param c int
   * @return int the minimum int value
   */
  public static int minimum(int a, int b, int c) {
    return NumberUtils.minimum(a,b,c);
  }

  /**
   * get the minimum double number among the three double numbers
   * @param a double
   * @param b double
   * @param c double
   * @return double the minimum double value
   */
  public static double minimum(double a, double b, double c){
    return (a<b?a:b)<c?(a<b?a:b):c;
  }

  /**
   * get the minimum float number among the three float numbers
   * @param a float
   * @param b float
   * @param c float
   * @return float the minimum float value
   */
  public static float minimum(float a,float b,float c){
    return (a<b?a:b)<c?(a<b?a:b):c;
  }

  /**
   * get the maximum long number among the three long numbers
   * @param a long
   * @param b long
   * @param c long
   * @return long the maximum long value
   */
  public static long maximum(long a, long b, long c) {
    return NumberUtils.maximum(a,b,c);
  }

  /**
   * get the maximum int number among the three int numbers
   * @param a int
   * @param b int
   * @param c int
   * @return int the maximum int value
   */
  public static int maximum(int a, int b, int c) {
    return NumberUtils.maximum(a,b,c);
  }

  /**
   * get the maximum double number among the three double numbers
   * @param a double
   * @param b double
   * @param c double
   * @return double the maximum double value
   */
  public static double maximum(double a, double b, double c){
    return (a>b?a:b)>c?(a>b?a:b):c;
  }

  /**
   * get the maximum float number among the three float numbers
   * @param a float
   * @param b float
   * @param c float
   * @return float the maximum float value
   */
  public static float maximum(float a,float b,float c){
    return (a>b?a:b)>c?(a>b?a:b):c;
  }

  /**
   * get the minimum int number in an int array
   * @param intArray int[]
   * @return int the minimum int value in the int array
   */
  public static int minimum(int[] intArray){
    int min=Integer.MAX_VALUE;
    for(int i=0;i<intArray.length;i++){
      if(intArray[i]<min){
        min=intArray[i];
      }
    }
    return min;
  }

  /**
   * get the minimum long number in a long array
   * @param longArray long[]
   * @return long the minimum long value in a long array
   */
  public static long minimum(long[] longArray){
    long min=Long.MAX_VALUE;
    for(int i=0;i<longArray.length;i++){
      if(longArray[i]<min){
        min=longArray[i];
      }
    }
    return min;
  }

  /**
   * get the minimum double number in a double array
   * @param doubleArray double[]
   * @return double the minimum double value in a double array
   */
  public static double minimum(double[] doubleArray){
    double min=Double.POSITIVE_INFINITY;
    for(int i=0;i<doubleArray.length;i++){
      if(doubleArray[i]<min){
        min=doubleArray[i];
      }
    }
    return min;
  }

  /**
   * get the minimum float number in a float array
   * @param floatArray float[]
   * @return float the minimum float value in a float array
   */
  public static float minimum(float[] floatArray){
    float min=Float.POSITIVE_INFINITY;
    for(int i=0;i<floatArray.length;i++){
      if(floatArray[i]<min){
        min=floatArray[i];
      }
    }
    return min;
  }

  /**
   * get the maximum int number in an int array
   * @param intArray int[]
   * @return int the maximum int value in an int array
   */
  public static int maximum(int[] intArray){
    int max=Integer.MIN_VALUE;
    for(int i=0;i<intArray.length;i++){
      if(intArray[i]>max){
        max=intArray[i];
      }
    }
    return max;
  }

  /**
   * get the maximum long number in a long array
   * @param longArray long[]
   * @return long the maximum long value in a long array
   */
  public static long maximum(long[] longArray){
    long max=Long.MIN_VALUE;
    for(int i=0;i<longArray.length;i++){
      if(longArray[i]>max){
        max=longArray[i];
      }
    }
    return max;
  }

  /**
   * get the maximum double number in a double array
   * @param doubleArray double[]
   * @return double the maximum double number in a double array
   */
  public static double maximum(double[] doubleArray){
    double max=Double.NEGATIVE_INFINITY;
    for(int i=0;i<doubleArray.length;i++){
      if(doubleArray[i]>max){
        max=doubleArray[i];
      }
    }
    return max;
  }

  /**
   * get the maximum float number in a float array
   * @param floatArray float[]
   * @return float the maximum float value in a float array
   */
  public static float maximum(float[] floatArray){
    float max=Float.NEGATIVE_INFINITY;
    for(int i=0;i<floatArray.length;i++){
      if(floatArray[i]>max){
        max=floatArray[i];
      }
    }
    return max;
  }

  /**
   * Convert a String to a double
   * @param inStr String
   * @return double
   */
  public static double toDouble(String inStr) {
    if (inStr == null) {
      return 0;
    }
    else {
      return Double.valueOf(inStr).doubleValue();
    }
  }

  /**
   * Convert a String to a float
   * @param inStr String
   * @return float
   */
  public static float toFloat(String inStr) {
    if (inStr == null) {
      return 0;
    }
    else {
      return Float.valueOf(inStr).floatValue();
    }
  }

  /**
   * Convert a String to a int
   * @param inStr String
   * @return int
   */
  public static int toInteger(String inStr) {
    if (inStr == null) {
      return 0;
    }
    else {
      return new Integer(inStr).intValue();
    }
  }

  /**
   * Convert a String to a long
   * @param inStr String
   * @return long
   */
  public static long toLong(String inStr) {
    if (inStr == null) {
      return 0;
    }
    else {
      return Long.valueOf(inStr).longValue();
    }
  }

  /**
   * Round up to 0 or 5 at the last digit.
   * @param value double
   * @param digits int
   * @return double
   */
  public static double round5(double value,int digits){
    int multiple = (int)Math.pow(10,digits);
    int mutiValue = (int)(NumberUtils.truncate(value,digits)*multiple);
    int mod=mutiValue%10;
    mutiValue -= mod;
    if(mod>0&&mod<5) mod = 5;
    else if(mod>5) mod = 10;
    return (mutiValue+mod)/(double)multiple;
  }

  /**
   * round up
   * @param value double
   * @param digits int
   * @return double
   */
  public static double roundUp(double value, int digits){
    if(value==truncate(value,digits)) return value;
    else return truncate(value,digits)+(1.0/Math.pow(10,digits));
  }

  /**
   * round up to multiple of the meta-value
   * @param value double
   * @param digits int
   * @param metavalue double
   * @return double
   */
  public static double roundMultiple(double value, int digits,double metavalue){
    int upMultiple = (int)roundUp(round(value,digits)/metavalue,0);
    return (upMultiple<=0?1:upMultiple)*metavalue;
  }

  /**
   * transform Long to BigDecimal
   * @param value Long
   * @return BigDecimal
   */
  public static BigDecimal toBigDecimal(Long value){
    if(value==null) return null;
    return new BigDecimal(value.longValue());
  }

}
