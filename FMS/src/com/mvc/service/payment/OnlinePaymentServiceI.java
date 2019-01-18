package com.mvc.service.payment;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;

public interface OnlinePaymentServiceI {
	public List<Map<String,Object>> dataGrid(String user_id,PageFilter ph) throws Exception;
	public Long count(String user_id,PageFilter ph) throws Exception;
	public int upload(UploadInfo info,HttpSession session) throws Exception;
	public void dataHandle(String month,String user_id,String org_id) throws Exception;
	public void confirm(String user_id) throws Exception;
	public Map<String,Object> check(String user_id,String org_id) throws Exception;
}
