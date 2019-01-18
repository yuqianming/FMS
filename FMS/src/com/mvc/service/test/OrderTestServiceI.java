package com.mvc.service.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.OrderTest;
import com.mvc.pageModel.sys.UploadInfo;

public interface OrderTestServiceI {
	public void upload(UploadInfo info) throws Exception;
	public List<Map<String,Object>> dataGrid(String user_id,PageFilter ph) throws Exception;
	public Long count(String user_id,PageFilter ph) throws Exception;
	public void add(String user_id,OrderTest info) throws Exception;
	public void edit(String user_id,OrderTest info) throws Exception;
	public void delete(String user_id,String ids) throws Exception;
	public Map<String,Object> startTest(String user_id,String org_id,BigDecimal tax_rate,BigDecimal deviation) throws Exception;
	public void confirmSign(BigDecimal invoice_amt_e_tax,BigDecimal pay_amt_e_tax,String user_id,String org_id) throws Exception;
	public Map<String,Object> getNopassCount(String user_id) throws Exception;
	public void deleteOrderTest(String user_id) throws Exception;
}
