/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.syr.sndd.common.util;

import edu.syr.sndd.common.framework.ExceptionFactory;
import edu.syr.sndd.fitSN.SNGenerator;
import edu.syr.sndd.fitSN.SNParameters;
import java.util.HashMap;
import java.io.*;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Because our original GSD data are in Excel, this utility is to read the original data and
 * possibly output the data to Excel format. It is an extention to HSSF package. 
 * @author Stoney Q. Gan
 */
public class ExcelUtil {

    private static HSSFCellStyle titleCellStyle = null;
    private static HSSFCellStyle shadeTitleCellStyle = null;
    private static HSSFCellStyle shadeDataCellStyle = null;
    private static HSSFCellStyle normalDataCellStyle = null;
    
    /**
     *  A util method to create a new Excel file
     * @param outfile
     * @return
     * @throws Exception
     */
    public static HSSFWorkbook createNew(String outfile) throws Exception {

      FileInputStream in = new FileInputStream(outfile);
      HSSFWorkbook wb = new HSSFWorkbook(in);
 
        try
        {
          HSSFFont titleCellFont = wb.createFont();
          titleCellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
          titleCellFont.setFontName(HSSFFont.FONT_ARIAL);
          titleCellFont.setFontHeightInPoints((short) 10);

          HSSFFont shadeTitleCellFont = wb.createFont();
          shadeTitleCellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
          shadeTitleCellFont.setFontName(HSSFFont.FONT_ARIAL);
          shadeTitleCellFont.setFontHeightInPoints((short) 12);
          
          HSSFFont dataCellFont = wb.createFont();
          dataCellFont.setFontHeight((short)10);

          titleCellStyle = wb.createCellStyle();
          titleCellStyle.setFont(titleCellFont);

          shadeTitleCellStyle = wb.createCellStyle();
          shadeTitleCellStyle.setFont(shadeTitleCellFont);
          shadeTitleCellStyle.setFillBackgroundColor(HSSFCellStyle.SPARSE_DOTS);

          shadeDataCellStyle = wb.createCellStyle();
          shadeDataCellStyle.setFillBackgroundColor(HSSFCellStyle.SPARSE_DOTS);
          shadeDataCellStyle.setFont(dataCellFont);

          normalDataCellStyle = wb.createCellStyle();
          normalDataCellStyle.setFont(dataCellFont);
        }
        catch (Exception e)
        {
          throw ExceptionFactory.parse(e);
        }
        
        in.close();
        return wb;
    }

    public static void writeAndClose(HSSFWorkbook wb, FileOutputStream out) throws Exception {
          wb.write(out);
          out.close();
    }

    private static void createTitleRow(HSSFSheet sheet, String strRowTitle, String[] strTitle){
          HSSFRow row = sheet.createRow(1);
          HSSFCell cell = row.createCell(0);
          cell.setCellStyle(shadeTitleCellStyle);

          row = sheet.createRow(1);
          for (int i=0; i< strTitle.length; i++){
              HSSFCell c = row.createCell(i);
              c.setCellStyle(titleCellStyle);
              c.setCellValue(strTitle[i]);
             // sheet.setColumnWidth(i, AppConstant.COLUMN_WIDTH[i]);
          }

    }
 
    public static void appendResultToExcel(HSSFSheet sheet, String sampleName, double sampleValues[], 
                                int numCols, int rowNum){

        HSSFRow row = sheet.createRow(rowNum);

        if (rowNum % 2 == 0)
            row.setRowStyle(shadeDataCellStyle);
        else
            row.setRowStyle(normalDataCellStyle);

        for (int k=0; k< numCols; k++){
            HSSFCell c = row.createCell(k);
            c.setCellValue(sampleValues[k]);
        }             
    }

    public static synchronized void appendSNResultToExcel(String SNResultFileName, List<SNParameters> asnps) throws Exception{                
            Sheet SNSheet;
          
            FileInputStream in = new FileInputStream(SNResultFileName);
            //HSSFWorkbook wb = createNew(SNResultFileName);
            
            Workbook wb = new XSSFWorkbook(in);
            in.close();
            
            // assume the file exist
            SNSheet = wb.getSheetAt(0);

            
            Row myRow;
            Cell myCell;
            int rows = SNSheet.getPhysicalNumberOfRows(); 
            myRow = SNSheet.createRow(rows);
           
            Iterator itr = asnps.iterator();
            int i=0;
            int j=0;
            while(itr.hasNext()){
                SNParameters snp = (SNParameters) itr.next();
                if (i == 0){
                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(snp.getSampleName());
                    i++;
                }
                  
                double omega = snp.getOmega();
                double xi = snp.getXi();
                double alpha = snp.getAlpha();
                
                myCell = myRow.createCell(j++);
                myCell.setCellValue(omega);
                
                myCell = myRow.createCell(j++);
                myCell.setCellValue(xi);
                
                myCell = myRow.createCell(j++);
                myCell.setCellValue(alpha);
                
                myCell = myRow.createCell(j++);
                myCell.setCellValue(SNGenerator.computeMean(omega, xi, alpha));
                
                myCell = myRow.createCell(j++);
                myCell.setCellValue(SNGenerator.computeVariance(omega, xi, alpha));
                
                myCell = myRow.createCell(j++);
                myCell.setCellValue(SNGenerator.computeSkewness(omega, xi, alpha));
                
                /*
                myCell = myRow.createCell(j++);
                myCell.setCellValue(snp.getfDataSN());
                
                myCell = myRow.createCell(j++);
                myCell.setCellValue(snp.getDataMax());
                */
            }
            
            FileOutputStream out=null;
            try {
                out = new FileOutputStream(SNResultFileName);
                wb.write(out);
            } finally {
                if (out != null){
                    out.flush();
                    out.close();
                }
            }
    }
    public static synchronized void appendAllSNResultToExcel(String SNResultFileName, List<List<SNParameters>> asnps) throws Exception{                
            Sheet SNSheet;
          
            FileInputStream in = new FileInputStream(SNResultFileName);
            //HSSFWorkbook wb = createNew(SNResultFileName);
            
            Workbook wb = new XSSFWorkbook(in);
            in.close();
            
            // assume the file exist
            SNSheet = wb.getSheetAt(0);
            
            Row myRow;
            Cell myCell;
            int rows = SNSheet.getPhysicalNumberOfRows(); 
           
            Iterator snItr = asnps.iterator();
            while (snItr.hasNext()){
                List<SNParameters> sns = (List<SNParameters>)snItr.next();
                Iterator itr = sns.iterator();
                int i=0;
                int j=0;
                myRow = SNSheet.createRow(rows++);
                while(itr.hasNext()){
                    SNParameters snp = (SNParameters) itr.next();
                    if (i == 0){
                        myCell = myRow.createCell(j++);
                        myCell.setCellValue(snp.getSampleName());
                        i++;
                    }

                    double omega = snp.getOmega();
                    double xi = snp.getXi();
                    double alpha = snp.getAlpha();

                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(omega);

                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(xi);

                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(alpha);

                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(SNGenerator.computeMean(omega, xi, alpha));

                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(SNGenerator.computeVariance(omega, xi, alpha));

                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(SNGenerator.computeSkewness(omega, xi, alpha));

                    /*
                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(snp.getfDataSN());

                    myCell = myRow.createCell(j++);
                    myCell.setCellValue(snp.getDataMax());
                    */
                }
            }
            
            FileOutputStream out=null;
            try {
                out = new FileOutputStream(SNResultFileName);
                wb.write(out);
            } finally {
                if (out != null){
                    out.flush();
                    out.close();
                }
            }
    }
    
    public static void  main(String[] argv){
        try {
        	edu.syr.sndd.common.util.ExcelUtil.createNew("outputTest.xls");
        } catch (Exception e){
        }
    }
}
