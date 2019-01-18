package com.mvc.service.dictionary.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TPms;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dictionary.PmsServiceI;

@Service
public class PmsServiceImpl implements PmsServiceI{
	@Autowired
	private BaseDaoI<TPms> pmsDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String,Object>> dataGrid(TPms info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="select * from t_pms t "+whereHql(info);
		if(ph==null)
		{
			sql+=" order by project_code asc";
		}
		else
		{
		    sql+=" order by t." + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(TPms info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(*) from TPms t"+whereHql(info);
		return pmsDao.count(hql);
	}
	
	private String whereHql(TPms info)
	{
		String hql=" where t.org_id = '"+info.getOrg_id()+"' ";
		if(StringUtils.hasText(info.getProject_code()))
		{
			hql+=" and t.project_code like '%"+info.getProject_code()+"%'";
		}
		if(StringUtils.hasText(info.getOrder_code()))
		{
			hql+=" and t.order_code like '%"+info.getOrder_code()+"%'";
		}
		return hql;
	}

	@Override
	public void add(TPms info) throws Exception {
		// TODO Auto-generated method stub
		pmsDao.save(info);
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		pmsDao.executeSql("delete from t_pms where id in ("+ids+")");
	}
	
	@Override
	public void edit(TPms info) throws Exception {
		// TODO Auto-generated method stub
		pmsDao.update(info);
	}
}
