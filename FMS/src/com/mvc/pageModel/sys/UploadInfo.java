package com.mvc.pageModel.sys;

import org.springframework.web.multipart.MultipartFile;

public class UploadInfo {
	private MultipartFile upFile;
	private String tableName;
	private String isFull = "1";
	private String userId;
	private String isSynchronize;
	private String importType;
	private String orgId;
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
	public String getIsSynchronize() {
		return isSynchronize;
	}
	public void setIsSynchronize(String isSynchronize) {
		this.isSynchronize = isSynchronize;
	}
	public String getImportType() {
		return importType;
	}
	public void setImportType(String importType) {
		this.importType = importType;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
