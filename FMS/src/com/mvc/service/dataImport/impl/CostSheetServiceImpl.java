package com.mvc.service.dataImport.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TCost;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.CostInfo;
import com.mvc.service.dataImport.CostSheetServiceI;


@Service
public class CostSheetServiceImpl implements CostSheetServiceI{
	@Autowired
	private BaseDaoI<TCost> costDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public List<Map<String,Object>> dataGrid(CostInfo costInfo,PageFilter ph) {
		// TODO Auto-generated method stub
		System.out.println("###### 成本单dataGrid start : "+sdf.format(new Date()));
		String sql="select id,list_id,date_format(busi_date, '%Y-%m-%d') as busi_date,order_code,source_type,supplier_name,supplier_code,material_code,manufacturer,spec_code,material_name,"
				+ "service_code,unit,quantity,format(unit_price_e_tax,2) as unit_price_e_tax,format(total_amt_e_tax,2) as total_amt_e_tax,"
				+ "format(period_cost_amt,2) as period_cost_amt,format(account_amt,2) as account_amt,project_code,project_name,address_code,"
				+ "address_name,accounting_org,doc_type,documentary,assemble_sts,allocation_sts,asset_trans_sts,date_format(timestamp, '%Y-%m-%d') as timestamp,account_code,"
				+ "account_name,order_type,voucher_no,detail_no from t_cost t "+whereHql(costInfo);
		if(ph==null)
		{
			sql+=" order by id asc";
		}
		else
		{
		    sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		System.out.println("###### 成本单dataGrid end : "+sdf.format(new Date()));
		return mapList;
	}

	@Override
	public Long count(CostInfo costInfo,PageFilter ph) {
	// TODO Auto-generated method stub
		String hql="select count(*) from TCost t"+whereHql(costInfo);
		return costDao.count(hql);
	}
	
	private String whereHql(CostInfo costInfo)
	{
		String hql=" where t.org_id = '"+costInfo.getOrg_id()+"' ";
		if(costInfo!=null)
		{
			if(StringUtils.hasText(costInfo.getBusi_date()))
			{
				hql+=" and t.busi_date = '"+costInfo.getBusi_date()+"'";
			}
			if(StringUtils.hasText(costInfo.getOrder_code()))
			{
				hql+=" and t.order_code like '%"+costInfo.getOrder_code()+"%'";
			}
			if(StringUtils.hasText(costInfo.getSupplier_name()))
			{
				hql+=" and t.supplier_name like '%"+costInfo.getSupplier_name()+"%'";
			}
			if(StringUtils.hasText(costInfo.getSupplier_code()))
			{
				hql+=" and t.supplier_code like '%"+costInfo.getSupplier_code()+"%'";
			}
			if(StringUtils.hasText(costInfo.getProject_code()))
			{
				hql+=" and t.project_code like '%"+costInfo.getProject_code()+"%'";
			}
			if(StringUtils.hasText(costInfo.getProject_name()))
			{
				hql+=" and t.project_name like '%"+costInfo.getProject_name()+"%'";
			}
			if(StringUtils.hasText(costInfo.getDocumentary()))
			{
				hql+=" and t.documentary like '%"+costInfo.getDocumentary()+"%'";
			}
			if(StringUtils.hasText(costInfo.getAccount_code()))
			{
				hql+=" and t.account_code like '%"+costInfo.getAccount_code()+"%'";
			}
			if(StringUtils.hasText(costInfo.getAccount_name()))
			{
				hql+=" and t.account_name like '%"+costInfo.getAccount_name()+"%'";
			}
			if(StringUtils.hasText(costInfo.getVoucher_no()))
			{
				hql+="and t.voucher_no like '%"+costInfo.getVoucher_no()+"%'";
			}
		}
	    return hql;
	}
}
