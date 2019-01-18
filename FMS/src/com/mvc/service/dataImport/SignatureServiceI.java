package com.mvc.service.dataImport;

import java.util.List;
import java.util.Map;

import com.mvc.model.sys.TSignature;
import com.mvc.pageModel.base.PageFilter;

public interface SignatureServiceI {
	public List<Map<String,Object>> dataGrid(TSignature info,PageFilter ph) throws Exception;
	public Long count(TSignature info,PageFilter ph) throws Exception;
	public void delete(String ids,String org_id) throws Exception;
	public void edit(TSignature info) throws Exception;
}
