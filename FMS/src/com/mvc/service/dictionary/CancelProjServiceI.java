package com.mvc.service.dictionary;

import java.util.List;

import com.mvc.model.sys.TCancelProj;
import com.mvc.pageModel.base.PageFilter;

public interface CancelProjServiceI {
	public List<TCancelProj> dataGrid(TCancelProj info,PageFilter ph) throws Exception;
	public Long count(TCancelProj info,PageFilter ph)throws Exception;
	public void add(TCancelProj info) throws Exception;
	public void delete(String ids) throws Exception;
	public void edit(TCancelProj accountInfo) throws Exception;
}
