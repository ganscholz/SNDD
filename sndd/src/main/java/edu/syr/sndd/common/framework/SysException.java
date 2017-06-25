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
