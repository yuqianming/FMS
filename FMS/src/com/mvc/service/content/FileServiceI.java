package com.mvc.service.content;

import java.util.Map;



import org.springframework.web.multipart.MultipartFile;

import com.mvc.pageModel.base.PageFilter;

public interface FileServiceI {
	public Map<String,Object> dataGrid(String file_name,String remark,String org_id,PageFilter ph) throws Exception;
	public void add(MultipartFile upFile,String remark,String user_id,String org_id) throws Exception;
	
	public void delete(String id) throws Exception;
	
	public Map<String,Object> getFilePathById(int id) throws Exception;
}
