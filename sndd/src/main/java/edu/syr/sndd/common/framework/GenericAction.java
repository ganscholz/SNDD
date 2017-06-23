/**
 * Class Name:   GenericAction
 * Copyright:    Copyright (c) 2005
 * Date:         11/01/2005
 * Company:      Syracuse University
 * @author       Jinping xu
 * @version      1.0
 * <p>
 * Description:  The class used to extends struts action,add our own method to deal with business layer.
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.framework;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.syr.sndd.common.util.AppConstant;

public abstract class GenericAction extends Action {
    
    /**
     * The place to write our own business logic
     *
     * @param mapping
     *            the ActionMapping
     * @param form
     *            the ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return the ActionForward
     * @throws java.lang.Exception
     *             throw this exception when there is an errror occurred during
     *             the processing
     */
    public abstract ActionForward process(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception;
    
    /**
     * Override the execute method to invoke our own business method and
     * integrade the permission check function in the action
     *
     * @param mapping
     *            the ActionMapping
     * @param form
     *            the ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return the ActionForward
     * @throws java.lang.Exception
     *             throw this exception when there is an error occurred in the
     *             processing
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            request.setAttribute(AppConstant.LOGIN_END_URI, request.getRequestURI());
            /**
             * when needed, we will turn on checking repeated submit if
             * (checkRepeatedSubmit(request)) { throw new
             * AppException(AppException.REREATED_SUBMIT); }
             */
            ActionForward forward = process(mapping, form, request, response);
            request.setAttribute(AppConstant.ACTION_FORM, form);
            return forward;
        } catch (Exception ex) {
            return ExceptionHandler.process(ex, request, response);
        }
    }
    
    /**
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
        /*
         * private boolean checkRepeatedSubmit(HttpServletRequest request)throws
         * Exception { boolean result = false; String checkRepeated = (String)
         * request.getAttribute("has_check_repeated"); if
         * ("true".equals(checkRepeated)) return false; String sCurPageToken =
         * request.getParameter("PageToken"); if (sCurPageToken != null) { HashSet
         * hPageToken = (HashSet)
         * request.getSession(true).getAttribute("PageTokens"); if (hPageToken !=
         * null) { synchronized (hPageToken) { if
         * (hPageToken.contains(sCurPageToken)) { hPageToken.remove(sCurPageToken);
         * request.setAttribute("has_check_repeated", checkRepeated); } else {
         * result = true; } } } else { result = true; } } return result; }
         */
}
