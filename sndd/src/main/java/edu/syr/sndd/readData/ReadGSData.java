/* (C) 2011 Stoney Q. Gan. All Rights Reserved
 This source code is a registered copyright of Stoney Q. Gan.
 Access to and copying of this source code is granted by the author provided users cite the web site as a reference.


 Modification History

 Date		Version		Author			Description
 ------------------------------------------------------------------------------
 2011-07-25	1.0		Stoney Q. Gan		Initial Version.
 ------------------------------------------------------------------------------
*/

package edu.syr.sndd.readData;

/**
 *
 * @author Stoney Q. Gan
 */

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import edu.syr.sndd.common.util.*;


public class ReadGSData {
    
/**
 * The original GSD data output from the instrument analysis were in Excel format. Consequentally, many of data processing are done with 
 * MS Excel format. This class reads the data from an MS Excel file into a 2-D array for further processing. Note: the number of columns
 * read is defined in the AppConstant.COL_TITLE.	
 * @param fileName input file name in excel.
 * @return double{}{}.
 * @throws Exception
 */
 public static String [][] readInputData (String fileName) throws Exception {      
     
     int numCols = AppConstant.COL_TITLE.length;
     String sampleValues[][] = null;
     try {
        Workbook wb = new XSSFWorkbook(fileName);
        Sheet sheet = wb.getSheetAt(0);
        Row row;

        int i, rows; // No of rows

        rows = sheet.getPhysicalNumberOfRows();
        System.out.println("this is the number of rows: " + rows);
        if (rows < 1){
            throw new Exception("This is a blank spreadsheet!");
        }

        // read the first row for the column titles
        i=0;
        row = sheet.getRow(i);
      
        if (rows == 1){
            throw new Exception("There is no data in this spreadsheet!");
        }

        sampleValues = new String[rows][numCols];
 
        // read each row, do search and write result to an excel file.
        for(int r = 0; r < rows; r++) {
            row = sheet.getRow(r);
            //System.out.print("this is row: " + r);
            if(row != null) {  
                for (int c = 0; c< numCols; c++) {
                    sampleValues[r][c] = row.getCell(c).toString();
                }
            }
        }
         
        // do not forget to save and close both input and outfile
        //ExcelUtil.writeAndClose();          
    } catch(Exception ioe) {
        throw ioe;
    }
    
    return sampleValues;
  }
 
 public static void main(String[] args) throws Exception {

     String inFileName = "C:\\dropbx\\Dropbox\\Stoney thesis docs\\chapter 2\\results-from-2012-03\\bosumtwi-proc-data.xlsx";
  
     String inputData[][] = ReadGSData.readInputData(inFileName);
     /*
     int R = inputData.length;
     int C = inputData[0].length;
     for (int i = 1; i< R; i++){
         for (int j =0; j< C ; j++) {
             System.out.print(inputData[i][j] + " ");  
         }
         System.out.println();
     }
      */
 }
}
