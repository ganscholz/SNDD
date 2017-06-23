/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.syr.sndd.readData;

/**
 *
 * @author Stoney Q Gan
 */
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import edu.syr.sndd.common.util.*;

/**
 * Read the SN partitioned data into a 2-d array. It is assumed that the data file in MS Excel format.
 * @author Stoney Q. Gan
 *
 */
public class ReadSNData {
   
   public static String [][] readInputData (String fileName) throws Exception {      
     
     int numCols=30;
     String sampleValues[][] = null;
     try {
        //POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));

        Workbook wb = new XSSFWorkbook(fileName);
        Sheet sheet = wb.getSheetAt(0);
        Row row;

        int i, rows; // No of rows

        rows = sheet.getPhysicalNumberOfRows();
        System.out.println("this is the number of rows: " + rows);

        if (rows < 1){
            throw new Exception("This is a blank spreadsheet!");
        }

        if (rows == 1){
            throw new Exception("There is no data in this spreadsheet!");
        }

        // read the first row for the column titles
        i=0;
        row = sheet.getRow(i);
        sampleValues = new String[rows-1][numCols]; // first row is headings
 
        // read each row, do search and write result to an excel file.
        for(int r = 1; r < rows; r++) {
            row = sheet.getRow(r);
            //System.out.print("this is row: " + r);
            if(row != null) { 
                for (int c = 0; c< numCols; c++) {
                    if (row.getCell(c) == null || row.getCell(c).toString().trim().isEmpty())
                        sampleValues[r-1][c] = "0";
                    else
                        sampleValues[r-1][c] = row.getCell(c).toString();
                    //System.out.println(" row: " + r + " col: " + c + " value " + sampleValues[r][c]);
                    //if ( c == 0) {
                      //   System.out.println(" sample name " + sampleValues[r][c]);
                    //}
                    //sampleValues[c] = Double.parseDouble(row.getCell(c).getStringCellValue());
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
}
