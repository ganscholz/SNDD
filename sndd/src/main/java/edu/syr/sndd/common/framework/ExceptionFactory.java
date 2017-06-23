/**
 * Class Name:   ExceptionFactory
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/2008
 * Company:      ExportSolution, Inc.
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  Excepeption parser
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.framework;

public class ExceptionFactory {
	/**
	 * Convert a common error to the System customized GenericException. To be
	 * updated...
	 * 
	 * @param e
	 *            a common error
	 * @return our customized GenericException
	 */
	public static GenericException parse(Exception e) {
		GenericException objException = null;
		if (e instanceof AppException ) {
                    objException= (AppException)e;
		} else if(e instanceof SysException){
                    objException = (SysException)e;
                }
                else{
                     objException = new SysException(e.getMessage(), e);
                }
                    
		return objException;
	}
}
