package com.mvc.service.dataCheck;

import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;


public interface StoreCheckServiceI {
	public List<Map<String,Object>> addGrid(String startMonth,String endMonth,String org_id) throws Exception;
	public List<Map<String,Object>> subGrid(String startMonth,String endMonth,String org_id) throws Exception;
	public Map<String,Object> poolData(String startMonth,String endMonth,String org_id) throws Exception;
	public Map<String,Object> detailGrid(String startMonth,String endMonth,String type,String mode,boolean isAll,boolean hideZero,PageFilter ph,String org_id) throws Exception;
	public Long detailCount(String startMonth,String endMonth,String type,String mode,boolean isAll,boolean hideZero,String org_id) throws Exception;
}
