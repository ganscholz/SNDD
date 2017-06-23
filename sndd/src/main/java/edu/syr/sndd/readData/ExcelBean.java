package edu.syr.sndd.readData;
// *** this file is not in use!!!!

import edu.syr.sndd.common.framework.ExceptionFactory;
import edu.syr.sndd.common.framework.GenericException;
import javax.servlet.http.HttpServletResponse;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import jxl.*;


public class ExcelBean {
	private WritableWorkbook workbook = null;
	private WritableSheet sheet = null;
	private WritableCellFormat titleFmt = null;
	private WritableCellFormat colFmt = null;
	private WritableCellFormat stringFmt = null;
	private WritableCellFormat numFmt = null;
	private WritableCellFormat numFmt2 = null;
	private WritableCellFormat numFmt3 = null;
	private WritableCellFormat dateFmt = null;

	/**
	 * Create a excel bean
	 * 
	 * @param response
	 * @param sheetTitle
	 * @throws Exception
	 */
	public ExcelBean(HttpServletResponse response, String sheetTitle)
			throws Exception {
		response.setContentType("application/vnd.ms-excel");
		String fileName = "ReportExcel" + "-" + System.currentTimeMillis();
		response.addHeader("Content-disposition", "inline: filename = "
				+ fileName);
		workbook = Workbook.createWorkbook(response.getOutputStream());
		sheet = workbook.createSheet(sheetTitle, 0);
		setFormat();
	}

	/**
	 * Write and close
	 * 
	 * @throws GenericException
	 */
	public void writeAndClose() throws GenericException {
		try {
			workbook.write();
			workbook.close();
		} catch (Exception ex) {
			throw ExceptionFactory.parse(ex);
		}
	}

	/**
	 * Set format of thefonts
	 * 
	 * @throws GenericException
	 */
	private void setFormat() throws GenericException {
		try {
			// sheet title:Font
			WritableFont fnt = new WritableFont(WritableFont.TIMES);
			fnt.setBoldStyle(WritableFont.BOLD);
			fnt.setPointSize(10);
			titleFmt = new WritableCellFormat(fnt);
			titleFmt.setAlignment(Alignment.LEFT);

			// column title:Font
			fnt = new WritableFont(WritableFont.TIMES);
			fnt.setBoldStyle(WritableFont.BOLD);
			fnt.setPointSize(10);
			colFmt = new WritableCellFormat(fnt);
			colFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
			colFmt.setAlignment(Alignment.LEFT);

			// cell:string Font
			fnt = new WritableFont(WritableFont.TIMES);
			fnt.setBoldStyle(WritableFont.NO_BOLD);
			fnt.setPointSize(10);
			stringFmt = new WritableCellFormat(fnt);
			stringFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
			stringFmt.setAlignment(Alignment.LEFT);

			// cell:number Font
			NumberFormat nft = new NumberFormat("#,##0.00");
			fnt = new WritableFont(WritableFont.TIMES);
			fnt.setBoldStyle(WritableFont.NO_BOLD);
			fnt.setPointSize(10);
			numFmt = new WritableCellFormat(fnt, nft);
			numFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
			numFmt.setAlignment(Alignment.RIGHT);

			// cell:number Font 2
			nft = new NumberFormat("#,##0.000");
			fnt = new WritableFont(WritableFont.TIMES);
			fnt.setBoldStyle(WritableFont.NO_BOLD);
			fnt.setPointSize(10);
			numFmt2 = new WritableCellFormat(fnt, nft);
			numFmt2.setBorder(Border.ALL, BorderLineStyle.THIN);
			numFmt2.setAlignment(Alignment.RIGHT);

			// cell:number Font 3
			nft = new NumberFormat("#,##0.0000");
			fnt = new WritableFont(WritableFont.TIMES);
			fnt.setBoldStyle(WritableFont.NO_BOLD);
			fnt.setPointSize(10);
			numFmt3 = new WritableCellFormat(fnt, nft);
			numFmt3.setBorder(Border.ALL, BorderLineStyle.THIN);
			numFmt3.setAlignment(Alignment.RIGHT);

			// cell:date Font
			DateFormat dft = new DateFormat("yyyy-MM-dd");
			fnt = new WritableFont(WritableFont.TIMES);
			fnt.setBoldStyle(WritableFont.NO_BOLD);
			fnt.setPointSize(10);
			dateFmt = new WritableCellFormat(fnt, dft);
			dateFmt.setBorder(Border.ALL, BorderLineStyle.THIN);
			dateFmt.setAlignment(Alignment.LEFT);
		} catch (Exception ex) {
			throw ExceptionFactory.parse(ex);
		}
	}

	public WritableSheet getSheet() {
		return sheet;
	}

	public WritableCellFormat getColFmt() {
		return colFmt;
	}

	public void setColFmt(WritableCellFormat colFmt) {
		this.colFmt = colFmt;
	}

	public WritableCellFormat getDateFmt() {
		return dateFmt;
	}

	public void setDateFmt(WritableCellFormat dateFmt) {
		this.dateFmt = dateFmt;
	}

	public WritableCellFormat getNumFmt() {
		return numFmt;
	}

	public void setNumFmt(WritableCellFormat numFmt) {
		this.numFmt = numFmt;
	}

	public WritableCellFormat getNumFmt2() {
		return numFmt2;
	}

	public void setNumFmt2(WritableCellFormat numFmt2) {
		this.numFmt2 = numFmt2;
	}

	public WritableCellFormat getNumFmt3() {
		return numFmt3;
	}

	public void setNumFmt3(WritableCellFormat numFmt3) {
		this.numFmt3 = numFmt3;
	}

	public WritableCellFormat getStringFmt() {
		return stringFmt;
	}

	public void setStringFmt(WritableCellFormat stringFmt) {
		this.stringFmt = stringFmt;
	}

	public WritableCellFormat getTitleFmt() {
		return titleFmt;
	}

	public void setTitleFmt(WritableCellFormat titleFmt) {
		this.titleFmt = titleFmt;
	}

	public void setSheet(WritableSheet sheet) {
		this.sheet = sheet;
	}

	public WritableWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(WritableWorkbook workbook) {
		this.workbook = workbook;
	}
}
