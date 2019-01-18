package com.mvc.service.report;

import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public interface PaymentReportServiceI {
	public Map<String,Object> dataGrid(String startMonth,String endMonth,String type,PageFilter ph) throws Exception;
}
