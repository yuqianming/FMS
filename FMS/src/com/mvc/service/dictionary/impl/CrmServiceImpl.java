package com.mvc.service.dictionary.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TCrm;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dictionary.CrmServiceI;

@Service
public class CrmServiceImpl implements CrmServiceI{
	@Autowired
	private BaseDaoI<TCrm> crmDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String,Object>> dataGrid(TCrm info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="select * from t_crm t "+whereHql(info);
		if(ph==null)
		{
			sql+=" order by order_code asc";
		}
		else
		{
		    sql+=" order by t." + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(TCrm info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(*) from TCrm t"+whereHql(info);
		return crmDao.count(hql);
	}
	
	private String whereHql(TCrm info)
	{
		String hql=" where t.org_id = '"+info.getOrg_id()+"' ";
		if(StringUtils.hasText(info.getRent_status()))
		{
			hql+=" and t.rent_status like '%"+info.getRent_status()+"%'";
		}
		if(StringUtils.hasText(info.getOrder_code()))
		{
			hql+=" and t.order_code like '%"+info.getOrder_code()+"%'";
		}
		return hql;
	}

	@Override
	public void add(TCrm info) throws Exception {
		// TODO Auto-generated method stub
		crmDao.save(info);
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		crmDao.executeSql("delete from t_crm where id in ("+ids+")");
	}
	
	@Override
	public void edit(TCrm info) throws Exception {
		// TODO Auto-generated method stub
		crmDao.update(info);
	}
}
