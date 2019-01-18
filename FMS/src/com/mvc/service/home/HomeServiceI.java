package com.mvc.service.home;

import java.util.List;
import java.util.Map;
import com.mvc.model.sys.TPayment;
import com.mvc.pageModel.base.PageFilter;

public interface HomeServiceI {
	
	//获取供应商前十
	public List<TPayment> getPayOffList() throws Exception;
	
	//获取供应商前十
	public List<Map<String,Object>> payOffDataGrid(String orgId,PageFilter ph) throws Exception;
	
	//数据导入记录
//	public List<Map<String,Object>> getDataImportTree(String orgId);
	
	//数据导入记录
//	public List<Map<String,Object>> getDataImportGrid(String orgId,PageFilter ph) throws Exception;
	
	//数据导入记录
	public List<Map<String, Object>> getDataImport(String orgId, PageFilter ph) throws Exception;
	public Long count(String orgId) throws Exception;
	
	//自定义首页-弹窗复选框及Echart初始化
	public List<Map<String,Object>> initCustomTreeList(String userId) throws Exception;
	
	//自定义首页-保存用户选择
	public int[] updateCustom(String userId,String customIds) throws Exception;
	
	//获取notice org_name
	public Map<String, Object> getNoticeDept(String orgId) throws Exception;
	//获取notice user_name
	public Map<String, Object> getNoticeUser(String userId) throws Exception;
	
}
