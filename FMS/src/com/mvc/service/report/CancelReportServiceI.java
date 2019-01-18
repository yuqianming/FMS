package com.mvc.service.report;

import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public  interface CancelReportServiceI {
	public Map<String,Object> dataGrid(String startMonth,String endMonth,String type,boolean hide,boolean cancelStock,boolean noMaterial,PageFilter ph) throws Exception;
}
