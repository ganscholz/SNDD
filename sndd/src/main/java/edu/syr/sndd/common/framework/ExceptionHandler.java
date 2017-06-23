/**
 * Class Name:   ExceptionHandler
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/2008
 * Company:      ExportSolution, Inc.
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:  The customized exception handler used to deal with exceptions in the struts framework.
 *					We defined our own error type code here and our exception error page to show the error informations.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */
package edu.syr.sndd.common.framework;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.action.*;

public class ExceptionHandler extends org.apache.struts.action.ExceptionHandler {

	/**
	 * the message page used to show messages
	 */
	private static final String MSG_PAGE = "/web/jsp/common/message.jsp";

	/**
	 * the error page used to show the error information
	 */
	private static final String ERROR_PAGE = "/web/jsp/common/error.jsp";

	/**
	 * When there is an exception occurred, recover the removed PageToken
	 * exclude the condition that the page has been submit multiple times
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	/*public static void addPageToken(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		HashSet hPageToken = (HashSet) session.getAttribute("PageTokens");
		String sCurPageToken = request.getParameter("PageToken");
		if (hPageToken != null && sCurPageToken != null) {
			synchronized (hPageToken) {
				if (!hPageToken.contains(sCurPageToken))
					hPageToken.add(sCurPageToken);
			}
		}
	}*/

	/**
	 * Override the execute method to provide our own exception handling code
	 * 
	 * @param ex
	 *            the throwed exception
	 * @param ae
	 *            the exception config
	 * @param mapping
	 *            the action mappings
	 * @param formInstance
	 *            the form instance
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return the ActionForward
	 * @throws ServletException
	 *             throw this exception when there is any exception occurred
	 *             during the exception processing
	 */
	public ActionForward execute(Exception ex, ExceptionConfig ae,
			ActionMapping mapping, ActionForm formInstance,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		return process(ex, request, response);
	}

	/**
	 * Override the execute method to provide our own exception handling code
	 * 
	 * @param ex
	 *            the throwed exception
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return the ActionForward
	 * @throws ServletException
	 *             throw this exception when there is any exception occurred
	 *             during the exception processing
	 */
	public static ActionForward process(Exception ex,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		//addPageToken(request);
		ActionForward forward = null;
		if (ex instanceof AppException) {
			forward = handleAppException(request, response, (AppException) ex);
		} else if (ex instanceof SysException) {
			forward = handleSysException(request, response, (SysException) ex);
		} else {	
                    forward = handleError(request, response, ex);
		}
		return forward;
	}

	/**
	 * Handle the customized application exception
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param ae
	 *            the customized exception
	 * @return the ActionForward
	 */
	protected static ActionForward handleAppException(
			HttpServletRequest request, HttpServletResponse response,
			AppException ae) {
		request.setAttribute("p_msg", ae.getDescription());
		request.setAttribute("p_detail", getExceptionMsg(ae));
		return new ActionForward(MSG_PAGE, false);
	}

	/**
	 * Handle the customized system exception
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param se
	 *            the customized system exception
	 * @return the ActionForward
	 */
	protected static ActionForward handleSysException(
			HttpServletRequest request, HttpServletResponse response,
			SysException se) {
		request.setAttribute("p_msg", se.getDescription());
		request.setAttribute("p_detail", getExceptionMsg(se));
		return new ActionForward(ERROR_PAGE, false);
	}

	/**
	 * output the error message to the error page
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @param exception
	 *            Exception
	 * @return the ActionForward
	 */
	protected static ActionForward handleError(HttpServletRequest request,
			HttpServletResponse response, Exception exception) {
		request.setAttribute("p_msg", exception.getMessage());
		request.setAttribute("p_detail", getExceptionMsg(exception));
		return new ActionForward(ERROR_PAGE, false);
	}

	/**
	 * Get the message from the exception
	 * 
	 * @param e
	 *            the exception
	 * @return the message in the exception
	 */
	protected static String getExceptionMsg(Throwable e) {
		String result = null;
		StringWriter out = new StringWriter();
		try {
			e.printStackTrace(new PrintWriter(out));
			result = out.toString();
		} catch (Exception e1) {
		} finally {
			try {
				out.close();
			} catch (Exception e2) {
			}
		}
		return result;
	}
}
