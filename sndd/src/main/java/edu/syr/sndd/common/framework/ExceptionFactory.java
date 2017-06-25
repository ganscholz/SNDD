/**
 * 
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  Excepeption parser
 * </p>
 */

package edu.syr.sndd.common.framework;

public class ExceptionFactory {
	/**
	 * Convert a common error to the System customized GenericException. To be
	 * updated...
	 * 
	 * @param e an exception to catch.
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
