/**
 * Class Name:   SysException
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/2008
 * Company:      ExportSolution, Inc.
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  The customized system exception to be thrown when there is an
 * 					system level exception occurred.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.framework;

public class SysException extends GenericException {
	private static final long serialVersionUID = 1234567890L;
/**
	 * @param strError: the exception message
	 * @param aobjThrow: the source exception
	 */
	public SysException(String strError, Throwable aobjThrow) {
		super(strError, aobjThrow);
	}

	
	/**
	 * Initialize a SysException with the error code and the original exception
	 * 
	 * @param aobjThrow: the original exception
	 */
	public SysException(Throwable aobjThrow) {
		super(aobjThrow);
	}
                
        /**
	 * @param strError: the exception message
	 */
	public SysException(String strError) {
		super(strError);
	}
}
