package com.mvc.service.dataCheck;

import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.AmtInfo;
import com.mvc.pageModel.sys.SearchInfo;

public interface ProjectCheckServiceI {
	public List<AmtInfo> poolGrid(String org_id) throws Exception;
	public List<Map<String,Object>> monthGrid(SearchInfo info,PageFilter ph,String org_id) throws Exception;
	public Long monthCount(SearchInfo info,PageFilter ph,String org_id) throws Exception;
	public List<Map<String,Object>> categoryGrid(SearchInfo info,PageFilter ph,String org_id) throws Exception;
	public Long categoryCount(SearchInfo info,PageFilter ph,String org_id) throws Exception;
	public List<Map<String,Object>> detailGrid(SearchInfo info,PageFilter ph,String org_id) throws Exception;
	public Long detailCount(SearchInfo info,PageFilter ph,String org_id) throws Exception;
}
