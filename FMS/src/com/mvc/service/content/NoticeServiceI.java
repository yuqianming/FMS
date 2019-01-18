package com.mvc.service.content;

import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public interface NoticeServiceI {
	
	public Map<String,Object> dataGrid(String id,String notice_title,String notice_content,String org_id,PageFilter ph) throws Exception;
	public void add(String notice_title,String notice_content,String org_id,String user_id) throws Exception;
	
	public void delete(String idS) throws Exception;
}
