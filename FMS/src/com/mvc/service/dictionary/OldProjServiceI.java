package com.mvc.service.dictionary;

import java.util.List;

import com.mvc.model.sys.TOldProj;
import com.mvc.pageModel.base.PageFilter;

public interface OldProjServiceI {
	public List<TOldProj> dataGrid(TOldProj info,PageFilter ph) throws Exception;
	public Long count(TOldProj info,PageFilter ph)throws Exception;
	public void add(TOldProj info) throws Exception;
	public void delete(String ids) throws Exception;
	public void edit(TOldProj accountInfo) throws Exception;
}
