/**
 * Class Name:   StringUtils
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/08
 * Company:      exportSolution, Inc
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  This is the utility class to manipulate strings.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.util;

import java.util.StringTokenizer;
import org.apache.commons.lang.*;
import org.apache.commons.lang.math.NumberUtils;

public class StringUtils {
    private StringUtils() {
    }
    
    /**
     * <p>Replaces all occurances of a String within another String.</p>
     *
     * <p>A <code>null</code> reference passed to this method is a no-op.</p>
     *
     * <pre>
     * StringUtils.replace(null, *, *)        = null
     * StringUtils.replace("", *, *)          = ""
     * StringUtils.replace("aba", null, null) = "aba"
     * StringUtils.replace("aba", null, null) = "aba"
     * StringUtils.replace("aba", "a", null)  = "aba"
     * StringUtils.replace("aba", "a", "")    = "aba"
     * StringUtils.replace("aba", "a", "z")   = "zbz"
     * </pre>
     *
     * @param text  text to search and replace in, may be null
     * @param repl  the String to search for, may be null
     * @param with  the String to replace with, may be null
     * @return the text with any replacements processed,
     *  <code>null</code> if null String input
     */
    
    /**
     * <p>Checks whether the <code>String</code> contains only
     * digit characters.</p>
     *
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str  the <code>String</code> to check
     * @return <code>true</code> if str contains only unicode numeric
     */
    public static boolean isDigits(String str){
        return NumberUtils.isDigits(str);
    }
    
    /**
     * <p>Checks whether the <code>String</code> is an Integer<p>
     * @param str String
     * @return boolean
     */
    public static boolean isInteger(String str){
        if(str!=null&&!str.equals("")&&isDigits(str)&&!str.startsWith("0")){
            return true;
        }
        return false;
    }
    
    /**
     * <p>Checks whether the String a valid Java number.</p>
     *
     * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
     * qualifier, scientific notation and numbers marked with a type
     * qualifier (e.g. 123L).</p>
     *
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str  the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     */
    public static boolean isNumber(String str){
        return NumberUtils.isNumber(str);
    }
    
    /**
     * <p>Checks whether the <code>String</code> is null<p>
     * @param str String
     * @return boolean
     */
    public static boolean isNull(String str){
        if(str==null){
            return true;
        }
        return false;
    }
    
    /**
     * <p>Checks whether the <code>String</code> is an empty String</p>
     * @param str String
     * @return boolean
     */
    public static boolean isEmpty(String str){
        if(str!=null&&str.trim().equals("")){
            return true;
        }
        return false;
    }
    
    /**
     * <p>Checks whether the <code>String</code> is empty or null</p>
     * @param str String
     * @return boolean
     */
    public static boolean isNullOrEmpty(String str){
        return isNull(str)||isEmpty(str);
    }
    
    /**
     * <p>Checks whether the <code>String</code> is null,
     * if it is null,convert it to an empty string</p>
     * @param str String
     * @return String
     */
    public static String nullToEmpty(String str){
        if(str==null){
            return "";
        }
        return str;
    }
    
    /**
     * <p>Checks whether the <code>String</code> is empty,
     * if it is empty, convert the empty string to null</p>
     * @param str String
     * @return String
     */
    public static String emptyToNull(String str){
        if(str==null||str.trim().equals("")){
            return null;
        }
        return str;
    }
    
    // HTML and XML
    //--------------------------------------------------------------------------
    /**
     * <p>Escapes the characters in a <code>String</code> using HTML entities.</p>
     *
     * <p>
     * For example: <tt>"bread" & "butter"</tt> => <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
     * </p>
     *
     * <p>Supports all known HTML 4.0 entities, including funky accents.</p>
     *
     * @param str  the <code>String</code> to escape, may be null
     * @return a new escaped <code>String</code>, <code>null</code> if null string input
     *
     * @see #unescapeHtml(String)
     * @see </br><a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
     * @see </br><a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
     * @see </br><a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
     * @see </br><a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
     * @see </br><a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
     **/
    public static String escapeHtml(String str) {
        return StringEscapeUtils.escapeHtml(str);
    }
    
    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes. Supports HTML 4.0 entities.</p>
     *
     * <p>For example, the string "&amp;lt;Fran&amp;ccedil;ais&amp;gt;"
     * will become "&lt;Fran&ccedil;ais&gt;"</p>
     *
     * <p>If an entity is unrecognized, it is left alone, and inserted
     * verbatim into the result string. e.g. "&amp;gt;&amp;zzzz;x" will
     * become "&gt;&amp;zzzz;x".</p>
     *
     * @param str  the <code>String</code> to unescape, may be null
     * @return a new unescaped <code>String</code>, <code>null</code> if null string input
     * @see #escapeHtml(String)
     **/
    public static String unescapeHtml(String str) {
        return StringEscapeUtils.unescapeHtml(str);
    }
    
    /**
     * <p>Escapes the characters in a <code>String</code> using XML entities.</p>
     *
     * <p>For example: <tt>"bread" & "butter"</tt> =>
     * <tt>&amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;</tt>.
     * </p>
     *
     * <p>Supports only the four basic XML entities (gt, lt, quot, amp).
     * Does not support DTDs or external entities.</p>
     *
     * @param str  the <code>String</code> to escape, may be null
     * @return a new escaped <code>String</code>, <code>null</code> if null string input
     * @see #unescapeXml(java.lang.String)
     **/
    public static String escapeXml(String str) {
        return StringEscapeUtils.escapeXml(str);
    }
    
    /**
     * <p>Unescapes a string containing XML entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes.</p>
     *
     * <p>Supports only the four basic XML entities (gt, lt, quot, amp).
     * Does not support DTDs or external entities.</p>
     *
     * @param str  the <code>String</code> to unescape, may be null
     * @return a new unescaped <code>String</code>, <code>null</code> if null string input
     * @see #escapeXml(String)
     **/
    public static String unescapeXml(String str){
        return StringEscapeUtils.unescapeXml(str);
    }
    
    /**
     * <p>Escapes the characters in a <code>String</code> to be suitable to pass to
     * an SQL query.</p>
     *
     * <p>For example,
     * <pre>statement.executeQuery("SELECT * FROM MOVIES WHERE TITLE='" +
     *   StringEscapeUtils.escapeSql("McHale's Navy") +
     *   "'");</pre>
     * </p>
     *
     * <p>At present, this method only turns single-quotes into doubled single-quotes
     * (<code>"McHale's Navy"</code> => <code>"McHale''s Navy"</code>). It does not
     * handle the cases of percent (%) or underscore (_) for use in LIKE clauses.</p>
     *
     * see http://www.jguru.com/faq/view.jsp?EID=8881
     * @param str  the string to escape, may be null
     * @return a new String, escaped for SQL, <code>null</code> if null string input
     */
    public static String escapeSql(String str){
        return StringEscapeUtils.escapeSql(str);
    }
    
    // Java and JavaScript
    //--------------------------------------------------------------------------
    /**
     * <p>Escapes the characters in a <code>String</code> using Java String rules.</p>
     *
     * <p>Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     *
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str  String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string input
     */
    public static String escapeJava(String str) {
        return StringEscapeUtils.escapeJava(str);
    }
    
    /**
     * <p>Unescapes any Java literals found in the <code>String</code>.
     * For example, it will turn a sequence of <code>'\'</code> and
     * <code>'n'</code> into a newline character, unless the <code>'\'</code>
     * is preceded by another <code>'\'</code>.</p>
     *
     * @param str  the <code>String</code> to unescape, may be null
     * @return a new unescaped <code>String</code>, <code>null</code> if null string input
     */
    public static String unescapeJava(String str) {
        return StringEscapeUtils.unescapeJava(str);
    }
    
    /**
     * <p>Escapes the characters in a <code>String</code> using JavaScript String rules.</p>
     * <p>Escapes any values it finds into their JavaScript String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters <code>'\\'</code> and
     * <code>'t'</code>.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote must be escaped.</p>
     *
     * <p>Example:
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     * </p>
     *
     * @param str  String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string input
     */
    public static String escapeJavaScript(String str) {
        return StringEscapeUtils.escapeJavaScript(str);
    }
    
    /**
     * <p>Unescapes any JavaScript literals found in the <code>String</code>.</p>
     *
     * <p>For example, it will turn a sequence of <code>'\'</code> and <code>'n'</code>
     * into a newline character, unless the <code>'\'</code> is preceded by another
     * <code>'\'</code>.</p>
     *
     * @see #unescapeJava(String)
     * @param str  the <code>String</code> to unescape, may be null
     * @return A new unescaped <code>String</code>, <code>null</code> if null string input
     */
    public static String unescapeJavaScript(String str) {
        return StringEscapeUtils.unescapeJavaScript(str);
    }
    
    public static String[] split(String str,String splitor) throws Exception{
        if(str==null) throw new Exception();
        StringTokenizer st = new StringTokenizer(str,splitor);
        String[] strAry = new String[st.countTokens()];
        int i=0;
        while(st.hasMoreTokens()){
            strAry[i]=st.nextToken();
            i++;
        }
        return strAry;
    }
    
    /**
     * <p>Generate a javascript array from a two dimensional Java String Array
     * @param sValue String[][] the java String array used to generate the
     *  javascript array
     * @return String a javascript array string
     */
    public static String generateJSArray(String[][] sValue) {
        if (sValue == null) {
            sValue = new String[0][0];
        }
        StringBuffer buff = new StringBuffer(4096);
        buff.append("new Array(");
        String[] ss;
        String s;
        for (int i = 0; i < sValue.length; i++) {
            ss = sValue[i];
            if (ss == null) {
                ss = new String[0];
            }
            if (i > 0) {
                buff.append(",");
            }
            buff.append("new Array(");
            for (int j = 0; j < ss.length; j++) {
                if (j > 0) {
                    buff.append(",");
                }
                s = ss[j];
                if (s == null) {
                    s = "&nbsp;";
                } else if ("".equals(s.trim())) {
                    s = "&nbsp;";
                }
                buff.append("\"" + escapeJava(s) + "\"");
            }
            buff.append(")");
        }
        buff.append(")");
        return buff.toString();
    }
    
    /**
     * to convert the string to a boolean value.
     * the following are the rules:
     * 1.the string equals "TRUE"(ignore case);
     * 2.the string equals "Y" (ignore case).
     * Example:
     * toBoolean("Y") = true
     * toBoolean("y") = true
     * toBoolean("true") = true
     * toBoolean("True") = true
     * toBoolean("") = false
     * toBoolean(null) = false
     * toBoolean("123") = false
     * @param str String
     * @return boolean return true if the string equals "TRUE" or "Y"(ignore case).
     */
    public static boolean toBoolean(String str){
        return "TRUE".equalsIgnoreCase(str)||"Y".equalsIgnoreCase(str);
    }
    
    /**
     *
     * @param str
     * @return int
     */
    public static int getStringLength(String str){
        int len;
        if(str=="" || str==null){
            len=0;
        } else{
            byte[] b = str.getBytes();
            len = b.length;
        }
        return len;
    }
        
    /**
     * Insert space to the long mail message string
     *
     * @param str
     * @return string
     */
    public static String processMessageStr(String str){
        StringBuffer sb = new StringBuffer();
        for (int i=0; i< str.length(); i++){
            sb.append(str.substring(i,i+1));
            if(str.substring(i,i+1).equals(",")){
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    
}
