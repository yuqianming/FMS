package com.mvc.service.dataImport;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TPayment;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;

public interface PaymentServiceI {
	public List<Map<String,Object>> dataGrid(TPayment info,PageFilter ph) throws Exception;
	public Long count(TPayment info,PageFilter ph) throws Exception;
	public void delete(String ids,String org_id) throws Exception;
	public void edit(TPayment info) throws Exception;
	public void upload(UploadInfo info) throws Exception;
}
