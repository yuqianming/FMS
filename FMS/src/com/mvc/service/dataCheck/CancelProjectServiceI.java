package com.mvc.service.dataCheck;

import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public interface CancelProjectServiceI {
	public Map<String,Object> cancelProjGrid(boolean hide,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception;
	public Long cancelProjCount(boolean hide,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception;
	public List<Map<String,Object>> cancelProjFooter(boolean hide,PageFilter ph) throws Exception;
	public Map<String,Object> supplierGrid(String project_code,String supplier_name,boolean is_all,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception;
	public Long supplierCount(String project_code,boolean is_all,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception;
	public List<Map<String,Object>> supplierFooter(String project_code,boolean is_all,PageFilter ph) throws Exception;
}
