package com.mvc.service.dictionary.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TCancelProj;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dictionary.CancelProjServiceI;

@Service
public class CancelProjServiceImpl implements CancelProjServiceI{
	@Autowired
	private BaseDaoI<TCancelProj> cancelProjDao;
	@Override
	public List<TCancelProj> dataGrid(TCancelProj info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String hql="from TCancelProj t "+whereHql(info)+" order by t." + ph.getSort() + " " + ph.getOrder();;
		return cancelProjDao.find(hql,ph.getPage(),ph.getRows());
	}

	@Override
	public Long count(TCancelProj info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(*) from TCancelProj t"+whereHql(info);
		return cancelProjDao.count(hql);
	}
	
	private String whereHql(TCancelProj info)
	{
		String hql=" where t.org_id = '"+info.getOrg_id()+"' ";
		if(StringUtils.hasText(info.getProject_code()))
		{
			hql=" and t.project_code like '%"+info.getProject_code()+"%'";
		}
		if(StringUtils.hasText(info.getProject_name()))
		{
			hql=" and t.project_name like '%"+info.getProject_name()+"%'";
		}
		return hql;
	}

	@Override
	public void add(TCancelProj info) throws Exception {
		// TODO Auto-generated method stub
		cancelProjDao.save(info);
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		cancelProjDao.executeSql("delete from t_cancel_proj where id in ("+ids+")");
	}
	
	@Override
	public void edit(TCancelProj info) throws Exception {
		// TODO Auto-generated method stub
		cancelProjDao.update(info);
	}
}
