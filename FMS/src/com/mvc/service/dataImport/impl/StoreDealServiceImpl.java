package com.mvc.service.dataImport.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TStoreDeal;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dataImport.StoreDealServiceI;
@Service
public class StoreDealServiceImpl implements StoreDealServiceI{
	@Autowired
	private BaseDaoI<TStoreDeal> dealDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String,Object>> dataGrid(TStoreDeal info, PageFilter ph) throws Exception{
		// TODO Auto-generated method stub
		//String hql="from TStoreDeal t order by t." + ph.getSort() + " " + ph.getOrder();
		String sql="select t.project_code,t.project_name,t.order_code,t.batch_no,t.material_code,t.material_name,t.busi_type,t.opration_type,t.supplier_code,"
				+ "t.supplier_name,t.quantity_org,t.quantity_txn,t.quantity_left,format(t.total_amt_e_tax,2) as total_amt_e_tax,format(t.total_amt_i_tax,2) as "
				+ "total_amt_i_tax,t.create_time,t.is_check,t.remark from t_store_deal t"+ whereSql(info);
		if(ph==null)
		{
			sql+=" order by project_code asc";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(TStoreDeal info, PageFilter ph) throws Exception{
		// TODO Auto-generated method stub
		String hql="select count(*) from TStoreDeal t"+ whereSql(info);
		return dealDao.count(hql);
	}

	private String whereSql(TStoreDeal info)
	{
		String sql=" where t.org_id = '"+info.getOrg_id()+"' ";
		return sql;
	}
}
