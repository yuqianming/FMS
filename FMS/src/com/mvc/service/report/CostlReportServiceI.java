package com.mvc.service.report;

import java.util.Map;

public  interface CostlReportServiceI {
	public Map<String,Object> dataGrid(String startMonth,String endMonth,boolean no_cancel,String asset_trans_sts,String assemble_sts,String doc_type) throws Exception;
}
