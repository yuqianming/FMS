package com.mvc.service.dataImport;

import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public interface MaterialServiceI {
	public List<Map<String,Object>> dataGrid(PageFilter ph,String org_id) throws Exception;
	public Long count(PageFilter ph,String org_id) throws Exception;
}
