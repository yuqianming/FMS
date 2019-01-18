package com.mvc.service.dictionary;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.mvc.model.sys.TSupplierAdj;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;

public interface SupplierAdjServiceI {
	public List<Map<String,Object>> dataGrid(TSupplierAdj info,PageFilter ph) throws Exception;
	public Long count(TSupplierAdj info,PageFilter ph)throws Exception;
	public void add(TSupplierAdj info) throws Exception;
	public void delete(String ids) throws Exception;
	public void edit(TSupplierAdj accountInfo) throws Exception;
	public void upload(UploadInfo info,HttpSession session) throws Exception;
}
