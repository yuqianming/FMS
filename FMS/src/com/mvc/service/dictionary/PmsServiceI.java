package com.mvc.service.dictionary;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TPms;
import com.mvc.pageModel.base.PageFilter;

public interface PmsServiceI {
	public List<Map<String,Object>> dataGrid(TPms info,PageFilter ph) throws Exception;
	public Long count(TPms info,PageFilter ph)throws Exception;
	public void add(TPms info) throws Exception;
	public void delete(String ids) throws Exception;
	public void edit(TPms accountInfo) throws Exception;
}
