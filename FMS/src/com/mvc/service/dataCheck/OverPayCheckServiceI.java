package com.mvc.service.dataCheck;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public interface OverPayCheckServiceI {
	public Map<String,Object> supplierGrid(boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,String org_id) throws Exception;
	public Long supplierCount(boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,String org_id) throws Exception;
	public List<Map<String,Object>> supplierFooter(boolean cancelStock,boolean cancelAudit,PageFilter ph) throws Exception;
	public Map<String,Object> payGrid(String supplier_name,String project_code,boolean is_all,boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,String org_id) throws Exception;
	public Long payCount(String supplier_name,String project_code,boolean is_all,boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,String org_id) throws Exception;
	public List<Map<String,Object>> payFooter(String supplier_name,String project_code,boolean is_all,PageFilter ph) throws Exception;
}
