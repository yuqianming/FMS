package com.mvc.pageModel.sys;

public class SearchInfo {
	private String tableName;
	private String orderStart;
	private String month;
	private String category;
	private String batchNo;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getOrderStart() {
		return orderStart;
	}
	public void setOrderStart(String orderStart) {
		this.orderStart = orderStart;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
}
