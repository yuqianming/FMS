package com.mvc.service.overview;

import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.OverView;

public interface OverviewServiceI {
	public Map<String,Object> dataGrid(OverView info,PageFilter ph) throws Exception;
	public Long count(OverView info,PageFilter ph)throws Exception;
}
