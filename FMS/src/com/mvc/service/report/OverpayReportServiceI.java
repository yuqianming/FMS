package com.mvc.service.report;

import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public  interface OverpayReportServiceI {
	public Map<String,Object> dataGrid(String startMonth,String endMonth,String type,boolean cancelStock,boolean cancelAudit,boolean noMaterial,PageFilter ph) throws Exception;
}
