/**
 * Class Name:   GenericVO
 * Copyright:    Copyright (c) 2008
 * Date:         09/26/08
 * Company:      exportSolution, Inc
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  The basic class for value object class to inheritence for future extending.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.framework;

import java.io.Serializable;

public class GenericVO implements Serializable {
	private static final long serialVersionUID = 1234567890L;

	public GenericVO() {
	}

	/**
	 * output formatted string
	 * @return String
	 */
	public String toString() {
		return "";
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param obj Object
	 * @return boolean
	 */
	public boolean equals(Object obj) {
		if (!(obj.getClass().isInstance(this))) {
			return false;
		}
		return true;
	}
}
