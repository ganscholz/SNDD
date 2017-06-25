package edu.syr.sndd.common.framework;

/**
 * A part of exception handling package. It is just a wrap of exception.
 * @author Stoney Q. Gan
 *
 */
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
