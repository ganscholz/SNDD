/**
 * Class Name:   Env
 * Copyright:    Copyright (c) 2008
 * Date:         09/24/2008
 * Company:      ExportSolution, Inc.
 * @author       Stoney Q. Gan
 * @version      1.0
 * <p>
 * Description:	Environment set up
 * </p>
 * <p>
 *       Please Maintain the Maintenance Section Below
 *  Date:
 *  Performed By:
 *  Reason:
 * </p>
 */

package edu.syr.sndd.common.util;

import java.util.*;

public class Env {
    public static String LOG_CONTEXT = "";
    public static String DEFAULT_LANGUAGE_ID = "001";
    public static int NUM_REC_OF_PAGE = 20;
    public static String APP_URL_PREFIX = "";
    public static String DATASOURCE_DEFAULT = "jdbc/leanexport";
    public static String DATASOURCE_OTHERS_1;
       
    static {
        try {
            System.out.println(AppConstant.BASE_CONFIG_FILE_NAME);

            //ResourceBundle prop = ResourceBundle.getBundle(AppConstant.BASE_CONFIG_FILE_NAME);

            /*
            LOG_CONTEXT = prop.getString("LOG_CONTEXT");
            DEFAULT_LANGUAGE_ID = prop.getString("DEFAULT_LANGUAGE_ID");
            NUM_REC_OF_PAGE = NumberUtils.toInteger(prop.getString("NUM_REC_OF_PAGE"));
            APP_URL_PREFIX = prop.getString("APP_URL_PREFIX");
            DATASOURCE_DEFAULT = prop.getString("DATASOURCE_DEFAULT");
            DATASOURCE_OTHERS_1 = prop.getString("DATASOURCE_OTHERS_1");
             */
            APP_URL_PREFIX="/exim";
            NUM_REC_OF_PAGE=20;
            LOG_CONTEXT="exim";
            DEFAULT_LANGUAGE_ID="001";
            DATASOURCE_DEFAULT="jdbc/leanexport";

         } catch (Exception e) {
            Log.error(Env.class,"Error occurred when load property file: ", e);
        }
    }
}
