package com.mvc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;

public class PackResultUtils {
	public static Map<String,Object> packResult(List<Map<String,Object>> mapList,PageFilter ph)
	{
		Map<String, Object> result=new HashMap<String, Object>();
		List<Map<String,Object>> temp=new ArrayList<Map<String,Object>>();
		if(ph!=null)
		{
			if(mapList!=null&&mapList.size()>0)
			{
				int start=(ph.getPage()-1)*ph.getRows();
				int rows=ph.getPage()*ph.getRows()>mapList.size()?mapList.size():ph.getPage()*ph.getRows();
				for(int i=start;i<rows;i++)
				{
					temp.add(mapList.get(i));
				}
			}
			result.put("total", mapList.size());
			result.put("rows", temp);
		}
		else
		{
			temp=mapList;
			result.put("total", mapList.size());
			result.put("rows", temp);
		}
		return result;
	}
}
