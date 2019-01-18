package com.mvc.service.dictionary.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TAccount;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dictionary.AccountServiceI;

@Service
public class AccountServiceImpl implements AccountServiceI{
	@Autowired
	private BaseDaoI<TAccount> accountDao;
	@Override
	public List<TAccount> dataGrid(TAccount accountInfo, PageFilter ph) throws Exception{
		// TODO Auto-generated method stub
		String hql="from TAccount t "+whereHql(accountInfo)+" order by t." + ph.getSort() + " " + ph.getOrder();
		return accountDao.find(hql,ph.getPage(),ph.getRows());
	}

	@Override
	public Long count(TAccount accountInfo, PageFilter ph) throws Exception{
		// TODO Auto-generated method stub
		String hql="select count(*) from TAccount t"+whereHql(accountInfo);
		return accountDao.count(hql);
	}

	private String whereHql(TAccount accountInfo)
	{
		String hql=" where t.org_id = '"+accountInfo.getOrg_id()+"' ";
		if(StringUtils.hasText(accountInfo.getAccount_code()))
		{
			hql=" and t.account_code like '%"+accountInfo.getAccount_code()+"%'";
		}
		if(StringUtils.hasText(accountInfo.getAccount_name()))
		{
			hql=" and t.account_name like '%"+accountInfo.getAccount_name()+"%'";
		}
		return hql;
	}

	@Override
	public void add(TAccount accountInfo) throws Exception {
		// TODO Auto-generated method stub
		accountDao.save(accountInfo);
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		accountDao.executeSql("delete from t_account where id in ("+ids+")");
	}

	@Override
	public void edit(TAccount accountInfo) throws Exception {
		// TODO Auto-generated method stub
		List<TAccount> accountList=accountDao.find("from TAccount where id <> "+accountInfo.getId()+" and account_name = '"+accountInfo.getAccount_name()+"'");
		if(accountList!=null&&accountList.size()>0)
		{
			throw new Exception("科目：'"+accountInfo.getAccount_name()+"'已存在！");
		}
		accountDao.update(accountInfo);
	}

	@Override
	public List<TAccount> getAccountList() throws Exception {
		// TODO Auto-generated method stub
		return accountDao.find("from TAccount t order by t.account_code");
	}
}
