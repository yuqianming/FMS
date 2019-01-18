package com.mvc.service.dataImport;

import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.CostInfo;

public interface CostSheetServiceI {
	public List<Map<String,Object>> dataGrid(CostInfo costInfo,PageFilter ph) throws Exception;
	public Long count(CostInfo costInfo,PageFilter ph) throws Exception;
}
