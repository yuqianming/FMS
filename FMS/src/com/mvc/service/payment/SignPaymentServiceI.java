package com.mvc.service.payment;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TSignature;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.SignInfo;

public interface SignPaymentServiceI {
	public List<Map<String, Object>> supplierDataGrid(TSignature info,PageFilter ph) throws Exception;
	public Long supplierCount(TSignature info,PageFilter ph)throws Exception;
	public List<Map<String, Object>> accountDataGrid(TSignature info,PageFilter ph) throws Exception;
	public Long accountCount(TSignature info,PageFilter ph)throws Exception;
	public void save(SignInfo info) throws Exception;
	public void delete(String batchs,String org_id) throws Exception;
}
