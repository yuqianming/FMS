package com.mvc.pageModel.sys;


import org.springframework.web.multipart.MultipartFile;

public class CostInfo {
	private MultipartFile upFile;
	private String tableName;
	private String isFull;
	private String userId;
	
	private String list_id;
	private String busi_date;
	private String order_code;
	private String supplier_code;
	private String supplier_name;
	private String project_code;
	private String project_name;
	private String documentary;
	private String account_code;
	private String account_name;
	private String voucher_no;
	private String org_id;
	public MultipartFile getUpFile() {
		return upFile;
	}
	public void setUpFile(MultipartFile upFile) {
		this.upFile = upFile;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getIsFull() {
		return isFull;
	}
	public void setIsFull(String isFull) {
		this.isFull = isFull;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getList_id() {
		return list_id;
	}
	public void setList_id(String list_id) {
		this.list_id = list_id;
	}
	public String getBusi_date() {
		return busi_date;
	}
	public void setBusi_date(String busi_date) {
		this.busi_date = busi_date;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getSupplier_code() {
		return supplier_code;
	}
	public void setSupplier_code(String supplier_code) {
		this.supplier_code = supplier_code;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	public String getProject_code() {
		return project_code;
	}
	public void setProject_code(String project_code) {
		this.project_code = project_code;
	}
	public String getProject_name() {
		return project_name;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getDocumentary() {
		return documentary;
	}
	public void setDocumentary(String documentary) {
		this.documentary = documentary;
	}
	public String getAccount_code() {
		return account_code;
	}
	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getVoucher_no() {
		return voucher_no;
	}
	public void setVoucher_no(String voucher_no) {
		this.voucher_no = voucher_no;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	
	
}
