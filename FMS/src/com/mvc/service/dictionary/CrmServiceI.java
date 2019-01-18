package com.mvc.service.dictionary;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TCrm;
import com.mvc.pageModel.base.PageFilter;

public interface CrmServiceI {
	public List<Map<String,Object>> dataGrid(TCrm info,PageFilter ph) throws Exception;
	public Long count(TCrm info,PageFilter ph)throws Exception;
	public void add(TCrm info) throws Exception;
	public void delete(String ids) throws Exception;
	public void edit(TCrm accountInfo) throws Exception;
}
