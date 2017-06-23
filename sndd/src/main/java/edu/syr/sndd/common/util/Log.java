/**
 * Class Name:   Log
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/2008
 * Company:      ExportSolution, Inc.
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:	 Common Log Util
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.util;

import org.apache.log4j.*;

public class Log {
	private static Logger logger2;

	public static void info(Class classFrom, String object) {
		logger2 = Logger.getLogger(classFrom);
		logger2.info(object);
	}

	public static void info(Class classFrom, String object, Throwable ex) {
		logger2 = Logger.getLogger(classFrom);
		logger2.info(object, ex);
	}

	public static void debug(Class classFrom, String object) {
		logger2 = Logger.getLogger(classFrom);
		logger2.debug(object);
	}

	public static void debug(Class classFrom, String object, Throwable ex) {
		logger2 = Logger.getLogger(classFrom);
		logger2.debug(object, ex);
	}

	public static void error(Class classFrom, String object) {
		logger2 = Logger.getLogger(classFrom);
		logger2.error(object);
	}

	public static void error(Class classFrom, String object, Throwable ex) {
		logger2 = Logger.getLogger(classFrom);
		logger2.error(object, ex);
	}
}
