package com.mvc.service.common;

import javax.servlet.http.HttpSession;

import com.mvc.pageModel.sys.UploadInfo;

public interface CommonServiceI {
	public int upload(UploadInfo info,HttpSession session) throws Exception;
	public void save(UploadInfo info) throws Exception;
	public String tip(String tableName,String org_id) throws Exception;
}
