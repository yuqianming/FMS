package com.mvc.service.dictionary;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.mvc.model.sys.TSupplier;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;

public interface SupplierServiceI {
	public List<TSupplier> dataGrid(TSupplier info,PageFilter ph) throws Exception;
	public Long count(TSupplier info,PageFilter ph)throws Exception;
	public void add(TSupplier info) throws Exception;
	public void delete(String ids) throws Exception;
	public void edit(TSupplier accountInfo) throws Exception;
	public List<Map<String,Object>> getSupplierList(String org_id) throws Exception;
	public void upload(UploadInfo info,HttpSession session) throws Exception;
}
