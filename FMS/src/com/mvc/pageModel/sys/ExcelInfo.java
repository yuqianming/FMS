package com.mvc.pageModel.sys;

public class ExcelInfo {
	private String fileUrl;
	private String fileName;
	private String[] columnCodes={};
	private String[] columnNames={};
	private String[] cellTypes={};
	private String sheetName="sheet";
	private short colWidth=150;
	private String[] rangeNames={};
	private String[] rangeCells={};
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String[] getColumnCodes() {
		return columnCodes;
	}
	public void setColumnCodes(String[] columnCodes) {
		this.columnCodes = columnCodes;
	}
	public String[] getColumnNames() {
		return columnNames;
	}
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public short getColWidth() {
		return colWidth;
	}
	public void setColWidth(short colWidth) {
		this.colWidth = colWidth;
	}
	public String[] getCellTypes() {
		return cellTypes;
	}
	public void setCellTypes(String[] cellTypes) {
		this.cellTypes = cellTypes;
	}
	public String[] getRangeNames() {
		return rangeNames;
	}
	public void setRangeNames(String[] rangeNames) {
		this.rangeNames = rangeNames;
	}
	public String[] getRangeCells() {
		return rangeCells;
	}
	public void setRangeCells(String[] rangeCells) {
		this.rangeCells = rangeCells;
	}
}
