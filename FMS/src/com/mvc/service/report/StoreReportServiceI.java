package com.mvc.service.report;

import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public interface StoreReportServiceI {
	public Map<String,Object> dataGrid(String startMonth,String endMonth,boolean no_out,boolean no_in,PageFilter ph) throws Exception;
}
