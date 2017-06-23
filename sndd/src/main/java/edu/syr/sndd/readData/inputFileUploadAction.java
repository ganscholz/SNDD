/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.syr.sndd.readData;

/**
 *
 * @author sgan
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import edu.syr.sndd.common.util.AppConstant;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class inputFileUploadAction extends Action
{
  public ActionForward execute(
    ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response) throws Exception{
    inputFileUploadForm myForm = (inputFileUploadForm)form;

        // Process the FormFile
        FormFile myFile = myForm.getUploadFile();
        String contentType = myFile.getContentType();
        String fileName    = myFile.getFileName();
        int fileSize       = myFile.getFileSize();
        byte[] fileData    = myFile.getFileData();

        /*
        System.out.println("contentType: " + contentType);
        System.out.println("File Name: " + fileName);
        System.out.println("File Size: " + fileSize);
         */

        if (contentType.equalsIgnoreCase(AppConstant.EXCEL_TYPE_STRING) ||
                contentType.equals("application/vnd.ms-excel")) {
                //Get the servers upload directory real path name
            // String filePath = getServlet().getServletContext().getRealPath("/") +"upload";
            String filePath = "C:/Exim/searchHistory/";

            /* Save file on the server */
            DateFormat dateFormat = new SimpleDateFormat("-yyyyMMdd-HHmmss");
            java.util.Date date = new java.util.Date();
            String strDatetime = dateFormat.format(date);

            // attached date to original file name
            fileName += strDatetime;
                //Create file
            File fileToCreate = new File(filePath, fileName);
            //If file does not exists create file
            if(!fileToCreate.exists()){
              FileOutputStream fileOutStream = new FileOutputStream(fileToCreate);
              fileOutStream.write(myFile.getFileData());
              fileOutStream.flush();
              fileOutStream.close();
            }

            String outfile = filePath + "searchResult" + strDatetime;

            request.setAttribute(AppConstant.OUTPUT_FILENAME, outfile);

         return mapping.findForward("success");
      } else
          return mapping.findForward(AppConstant.FAILURE);

  }
}
