package com.mvc.service.dictionary;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TTaxRate;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;

public interface TaxRateServiceI {
	public List<Map<String,Object>> dataGrid(TTaxRate info,PageFilter ph) throws Exception;
	public Long count(TTaxRate info,PageFilter ph)throws Exception;
	public void add(TTaxRate info) throws Exception;
	public void delete(String ids) throws Exception;
	public List<Map<String,Object>> getRateList() throws Exception;
	public void edit(TTaxRate accountInfo) throws Exception;
	public void upload(UploadInfo info) throws Exception;
}
