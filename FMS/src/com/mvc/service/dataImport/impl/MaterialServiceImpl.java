package com.mvc.service.dataImport.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dataImport.MaterialServiceI;
@Service
public class MaterialServiceImpl implements MaterialServiceI{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String, Object>> dataGrid(PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		String sql="select id,operate_type,item_explain,format(amt,2) as amt,voucher_no,month,detail_adj,doc_code,system_type from t_material_adj where org_id = '"+org_id+"' ";
		if(ph==null)
		{
			sql+=" order by id asc";
		}
		else
		{
		    sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public Long count(PageFilter ph,String org_id) throws Exception{
		// TODO Auto-generated method stub
		String sql="select count(*) from t_material_adj where org_id = '"+org_id+"'";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

}
