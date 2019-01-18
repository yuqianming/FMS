package com.mvc.service.overview.impl;

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
import com.mvc.model.sys.TPayment;
import com.mvc.model.sys.TSignature;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.OverView;
import com.mvc.service.overview.OverviewServiceI;
import com.mvc.utils.PackResultUtils;
@Service
public class OverviewServiceImpl implements OverviewServiceI{
	@Autowired
	private BaseDaoI<TCost> costDao;
	@Autowired
	private BaseDaoI<TPayment> payDao;
	@Autowired
	private BaseDaoI<TSignature> signDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String,Object> dataGrid(OverView info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 整体浏览DataGrid开始："+sdf.format(new Date()));
		String sql=returnSql(info);
		if(ph!=null)
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		}
		else
		{
			sql+=" order by project_code asc";
		}
		System.out.println("###### datagrid sql : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		System.out.println("###### 整体浏览DataGrid结束："+sdf.format(new Date()));
		return result;
	}

	@Override
	public Long count(OverView info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 整体浏览Count开始："+sdf.format(new Date()));
		String sql="select count(*) from ("+returnSql(info)+") m";
		Long result=jdbcTemplate.queryForObject(sql, Long.class);
		System.out.println("###### 整体浏览Count结束："+sdf.format(new Date()));
		return result;
	}
	
	/*private String returnSql(OverView info) 
	{
		String sql="select 	c.project_code,c.supplier_name,c.account_name,format(c.account_amt,2) as account_amt,format(c.t_pay_amt,2) as t_pay_amt,format(c.s_pay_amt,2) as s_pay_amt,"
				+ "format(c.t_invoice_amt,2) as t_invoice_amt,format(c.t_invoice_amt_e_tax,2) as t_invoice_amt_e_tax,format(c.account_amt - c.t_pay_amt - c.s_pay_amt,2) as n_pay_amt from (select b.project_code,d.unique_name as supplier_name";
		if(!info.isHide_account())
		{
			sql+=",b.account_name";
		}
		else
		{
			sql+=",'' as account_name";
		}
		sql+=","
				+ "sum(b.account_amt) as account_amt,"
				+ "sum(b.t_pay_amt) as t_pay_amt,"
				+ "sum(b.s_pay_amt) as s_pay_amt,"
				+ "sum(b.t_invoice_amt) as t_invoice_amt,"
				+ "sum(b.t_invoice_amt_e_tax) as t_invoice_amt_e_tax "
				+ "from "
				+ "(select c.project_code,c.supplier_name,c.account_name,sum(c.account_amt) as account_amt,0 as t_pay_amt,0 as s_pay_amt,0 as t_invoice_amt,0 as t_invoice_amt_e_tax from t_cost c where c.org_id = '"+info.getOrg_id()+"' group by c.project_code,c.supplier_name,c.account_name "
				+ "union all "
				+ "select s.project_code,s.supplier_name,s.account_name,0 as account_amt,0 as t_pay_amt,s.pay_amt_e_tax as s_pay_amt,s.invoice_amt as t_invoice_amt,s.invoice_amt_e_tax as t_invoice_amt_e_tax from t_signature s  where s.org_id = '"+info.getOrg_id()+"'"
				+ "union all "
				+ "select p.project_code,p.supplier_name,p.account_name,0 as account_amt,p.pay_amt_e_tax as t_pay_amt,0 as s_pay_amt,p.invoice_amt as t_invoice_amt,p.invoice_amt_e_tax as t_invoice_amt_e_tax from t_payment p  where p.org_id = '"+info.getOrg_id()+"') b left join t_supplier d on b.supplier_name=d.supplier_name and d.org_id = '"+info.getOrg_id()+"' ";
		sql+="where 1=1 ";
		if(StringUtils.hasText(info.getProject_code()))
		{
			sql+=" and b.project_code like '%"+info.getProject_code()+"%'";
		}
		if(StringUtils.hasText(info.getSupplier_name()))
		{
			sql+=" and b.supplier_name like '%"+info.getSupplier_name()+"%'";
		}
		if(StringUtils.hasText(info.getProject_category()))
		{
			if("1".equals(info.getProject_category()))
			{
				sql+=" and exists (select 1 from t_cancel_proj c where c.org_id = '"+info.getOrg_id()+"' and c.project_code = b.project_code)";
			}
			else if("2".equals(info.getProject_category()))
			{
				sql+=" and not exists (select 1 from t_cancel_proj c where c.org_id = '"+info.getOrg_id()+"' and c.project_code = b.project_code)";
			}
		}
        sql+=" group by b.project_code,d.unique_name";
        if(!info.isHide_account())
        {
        	sql+=",b.account_name";
        }
        sql+=") c ";
        if(info.isHide_zero())
        {
        	sql+="where (c.account_amt - c.t_pay_amt - c.s_pay_amt) <> 0";
        }
		return sql;
	}*/
	private String returnSql(OverView info) 
	{
		String sql="select 	c.project_code,c.supplier_name,c.account_name,format(c.account_amt,2) as account_amt,format(c.t_pay_amt,2) as t_pay_amt,format(c.s_pay_amt,2) as s_pay_amt,"
				+ "format(c.t_invoice_amt,2) as t_invoice_amt,format(c.t_invoice_amt_e_tax,2) as t_invoice_amt_e_tax,format(c.account_amt - c.t_pay_amt - c.s_pay_amt,2) as n_pay_amt from (select b.project_code,b.supplier_name";
		if(!info.isHide_account())
		{
			sql+=",b.account_name";
		}
		else
		{
			sql+=",'' as account_name";
		}
		sql+=","
				+ "case when sum(b.account_amt) is null then 0 else sum(b.account_amt) end as account_amt,"
				+ "case when sum(b.t_pay_amt) is null then 0 else sum(b.t_pay_amt) end as t_pay_amt,"
				+ "case when sum(b.s_pay_amt) is null then 0 else sum(b.s_pay_amt) end as s_pay_amt,"
				+ "case when sum(b.t_invoice_amt) is null then 0 else sum(b.t_invoice_amt) end as t_invoice_amt,"
				+ "case when sum(b.t_invoice_amt_e_tax) is null then 0 else sum(b.t_invoice_amt_e_tax) end as t_invoice_amt_e_tax "
				+ "from t_over_view b ";
		sql+="where b.org_id = '"+info.getOrg_id()+"' ";
		if(StringUtils.hasText(info.getProject_code()))
		{
			sql+=" and b.project_code like '%"+info.getProject_code()+"%'";
		}
		if(StringUtils.hasText(info.getSupplier_name()))
		{
			sql+=" and b.supplier_name like '%"+info.getSupplier_name()+"%'";
		}
		if(StringUtils.hasText(info.getProject_category()))
		{
			if("1".equals(info.getProject_category()))
			{
				sql+=" and exists (select 1 from t_cancel_proj c where c.org_id = '"+info.getOrg_id()+"' and c.project_code = b.project_code)";
			}
			else if("2".equals(info.getProject_category()))
			{
				sql+=" and not exists (select 1 from t_cancel_proj c where c.org_id = '"+info.getOrg_id()+"' and c.project_code = b.project_code)";
			}
		}
        sql+=" group by b.project_code,b.supplier_name";
        if(!info.isHide_account())
        {
        	sql+=",b.account_name";
        }
        sql+=") c ";
        if(info.isHide_zero())
        {
        	sql+="where (c.account_amt - c.t_pay_amt - c.s_pay_amt) <> 0";
        }
		return sql;
	}
}
