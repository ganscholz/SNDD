/**
 * Class Name:   GenericException
 * Copyright:    Copyright (c) 2009
 * Date:         09/24/2008
 * Company:      ExportSolution, Inc.
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  Generic expection that provides some basic methods used to retrieve the exception information.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.framework;

import java.util.*;

public class GenericException extends Exception {
    private static final long serialVersionUID = 1234567890L;
    
    /** extra error information */
    private String strAdditionalInfo = null;
    
    /** original error */
    private Throwable objRootException = null;
    
    /**
     * Initialize an exception with the error code an original error information
     *
     * @param pNestedException
     *            the original error
     */
    public GenericException(Throwable aobjNestedException) {
        objRootException = aobjNestedException;
    }
    
    /**
     * Initialize an exception with the error code an original error information
     *
     * @param pAddInfo
     *            the extra error information
     */
    public GenericException(String astrAddiontionalInfo) {
        strAdditionalInfo = astrAddiontionalInfo;
    }
    
    /**
     * Initialize an exception with the error code,extra error information and
     * the original error
     *
     * @param pAddInfo
     *            the extra error information
     * @param pNestedException
     *            the original error
     */
    public GenericException(String astrAddiontionalInfo, Throwable aobjNestedException) {
        strAdditionalInfo = astrAddiontionalInfo;
        objRootException = aobjNestedException;
    }
    
    /**
     * Get the error information and return the current error descripton
     *
     * @return the error description
     */
    public String getDescription() {
        String strMessage = "";
        
        if (strAdditionalInfo != null) {
            strMessage = strMessage + "(" + strAdditionalInfo + ") ";
        }
        
        if (objRootException != null) {
            strMessage = strMessage + "Nested Exception is:"
                    + objRootException.toString();
        }
        
        return strMessage;
    }
    
    /**
     * Get the error information and return the current error message
     *
     * @return the error message
     */
    public String getMessage() {
        String strMessage = "";
        
        if (strAdditionalInfo != null) {
            strMessage = strMessage + "(" + strAdditionalInfo + ") ";
        }
        
        if (objRootException != null) {
            strMessage = strMessage + objRootException.getMessage();
        }
        
        return strMessage;
        
    }
    
    public String getAddInfo() {
        return strAdditionalInfo;
    }
    
}
