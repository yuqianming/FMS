package com.mvc.service.dataImport;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TGodownEntry;
import com.mvc.pageModel.base.PageFilter;

public interface CheckInServiceI {
	public List<Map<String,Object>> dataGrid(TGodownEntry info,PageFilter ph) throws Exception;
	public Long count(TGodownEntry info,PageFilter ph) throws Exception;
}
