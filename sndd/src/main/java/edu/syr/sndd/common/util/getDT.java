/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.syr.sndd.common.util;
/**
 *
 * @author eximlean
 */

public class getDT {

    /**
     * get the minimum of three integers.
     * @param a integer a;
     * @param b integer b;
     * @param c integer c;
     * @return min of three integers
     */
    private static int Minimum(int a, int b, int c) {
        int mi;

        mi = a;
        if (b < mi) {
            mi = b;
        }
        if (c < mi) {
            mi = c;
        }
        return mi;

    }

    /**
     * get larger value of two integers
     * @param x: integer x;
     * @param y integer y;
     * @return max(x, y);
     */
    private static int max(int x, int y) {
        if (x > y)
            return x;

        return y;
    }

    /**
     * An implementation of Levenshtein Distance. Modified a source code from
     * @param strS the first string;
     * @param strT the second string;
     * @return Levenshtein Distance;
     */
    private static int LD(String strS, String strT) {
        int d[][]; // matrix
        int n; // length of s
        int m; // length of t
        int i; // iterates through s
        int j; // iterates through t
        char s_i; // ith character of s
        char t_j; // jth character of t
        int cost; // cost

        n = strS.length();
        m = strT.length();

        d = new int[n + 1][m + 1];

        // row one in the matrix
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        // col one in the matrix
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }


        for (i = 1; i <= n; i++) {
            s_i = strS.charAt(i - 1);

            for (j = 1; j <= m; j++) {
                t_j = strT.charAt(j - 1);

                //
                if (s_i == t_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                // Step 6

                d[i][j] =
                        Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j -
                                1] + cost);
            }
        }

        return d[n][m];

    }

    /**
     * get degree of truth: DT = 1 - LD/max(strS.length(), strT.length);
     * @param strS
     * @param strT
     * @return the degree of truth;
     */
    public static double getDTValue(String strS, String strT) {
        double dt;
        int ld;

        if (strS == null && strT == null)
            return 1; // they are actually the same;

        if (strS == null )
            ld = strT.length();

        if (strT == null)
            ld = strS.length(); // strS cannot be empty;

        strS = strS.toUpperCase();
        strT = strT.toUpperCase();

        ld = LD(strS, strT);

        dt = (double)(1.0 - (double)ld / max(strS.length(), strT.length()));

        return dt;
    }

}

