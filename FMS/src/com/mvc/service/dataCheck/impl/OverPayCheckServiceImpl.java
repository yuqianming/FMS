package com.mvc.service.dataCheck.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dataCheck.OverPayCheckServiceI;
import com.mvc.utils.PackResultUtils;
@Service
public class OverPayCheckServiceImpl implements OverPayCheckServiceI{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String, Object> supplierGrid(boolean cancelStock,
			boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### OverPayCheckServiceImpl supplierGrid start : "+sdf.format(new Date()));
		String sql=supplierSql(cancelStock,cancelAudit,noMaterial,deviation,org_id);
		if(ph==null)
		{
			sql+=" order by project_code";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder();
			//sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		System.out.println("###### OverPayCheckServiceImpl supplierGrid end : "+sdf.format(new Date()));
		return result;
	}

	@Override
	public Long supplierCount(boolean cancelStock, boolean cancelAudit,boolean noMaterial,BigDecimal deviation,
			PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### OverPayCheckServiceImpl supplierCount start : "+sdf.format(new Date()));
		String sql="select count(*)  from ("+supplierSql(cancelStock,cancelAudit,noMaterial,deviation,org_id)+") x";
		Long count=jdbcTemplate.queryForObject(sql,Long.class);
		System.out.println("###### OverPayCheckServiceImpl supplierCount end : "+sdf.format(new Date()));
		return count;
	}

	@Override
	public List<Map<String, Object>> supplierFooter(boolean cancelStock,
			boolean cancelAudit, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		//String sql="select sum(x.account_amt) as account_amt,sum(x.pay_amt_e_tax) as pay_amt_e_tax,sum(x.dvalue) as dvalue from ("+supplierSql(cancelStock,cancelAudit,new BigDecimal(0),"")+") x";
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList("");
		if(mapList!=null&&mapList.size()>0)
		{
			mapList.get(0).put("supplier_name", "合计");
		}
		return mapList;
	}

	private String supplierSql(boolean cancelStock, boolean cancelAudit,boolean noMaterial,BigDecimal deviation,String org_id)
	{
		String sql="select a.supplier_name,a.project_code,"
				+ "(case when a.pay_amt_e_tax is null then 0 else a.pay_amt_e_tax end) as pay_amt_e_tax,format((case when a.pay_amt_e_tax is null then 0 else a.pay_amt_e_tax end),2) as pay_amt_e_tax_txt,"
				+ "(case when b.account_amt is null then 0 else b.account_amt end) as account_amt,format((case when b.account_amt is null then 0 else b.account_amt end),2) as account_amt_txt,format(((case when b.account_amt is null then 0 else b.account_amt end) - a.pay_amt_e_tax),2) as dvalue "
				+ "from (select t.project_code,t.supplier_name,sum(t.pay_amt_e_tax) as pay_amt_e_tax from t_payment t where t.org_id = '"+org_id+"' ";
		if(cancelStock)
		{
			sql+=" and t.flag = 0";
			//sql+=" and not exists (select 1 from t_cost c where c.org_id = '"+org_id+"' and c.project_code = t.project_code and c.supplier_name=t.supplier_name and c.account_name=t.account_name and c.order_type like '%退库红冲%') ";
		}
		if(noMaterial)
		{
			sql+=" and t.account_name <> '物资采购订单' ";
		}
		sql+="  group by t.project_code,t.supplier_name) a left join "
		+ "(select c.project_code,c.supplier_name,sum(c.account_amt) as account_amt from t_cost c where c.org_id = '"+org_id+"'  ";
		if(cancelAudit)
		{
			sql+=" and c.order_type not like '%审计调整%' ";
		}
		if(noMaterial)
		{
			sql+=" and c.order_code not like 'SK%' ";
		}
		sql+=" group by c.project_code,c.supplier_name) b on a.supplier_name=b.supplier_name and a.project_code=b.project_code "
				+ "where a.pay_amt_e_tax>0 and (case when b.account_amt is null then 0 else b.account_amt end) - a.pay_amt_e_tax<0 ";
		if(deviation!=null)
		{
			sql+=" and abs((case when b.account_amt is null then 0 else b.account_amt end) - a.pay_amt_e_tax) > "+ deviation;
		}
		System.out.println("###### sql : "+sql);
		return sql;
	}

	@Override
	public Map<String, Object> payGrid(String supplier_name,
			String project_code, boolean is_all,boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,String org_id)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### OverPayCheckServiceImpl payGrid start : "+sdf.format(new Date()));
		String sql=paySql(supplier_name,project_code,is_all,cancelStock,noMaterial,cancelAudit,deviation,org_id);
		if(ph!=null)
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		}
		else
		{
			sql+=" order by order_code";
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		System.out.println("###### OverPayCheckServiceImpl payGrid end : "+sdf.format(new Date()));
		return result;
	}

	@Override
	public Long payCount(String supplier_name, String project_code,
			boolean is_all,boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		/*String sql="select count(distinct t.voucher_no,t.account_name) from t_payment t ";
		if(!is_all)
		{
			sql+=" where t.project_code='"+project_code+"' and t.supplier_name='"+supplier_name+"'";
		}*/
		System.out.println("###### OverPayCheckServiceImpl payCount start : "+sdf.format(new Date()));
		String sql="select count(*) from ("+paySql(supplier_name,project_code,is_all,cancelStock,cancelAudit,noMaterial,deviation,org_id)+") m";
		Long result=jdbcTemplate.queryForObject(sql,Long.class);
		System.out.println("###### OverPayCheckServiceImpl payCount end : "+sdf.format(new Date()));
		return result;
	}

	@Override
	public List<Map<String, Object>> payFooter(String supplier_name,
			String project_code, boolean is_all, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="select sum(invoice_amt) as invoice_amt,sum(pay_amt) as pay_amt,sum(pay_amt_e_tax) as pay_amt_e_tax from t_payment ";
		if(!is_all)
		{
			sql+=" where project_code='"+project_code+"' and supplier_name='"+supplier_name+"'";
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			mapList.get(0).put("month", "合计");
		}
		return mapList;
	}
	
	private String paySql(String supplier_name, String project_code,boolean is_all,boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,String org_id)
	{
		String sql="";
		if(is_all)
		{
			/*sql="select d.month,d.voucher_no,d.account_name,d.order_code,"
					+ "(case when sum(d.invoice_amt) is null then 0 else sum(d.invoice_amt) end) as invoice_amt,format((case when sum(d.invoice_amt) is null then 0 else sum(d.invoice_amt) end),2) as invoice_amt_txt,"
					+ "(case when sum(d.pay_amt) is null then 0 else sum(d.pay_amt) end) as pay_amt,format((case when sum(d.pay_amt) is null then 0 else sum(d.pay_amt) end),2) as pay_amt_txt,"
					+ "(case when sum(d.pay_amt_e_tax) is null then 0 else sum(d.pay_amt_e_tax) end) as pay_amt_e_tax,format((case when sum(d.pay_amt_e_tax) is null then 0 else sum(d.pay_amt_e_tax) end),2) as pay_amt_e_tax_txt "
					+ "from (select a.supplier_name,a.project_code,"
					+ "(case when a.pay_amt_e_tax is null then 0 else a.pay_amt_e_tax end) as pay_amt_e_tax,format((case when a.pay_amt_e_tax is null then 0 else a.pay_amt_e_tax end),2) as pay_amt_e_tax_txt,"
					+ "(case when b.account_amt is null then 0 else b.account_amt end) as account_amt,format((case when b.account_amt is null then 0 else b.account_amt end),2) as account_amt_txt,format(((case when b.account_amt is null then 0 else b.account_amt end) - a.pay_amt_e_tax),2) as dvalue "
					+ "from (select t.supplier_name,t.project_code,sum(t.pay_amt_e_tax) as pay_amt_e_tax from t_payment t where t.org_id = '"+org_id+"'"
			        +"  group by t.supplier_name,t.project_code) a left join "
					+ "(select cost.supplier_name,cost.project_code,sum(cost.account_amt) as account_amt from t_cost cost where cost.org_id = '"+org_id+"' ";
			if(cancelStock)
			{
				sql+=" and cost.order_type not like '%退库红冲%' ";
			}
			if(cancelAudit)
			{
				sql+=" and cost.order_type not like '%审计调整%' ";
			}
			sql+=" group by cost.supplier_name,cost.project_code) b on a.supplier_name=b.supplier_name and a.project_code=b.project_code "
				 + "where a.pay_amt_e_tax>0 and (case when b.account_amt is null then 0 else b.account_amt end) - a.pay_amt_e_tax<0 ";
			if(deviation!=null)
			{
				sql+=" and abs((case when b.account_amt is null then 0 else b.account_amt end) - a.pay_amt_e_tax<0) > "+deviation;
			}
			sql+=") c inner join t_payment d on c.supplier_name = d.supplier_name and c.project_code = d.project_code "
			     +" group by d.voucher_no,d.account_name";*/
			sql+=" SELECT a. MONTH,a.voucher_no,a.account_name,a.order_code,(CASE WHEN sum(a.invoice_amt) IS NULL THEN 0 ELSE sum(a.invoice_amt) END) AS invoice_amt,"
					+ "format((CASE WHEN sum(a.invoice_amt) IS NULL THEN 0 ELSE sum(a.invoice_amt) END),2) AS invoice_amt_txt,(CASE WHEN sum(a.pay_amt) IS NULL THEN 0 "
					+ "ELSE sum(a.pay_amt) END ) AS pay_amt,format((CASE WHEN sum(a.pay_amt) IS NULL THEN 0 ELSE sum(a.pay_amt) END),2) AS pay_amt_txt,(CASE "
					+ "WHEN sum(a.pay_amt_e_tax) IS NULL THEN 0 ELSE sum(a.pay_amt_e_tax) END) AS pay_amt_e_tax,format((CASE WHEN sum(a.pay_amt_e_tax) IS NULL THEN 0 "
					+ "ELSE sum(a.pay_amt_e_tax) END),2) AS pay_amt_e_tax_txt FROM ( SELECT t.supplier_name,t.project_code,t. MONTH,t.voucher_no,t.account_name,t.order_code,"
					+ "sum(t.pay_amt_e_tax) AS pay_amt_e_tax,sum(t.pay_amt) as pay_amt,sum(t.invoice_amt) as invoice_amt FROM t_payment t WHERE t.org_id = '"+org_id+"' "
					+ "GROUP BY t.supplier_name,t.project_code) a LEFT JOIN ( SELECT cost.supplier_name,cost.project_code,sum(cost.account_amt) AS account_amt FROM "
					+ "t_cost cost WHERE cost.org_id = '"+org_id+"'";
			if(cancelStock)
			{
				sql+=" and cost.order_type not like '%退库红冲%' ";
			}
			if(cancelAudit)
			{
				sql+=" and cost.order_type not like '%审计调整%' ";
			}
			sql+="GROUP BY cost.supplier_name,cost.project_code) b ON a.supplier_name = b.supplier_name AND a.project_code = b.project_code WHERE a.pay_amt_e_tax > 0 "
				+ "AND (CASE WHEN b.account_amt IS NULL THEN 0 ELSE b.account_amt END) - a.pay_amt_e_tax < 0 ";
			if(deviation!=null)
			{
				sql+=" and abs((case when b.account_amt is null then 0 else b.account_amt end) - a.pay_amt_e_tax) > "+ deviation;
			}
			sql+=" group by a.account_name,a.order_code";
		}
		else
		{
			String supplier="";
			String query="select supplier_name from t_supplier where unique_name = (select unique_name from t_supplier where supplier_name = '"+supplier_name+"' and org_id = '"+org_id+"') and org_id = '"+org_id+"'";
			List<Map<String,Object>> mapList=jdbcTemplate.queryForList(query);
			if(mapList!=null&&mapList.size()>0)
			{
				for(Map<String,Object> map:mapList)
				{
					supplier+=",'"+map.get("supplier_name")+"'";
				}
			}
			sql="select t.month,t.voucher_no,t.account_name,t.order_code,"
					+ "(case when sum(t.invoice_amt) is null then 0 else sum(t.invoice_amt) end) as invoice_amt,format((case when sum(t.invoice_amt) is null then 0 else sum(t.invoice_amt) end),2) as invoice_amt_txt,"
					+ "(case when sum(t.pay_amt) is null then 0 else sum(t.pay_amt) end) as pay_amt,format((case when sum(t.pay_amt) is null then 0 else sum(t.pay_amt) end),2) as pay_amt_txt,"
					+ "(case when sum(t.pay_amt_e_tax) is null then 0 else sum(t.pay_amt_e_tax) end) as pay_amt_e_tax,format((case when sum(t.pay_amt_e_tax) is null then 0 else sum(t.pay_amt_e_tax) end),2) as pay_amt_e_tax_txt "
					+ "from t_payment t ";
			sql+=" where t.project_code='"+project_code+"' and t.supplier_name in ("+supplier.substring(1)+") and t.org_id = '"+org_id+"'";
			if(cancelStock)
			{
				sql+=" and not exists (select 1 from t_cost c where c.org_id = '"+org_id+"' and t.order_code=c.order_code and t.supplier_name=c.supplier_name and t.account_name=c.account_name and t.project_code = c.project_code and c.order_type like '%退库红冲%') ";
			}
			if(noMaterial)
			{
				sql+=" and t.account_name <> '物资采购订单' ";
			}
			sql+=" group by t.account_name,t.order_code";
		}
		
		return sql;
	}
}
