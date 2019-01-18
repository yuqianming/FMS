package com.mvc.service.dataImport;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TStoreDeal;
import com.mvc.pageModel.base.PageFilter;

public interface StoreDealServiceI {
	public List<Map<String,Object>> dataGrid(TStoreDeal info,PageFilter ph) throws Exception;
	public Long count(TStoreDeal info,PageFilter ph) throws Exception;
}
