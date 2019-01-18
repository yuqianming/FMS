package com.mvc.service.dictionary.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TOldProj;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dictionary.OldProjServiceI;

@Service
public class OldProjServiceImpl implements OldProjServiceI{
	@Autowired
	private BaseDaoI<TOldProj> oldProjDao;
	@Override
	public List<TOldProj> dataGrid(TOldProj info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String hql="from TOldProj t "+whereHql(info)+" order by t." + ph.getSort() + " " + ph.getOrder();;
		return oldProjDao.find(hql,ph.getPage(),ph.getRows());
	}

	@Override
	public Long count(TOldProj info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(*) from TOldProj t"+whereHql(info);
		return oldProjDao.count(hql);
	}
	
	private String whereHql(TOldProj info)
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
	public void add(TOldProj info) throws Exception {
		// TODO Auto-generated method stub
		oldProjDao.save(info);
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		oldProjDao.executeSql("delete from t_old_proj where id in ("+ids+")");
	}
	
	@Override
	public void edit(TOldProj info) throws Exception {
		// TODO Auto-generated method stub
		oldProjDao.update(info);
	}
}
