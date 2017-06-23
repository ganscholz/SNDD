/**
 * Class Name:   AppException
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/2008
 * Company:      ExportSolution, Inc.
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  The application exception to be throwned when there are some error
 * 					occurred during the application's running time.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.framework;

public class AppException extends GenericException {
	private static final long serialVersionUID = 1234567890L;

	/**
	 * @param strError: the exception message
	 * @param aobjThrow: the source exception
	 */
	public AppException(String strError, Throwable aobjThrow) {
		super(strError, aobjThrow);
	}
        
        /**
	 * @param strError: the exception message
	 */
	public AppException(String strError) {
		super(strError);
	}
}
