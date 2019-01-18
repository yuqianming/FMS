package com.mvc.service.dataCheck.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dataCheck.CancelProjectServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class CancelProjectServiceImpl implements CancelProjectServiceI{
	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String, Object> cancelProjGrid(boolean hide,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 已销项检查 start : "+sdf.format(new Date()));
		String sql=cancelProjSql(hide,cancelStock,noMaterial,org_id);
		if(ph==null)
		{
			sql+=" order by project_code";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		}
		System.out.println("###### sql is : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		System.out.println("###### 已销项检查end : "+sdf.format(new Date()));
		return result;
	}

	@Override
	public Long cancelProjCount(boolean hide,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### cancel query count start : "+sdf.format(new Date()));
		String sql="select count(*) from ("+cancelProjSql(hide,cancelStock,noMaterial,org_id)+") m";
		//System.out.println("###### sql is : "+sql);
		Long count=jdbcTemplate.queryForObject(sql,Long.class);
		System.out.println("###### cancel query count end : "+sdf.format(new Date())+"   --- count : "+count);
		return count;
	}
	
	private String cancelProjSql(boolean hide,boolean cancelStock,boolean noMaterial,String org_id)
	{
		/*String sql="select t.project_code,format((case when cost.account_amt is null then 0 else cost.account_amt end),2) as account_amt_txt,(case when cost.account_amt is null then 0 else cost.account_amt end) as account_amt,"
				+ "format((case when pay.pay_amt_e_tax is null then 0 else pay.pay_amt_e_tax end),2) as pay_amt_e_tax_txt,(case when pay.pay_amt_e_tax is null then 0 else pay.pay_amt_e_tax end) as pay_amt_e_tax from t_cancel_proj t "
				+ "left join (select c.project_code,sum(c.account_amt) as account_amt from t_cost c where c.org_id = '"+org_id+"' ";
		sql+=" group by c.project_code) cost on t.project_code=cost.project_code ";
		sql+=" left join (select p.project_code,sum(p.pay_amt_e_tax) as pay_amt_e_tax from t_payment p where p.org_id = '"+org_id+"'";
		if(cancelStock)
		{
			sql+=" and not exists (select 1 from t_cost c where c.org_id = '"+org_id+"' and c.order_code=p.order_code and c.supplier_name=p.supplier_name and c.account_name=p.account_name and c.project_code = p.project_code and c.order_type like '%退库红冲%') ";
		}
		if(noMaterial)
		{
			sql+=" and p.account_name <> '物资采购订单' ";
		}
		sql+=" group by p.project_code) pay on t.project_code=pay.project_code where t.org_id = '"+org_id+"'";
		if(hide)
		{
			sql+=" and (cost.account_amt <> 0 or pay.pay_amt_e_tax <> 0) ";
		}*/
		String sql="select a.project_code,a.supplier_name,a.account_amt,format(a.account_amt,2) as account_amt_txt,(case when b.pay_amt_e_tax is null then 0 else b.pay_amt_e_tax end) as pay_amt_e_tax,format((case when b.pay_amt_e_tax is null then 0 else b.pay_amt_e_tax end),2) as pay_amt_e_tax_txt,format(a.account_amt - (case when b.pay_amt_e_tax is null then 0 else b.pay_amt_e_tax end),2) as dvalue from "
				+ "(select c.project_code,c.supplier_name,sum(c.account_amt) as account_amt from t_cost c,t_cancel_proj p where c.org_id = '"+org_id+"' ";
		sql+=" and  p.org_id = '"+org_id+"' and p.project_code = c.project_code ";
		sql+=" group by c.project_code,c.supplier_name) a left join ( select t.project_code,t.supplier_name,sum(t.pay_amt_e_tax) as pay_amt_e_tax "
			+ "from t_payment t,t_cancel_proj p where t.org_id = '"+org_id+"' ";
		sql+=" and  p.org_id = '"+org_id+"' and p.project_code = t.project_code ";
		if(cancelStock)
		{
			sql+=" and t.flag = 0 ";
			//sql+=" and not exists (select 1 from t_cost c where c.org_id = '"+org_id+"' and t.project_code = c.project_code and  t.supplier_name=c.supplier_name and c.order_type like '%退库红冲%') ";
		}
		if(noMaterial)
		{
			sql+=" and t.account_name <> '物资采购订单' ";
		}
		sql+=" group by t.project_code,t.supplier_name) b on  a.project_code=b.project_code and a.supplier_name=b.supplier_name  ";
		if(hide)
		{
			sql+=" where a.account_amt <> 0 or (case when b.pay_amt_e_tax is null then 0 else b.pay_amt_e_tax end) <> 0";
		}
		return sql;
	}
	
	@Override
	public List<Map<String, Object>> cancelProjFooter(boolean hide,PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### cancel query footer start : "+new Date());
		String sql="select (case when sum(c.account_amt) is null then 0 else sum(c.account_amt) end) as account_amt,(case when sum(p.pay_amt_e_tax) is null then 0 else sum(p.pay_amt_e_tax) end) as pay_amt_e_tax from t_cancel_proj t left join t_cost c on t.project_code = c.project_code "
				+ "left join t_payment p on t.project_code = p.project_code ";
		if(hide)
		{
			sql+="where account_amt <> 0 or pay_amt_e_tax <> 0";
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			mapList.get(0).put("project_code", "合计");
		}
		System.out.println("###### cancel query footer start : "+new Date());
		return mapList;
	}

	@Override
	public Map<String, Object> supplierGrid(String project_code,String supplier_name,boolean is_all,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
        System.out.println("###### supplierGrid start : "+sdf.format(new Date()));
		String sql=supplierSql(project_code,supplier_name,is_all,cancelStock,noMaterial,org_id);
		if(ph!=null)
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		}
		else
		{
			sql+="order by project_code";
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		/*for(Map<String,Object> map:mapList)
		{
			BigDecimal account=(BigDecimal) map.get("account_amt");
			BigDecimal pay_amt_e_tax=(BigDecimal) map.get("pay_amt_e_tax");
			map.put("dvalue", account.subtract(pay_amt_e_tax));
		}*/
		System.out.println("###### supplierGrid end : "+sdf.format(new Date()));
		return result;
	}

	@Override
	public Long supplierCount(String project_code,boolean is_all,boolean cancelStock,boolean noMaterial,PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		/*String sql="select count(distinct p.order_code,p.supplier_name,p.account_name) from t_payment p where p.org_id = '"+org_id+"' ";
		if(is_all)
		{
			sql+=" and p.project_code in (select t.project_code from t_cancel_proj t) ";
		}
		else
		{
			sql+=" and p.project_code = '"+project_code+"' ";
		}*/
		System.out.println("###### supplierCount start : "+sdf.format(new Date()));
		String sql="select count(*) from ("+supplierSql(project_code,"",is_all,cancelStock,noMaterial,org_id)+") m";
		Long result=jdbcTemplate.queryForObject(sql,Long.class);
		System.out.println("###### supplierCount end : "+sdf.format(new Date()));
		return result;
	}

	@Override
	public List<Map<String, Object>> supplierFooter(String project_code,boolean is_all,PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String sql="select (case when sum(m.pay_amt_e_tax) is null then 0 else sum(m.pay_amt_e_tax) end) as pay_amt_e_tax,"
				+ "(case when m.account_amt is null then 0 else sum(m.account_amt) end) as account_amt,"
				+ "((case when sum(m.pay_amt_e_tax) is null then 0 else sum(m.pay_amt_e_tax) end) - (case when m.account_amt is null then 0 else sum(m.account_amt) end)) as dvalue "
				+ "from (select p.supplier_name,p.supplier_code,p.order_code,p.account_name,p.pay_amt_e_tax,c.account_amt from t_payment p "
				+ "left join t_cost c on p.project_code=c.project_code and p.supplier_code=c.supplier_code and p.account_name=c.account_name where ";
		if(is_all)
		{
			sql+="p.project_code in (select p.project_code from t_cancel_proj p) ";
		}
		else
		{
			sql+="p.project_code = '"+project_code+"' ";
		}
		sql+=") m left join t_supplier_adj s on m.supplier_code=s.s_supplier_code or m.supplier_code=s.t_supplier_code ";
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			mapList.get(0).put("supplier_name", "合计");
		}
		return mapList;
	}

	private String supplierSql(String project_code,String supplier_name,boolean is_all,boolean cancelStock,boolean noMaterial,String org_id)
	{
		String sql="select t.order_code,t.project_code,t.supplier_name,t.account_name,t.month,t.voucher_no,t.pay_amt_e_tax,format(t.pay_amt_e_tax,2) as pay_amt_e_tax_txt "
			+ "from t_payment t where t.org_id = '"+org_id+"' ";
		if(is_all)
		{
			sql+=" and  exists (select 1 from t_cancel_proj p where org_id = '"+org_id+"' and p.project_code = t.project_code ) ";
		}
		else
		{
			sql+=" and t.project_code = '"+project_code+"' and t.supplier_name in ( select s.supplier_name from t_supplier s where s.org_id = '"+org_id+"' and s.unique_name = '"+supplier_name+"' )";
		}
		if(cancelStock)
		{
			sql+=" and t.flag = 0 ";
			//sql+=" and not exists (select 1 from t_cost c where c.org_id = '"+org_id+"' and t.project_code = c.project_code and t.supplier_name=c.supplier_name and c.order_type like '%退库红冲%') ";
		}
		if(noMaterial)
		{
			sql+=" and t.account_name <> '物资采购订单' ";
		}
		return sql;
	}

}
