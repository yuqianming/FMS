package com.mvc.service.dataImport.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TGodownEntry;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dataImport.CheckInServiceI;
@Service
public class CheckInServiceImpl implements CheckInServiceI{
	@Autowired
	private BaseDaoI<TGodownEntry> checkDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String,Object>> dataGrid(TGodownEntry info, PageFilter ph) {
		// TODO Auto-generated method stub
		//String hql="from TGodownEntry t order by t." + ph.getSort() + " " + ph.getOrder();
		String sql="select t.entry_date,t.voucher_no,t.entry_code,t.order_code,format(t.amount,2) as amount,t.is_check,t.remark from t_godown_entry t "+whereSql(info);
		if(ph==null)
		{
			sql+=" order by entry_date asc";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(TGodownEntry info, PageFilter ph) {
		// TODO Auto-generated method stub
		String hql="select count(*) from TGodownEntry t"+whereSql(info);
		return checkDao.count(hql);
	}
	
	private String whereSql(TGodownEntry info)
	{
		String sql=" where t.org_id = '"+info.getOrg_id()+"' ";
		return sql;
	}

}
