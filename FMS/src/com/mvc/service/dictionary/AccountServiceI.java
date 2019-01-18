package com.mvc.service.dictionary;

import java.util.List;

import com.mvc.model.sys.TAccount;
import com.mvc.pageModel.base.PageFilter;

public interface AccountServiceI {
	public List<TAccount> dataGrid(TAccount accountInfo,PageFilter ph) throws Exception;
	public Long count(TAccount accountInfo,PageFilter ph)throws Exception;
	public void add(TAccount accountInfo) throws Exception;
	public void delete(String ids) throws Exception;
	public void edit(TAccount accountInfo) throws Exception;
	public List<TAccount> getAccountList() throws Exception;
}
