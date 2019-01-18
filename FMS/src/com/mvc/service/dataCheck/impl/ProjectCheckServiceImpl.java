package com.mvc.service.dataCheck.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.AmtInfo;
import com.mvc.pageModel.sys.SearchInfo;
import com.mvc.service.dataCheck.ProjectCheckServiceI;
@Service
public class ProjectCheckServiceImpl implements ProjectCheckServiceI{
	@Autowired
	JdbcTemplate jdbcTemplate;
	DecimalFormat df=new DecimalFormat(",###.##");
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public List<AmtInfo> poolGrid(String org_id) throws Exception{
		// TODO Auto-generated method stub
		List<AmtInfo> amtList=new ArrayList<AmtInfo>();
		System.out.println("###### 查询开始："+sdf.format(new Date()));
		//成本单
		AmtInfo amt=new AmtInfo();
		String sql="select format(sum(t.account_amt),2) as amt0,"
				+ "format(sum(case when t.order_code like 'PO%' then t.account_amt else 0 end),2) as amt1,"
				+ "format(sum(case when t.order_code like 'OFP%' then t.account_amt else 0 end),2) as amt2,"
				+ "format(sum(case when t.order_code like 'SK%' then t.account_amt else 0 end),2) as amt3,"
				+ "format(sum(case when t.order_code like 'SJ%' then t.account_amt else 0 end),2) as amt4,"
				+ "format(sum(case when t.order_code like '利息资本化%' then t.account_amt else 0 end),2) as amt5,"
				+ "format(sum(t.account_amt)-sum(case when t.order_code like 'PO%' then t.account_amt else 0 end)-sum(case when t.order_code like 'OFP%' then t.account_amt else 0 end)"
				+ "-sum(case when t.order_code like 'SK%' then t.account_amt else 0 end)-sum(case when t.order_code like 'SJ%' then t.account_amt else 0 end)"
				+ "-sum(case when t.order_code like '利息资本化%' then t.account_amt else 0 end),2) as amt6 "
				+ "from t_cost t "
				+ "where t.org_id = '"+org_id+"'";
		Map<String,Object> m=jdbcTemplate.queryForMap(sql);
		if(m!=null)
		{
			amt.setAmt_type("成本单");
			if(m.get("amt0")!=null)
			{
				amt.setAmt_total(m.get("amt0").toString());
				amt.setAmt_po(m.get("amt1").toString());
				amt.setAmt_ofp(m.get("amt2").toString());
				amt.setAmt_sk(m.get("amt3").toString());
				amt.setAmt_sj(m.get("amt4").toString());
				amt.setAmt_zb(m.get("amt5").toString());
				amt.setAmt_other(m.get("amt6").toString());
				amtList.add(amt);
			}
		}
		
		//已付款
		amt=new AmtInfo();
		sql="select format(sum(t.pay_amt),2) as amt0,"
				+ "format(sum(case when t.order_code like 'PO%' then t.pay_amt else 0 end),2) as amt1,"
				+ "format(sum(case when t.order_code like 'OFP%' then t.pay_amt else 0 end),2) as amt2,"
				+ "format(sum(case when t.order_code like 'SK%' then t.pay_amt else 0 end),2) as amt3,"
				+ "format(sum(case when t.order_code like 'SJ%' then t.pay_amt else 0 end),2) as amt4,"
				+ "format(sum(case when t.order_code like '利息资本化%' then t.pay_amt else 0 end),2) as amt5,"
				+ "format(sum(t.pay_amt)-sum(case when t.order_code like 'PO%' then t.pay_amt else 0 end)-sum(case when t.order_code like 'OFP%' then t.pay_amt else 0 end)"
				+ "-sum(case when t.order_code like 'SK%' then t.pay_amt else 0 end)-sum(case when t.order_code like 'SJ%' then t.pay_amt else 0 end)"
				+ "-sum(case when t.order_code like '利息资本化%' then t.pay_amt else 0 end),2) as amt6 "
				+ "from t_payment t "
				+ "where t.org_id = '"+org_id+"'";
		m=jdbcTemplate.queryForMap(sql);
		if(m!=null)
		{
			amt.setAmt_type("已付款");
			if(m.get("amt0")!=null)
			{
				amt.setAmt_total(m.get("amt0").toString());
				amt.setAmt_po(m.get("amt1").toString());
				amt.setAmt_ofp(m.get("amt2").toString());
				amt.setAmt_sk(m.get("amt3").toString());
				amt.setAmt_sj(m.get("amt4").toString());
				amt.setAmt_zb(m.get("amt5").toString());
				amt.setAmt_other(m.get("amt6").toString());
				amtList.add(amt);
			}
		}
		
		//签字未付款
		amt=new AmtInfo();
		sql="select format(sum(t.pay_amt),2) as amt0,"
				+ "format(sum(case when t.order_code like 'PO%' then t.pay_amt else 0 end),2) as amt1,"
				+ "format(sum(case when t.order_code like 'OFP%' then t.pay_amt else 0 end),2) as amt2,"
				+ "format(sum(case when t.order_code like 'SK%' then t.pay_amt else 0 end),2) as amt3,"
				+ "format(sum(case when t.order_code like 'SJ%' then t.pay_amt else 0 end),2) as amt4,"
				+ "format(sum(case when t.order_code like '利息资本化%' then t.pay_amt else 0 end),2) as amt5,"
				+ "format(sum(t.pay_amt)-sum(case when t.order_code like 'PO%' then t.pay_amt else 0 end)-sum(case when t.order_code like 'OFP%' then t.pay_amt else 0 end)"
				+ "-sum(case when t.order_code like 'SK%' then t.pay_amt else 0 end)-sum(case when t.order_code like 'SJ%' then t.pay_amt else 0 end)"
				+ "-sum(case when t.order_code like '利息资本化%' then t.pay_amt else 0 end),2) as amt6 "
				+ "from t_signature t "
				+ "where t.org_id = '"+org_id+"'";
		m=jdbcTemplate.queryForMap(sql);
		if(m!=null)
		{
			amt.setAmt_type("已签字未付款");
			if(m.get("amt0")!=null)
			{
				amt.setAmt_total(m.get("amt0").toString());
				amt.setAmt_po(m.get("amt1").toString());
				amt.setAmt_ofp(m.get("amt2").toString());
				amt.setAmt_sk(m.get("amt3").toString());
				amt.setAmt_sj(m.get("amt4").toString());
				amt.setAmt_zb(m.get("amt5").toString());
				amt.setAmt_other(m.get("amt6").toString());
				amtList.add(amt);
			}
		}
		/*BigDecimal amt_total=new BigDecimal(0);
		BigDecimal amt_goal=new BigDecimal(0);
		BigDecimal amt_sum=new BigDecimal(0);
		String sql="select (case when sum(account_amt) is null then 0 else sum(account_amt) end) as account_amt from t_cost where org_id = '"+org_id+"'";
		Map<String,Object> m=jdbcTemplate.queryForMap(sql);
		if(m!=null)
		{
			amt_total=(BigDecimal) m.get("account_amt");
		}
		amt.setAmt_type("成本单");
		amt.setAmt_total(df.format(amt_total));
		System.out.println("###### 查询成本单总金额结束："+sdf.format(new Date()));
		sql="select o.type as type,sum(t.account_amt) as account_amt from order_type o left join t_cost t on t.order_code like CONCAT(o.type,'%') and t.org_id = '"+org_id+"' GROUP BY o.type order by o.id";
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			for(Map<String,Object> map:mapList)
			{
				amt_goal=map.get("account_amt")==null?new BigDecimal(0):(BigDecimal) map.get("account_amt");
				amt_sum=amt_sum.add(amt_goal);
				if("PO".equals(map.get("type")))
				{
					amt.setAmt_po(df.format(amt_goal));
				}
				else if("OFP".equals(map.get("type")))
				{
					amt.setAmt_ofp(df.format(amt_goal));
				}
				else if("SK".equals(map.get("type")))
				{
					amt.setAmt_sk(df.format(amt_goal));
				}
				else if("SJ".equals(map.get("type")))
				{
					amt.setAmt_sj(df.format(amt_goal));
				}
				else if("利息资本化".equals(map.get("type")))
				{
					amt.setAmt_zb(df.format(amt_goal));
				}
			}
		}
		BigDecimal amt_other=amt_total.subtract(amt_sum);
		amt.setAmt_other(df.format(amt_other));
		amtList.add(amt);
		System.out.println("###### 查询成本单结束："+sdf.format(new Date()));
		//已付款
		amt=new AmtInfo();
		amt_total=new BigDecimal(0);
		amt_goal=new BigDecimal(0);
		amt_sum=new BigDecimal(0);
		sql="select (case when sum(pay_amt) is null then 0 else sum(pay_amt) end) as pay_amt from t_payment where org_id = '"+org_id+"'";
		m=jdbcTemplate.queryForMap(sql);
		if(m!=null)
		{
			amt_total=(BigDecimal) m.get("pay_amt");
		}
		amt.setAmt_type("已付款");
		amt.setAmt_total(df.format(amt_total));
		System.out.println("###### 查询付款总金额结束："+sdf.format(new Date()));
		sql="select o.type as type,sum(t.pay_amt) as pay_amt from order_type o left join t_payment t on t.order_code like CONCAT(o.type,'%') and t.org_id = '"+org_id+"' GROUP BY o.type order by o.id";
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			for(Map<String,Object> map:mapList)
			{
				amt_goal=map.get("pay_amt")==null?new BigDecimal(0):(BigDecimal) map.get("pay_amt");
				amt_sum=amt_sum.add(amt_goal);
				if("PO".equals(map.get("type")))
				{
					amt.setAmt_po(df.format(amt_goal));
				}
				else if("OFP".equals(map.get("type")))
				{
					amt.setAmt_ofp(df.format(amt_goal));
				}
				else if("SK".equals(map.get("type")))
				{
					amt.setAmt_sk(df.format(amt_goal));
				}
				else if("SJ".equals(map.get("type")))
				{
					amt.setAmt_sj(df.format(amt_goal));
				}
				else if("利息资本化".equals(map.get("type")))
				{
					amt.setAmt_zb(df.format(amt_goal));
				}
			}
		}
		amt_other=amt_total.subtract(amt_sum);
		amt.setAmt_other(df.format(amt_other));
		amtList.add(amt);
		System.out.println("###### 查询付款结束："+sdf.format(new Date()));
		//已签字未付款
		amt=new AmtInfo();
		amt_total=new BigDecimal(0);
		amt_goal=new BigDecimal(0);
		amt_sum=new BigDecimal(0);
		sql="select (case when sum(pay_amt) is null then 0 else sum(pay_amt) end) as pay_amt from t_signature where org_id = '"+org_id+"'";
		m=jdbcTemplate.queryForMap(sql);
		if(m!=null)
		{
			amt_total=(BigDecimal) m.get("pay_amt");
		}
		amt.setAmt_type("已签字未付款");
		amt.setAmt_total(df.format(amt_total));
		System.out.println("###### 查询签字未付款总金额结束："+sdf.format(new Date()));
		sql="select o.type as type,sum(t.pay_amt) as pay_amt from order_type o left join t_signature t on t.order_code like CONCAT(o.type,'%') and t.org_id = '"+org_id+"' GROUP BY o.type order by o.id";
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			for(Map<String,Object> map:mapList)
			{
				amt_goal=map.get("pay_amt")==null?new BigDecimal(0):(BigDecimal) map.get("pay_amt");
				amt_sum=amt_sum.add(amt_goal);
				if("PO".equals(map.get("type")))
				{
					amt.setAmt_po(df.format(amt_goal));
				}
				else if("OFP".equals(map.get("type")))
				{
					amt.setAmt_ofp(df.format(amt_goal));
				}
				else if("SK".equals(map.get("type")))
				{
					amt.setAmt_sk(df.format(amt_goal));
				}
				else if("SJ".equals(map.get("type")))
				{
					amt.setAmt_sj(df.format(amt_goal));
				}
				else if("利息资本化".equals(map.get("type")))
				{
					amt.setAmt_zb(df.format(amt_goal));
				}
			}
		}
		amt_other=amt_total.subtract(amt_sum);
		amt.setAmt_other(df.format(amt_other));
		amtList.add(amt);*/
		System.out.println("###### 查询结束："+sdf.format(new Date()));
		return amtList;
	}
	@Override
	public List<Map<String, Object>> monthGrid(SearchInfo info, PageFilter ph,String org_id)
			throws Exception {
		// TODO Auto-generated method stub
		String sql=monthSql("grid",info,org_id)+" group by month order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		System.out.println("###### sql : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}
	@Override
	public Long monthCount(SearchInfo info, PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		String sql=monthSql("count",info,org_id);
		return jdbcTemplate.queryForObject(sql,Long.class);
	}
	
	private String monthSql(String type,SearchInfo info,String org_id)
	{
		String sql="";
		if("grid".equals(type))
		{
			if("t_cost".equals(info.getTableName()))
			{
				sql+="select left(DATE_FORMAT(busi_date,'%Y%m%d'),6) as month,format(sum(account_amt),2) as amt_e_tax from t_cost ";
			}
			else if("t_payment".equals(info.getTableName()))
			{
				sql+="select month,format(sum(pay_amt),2) as amt,format(sum(pay_amt_e_tax),2) as amt_e_tax from t_payment ";
			}
			else if("t_signature".equals(info.getTableName()))
			{
				sql+="select substring(batch_no,3,6) month,format(sum(pay_amt),2) as amt,format(sum(pay_amt_e_tax),2) as amt_e_tax from t_signature ";
			}
		}
		else if("count".equals(type))
		{
			if("t_cost".equals(info.getTableName()))
			{
				sql+="select count(distinct left(DATE_FORMAT(busi_date,'%Y%m%d'),6)) from t_cost ";
			}
			else if("t_payment".equals(info.getTableName()))
			{
				sql+="select count(distinct month) from t_payment ";
			}
			else if("t_signature".equals(info.getTableName()))
			{
				sql+="select count(distinct substring(batch_no,3,6)) from t_signature ";
			}
		}
		
		if("amt_total".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"'";
		}
		else if("amt_po".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'po%'";
		}
		else if("amt_ofp".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'ofp%'";
		}
		else if("amt_sk".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'sk%'";
		}
		else if("amt_sj".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'sj%'";
		}
		else if("amt_zb".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like '利息资本化%'";
		}
		else if("amt_other".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code NOT REGEXP 'po|ofp|sk|sj|利息资本化'";
		}
		return sql;
	}
	@Override
	public List<Map<String, Object>> categoryGrid(SearchInfo info, PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		String sql=categorySql(info,org_id);
		if(ph==null)
		{
			sql+=" order by category";		
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		System.out.println("###### sql : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}
	@Override
	public Long categoryCount(SearchInfo info, PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		String sql="select count(*) from ("+categorySql(info,org_id)+") m";
		return jdbcTemplate.queryForObject(sql,Long.class);
	}
	
	private String categorySql(SearchInfo info,String org_id)
	{
		String sql="";
		if("t_cost".equals(info.getTableName()))
		{
			sql+="select supplier_name as category,format(sum(account_amt),2) as amt_e_tax from t_cost ";
		}
		else if("t_payment".equals(info.getTableName()))
		{
			sql+="select voucher_no as category,format(sum(pay_amt),2) as amt,format(sum(pay_amt_e_tax),2) as amt_e_tax from t_payment ";
		}
		else if("t_signature".equals(info.getTableName()))
		{
			sql+="select batch_no,supplier_name as category,format(sum(pay_amt),2) as amt,format(sum(pay_amt_e_tax),2) as amt_e_tax from t_signature ";
		}
		
		if("amt_total".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"'";
		}
		else if("amt_po".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'po%'";
		}
		else if("amt_ofp".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'ofp%'";
		}
		else if("amt_sk".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'sk%'";
		}
		else if("amt_sj".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like 'sj%'";
		}
		else if("amt_zb".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code like '利息资本化%'";
		}
		else if("amt_other".equals(info.getOrderStart()))
		{
			sql+="where org_id = '"+org_id+"' and order_code NOT REGEXP 'po|ofp|sk|sj|利息资本化'";
		}
		
		if(!"all".equals(info.getMonth()))
		{
			if("t_cost".equals(info.getTableName()))
			{
				sql+=" and left(DATE_FORMAT(busi_date,'%Y%m%d'),6) = '"+info.getMonth()+"' ";
			}
			else if("t_payment".equals(info.getTableName()))
			{
				sql+=" and month = '"+info.getMonth()+"' ";
			}
			else if("t_signature".equals(info.getTableName()))
			{
				sql+=" and substring(batch_no,3,6) = '"+info.getMonth()+"' ";
			}
		}
		
		if("t_signature".equals(info.getTableName()))
		{
			sql+=" group by batch_no,category ";
		}
		else if("t_cost".equals(info.getTableName()))
		{
			sql+=" group by supplier_code ";
		}
		else 
		{
			sql+=" group by category ";
		}
		return sql;
	}
	@Override
	public List<Map<String, Object>> detailGrid(SearchInfo info, PageFilter ph,String org_id)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="";
		if(ph==null)
		{
			sql=detailSql("grid",info,org_id)+" order by project_code";
		}
		else
		{
			sql=detailSql("grid",info,org_id)+" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		System.out.println("###### sql : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}
	@Override
	public Long detailCount(SearchInfo info, PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		String sql=detailSql("count",info,org_id);
		return jdbcTemplate.queryForObject(sql,Long.class);
	}

	private String detailSql(String type,SearchInfo info,String org_id)
	{
		String sql="";
		if("grid".equals(type))
		{
			if("t_cost".equals(info.getTableName()))
			{
				sql+="select project_code,supplier_name,format(account_amt,2) as amt from t_cost ";
			}
			else if("t_payment".equals(info.getTableName()))
			{
				sql+="select project_code,supplier_name,format(sum(pay_amt),2) as amt from t_payment ";
			}
			else if("t_signature".equals(info.getTableName()))
			{
				sql+="select project_code,supplier_name,format(sum(pay_amt),2) as amt from t_signature ";
			}
		}
		else if("count".equals(type))
		{
			if("t_cost".equals(info.getTableName()))
			{
				sql+="select count(*) from t_cost ";
			}
			else if("t_payment".equals(info.getTableName()))
			{
				sql+="select count(distinct project_code,supplier_name) from t_payment ";
			}
			else if("t_signature".equals(info.getTableName()))
			{
				sql+="select count(distinct project_code,supplier_name) from t_signature ";
			}
		}
		sql+="where org_id = '"+org_id+"' ";
		if("amt_total".equals(info.getOrderStart()))
		{
			sql+="";
		}
		else if("amt_po".equals(info.getOrderStart()))
		{
			sql+="and order_code like 'po%'";
		}
		else if("amt_ofp".equals(info.getOrderStart()))
		{
			sql+="and order_code like 'ofp%'";
		}
		else if("amt_sk".equals(info.getOrderStart()))
		{
			sql+="and order_code like 'sk%'";
		}
		else if("amt_sj".equals(info.getOrderStart()))
		{
			sql+="and order_code like 'sj%'";
		}
		else if("amt_zb".equals(info.getOrderStart()))
		{
			sql+="and order_code like '利息资本化%'";
		}
		else if("amt_other".equals(info.getOrderStart()))
		{
			sql+="and order_code NOT REGEXP 'po|ofp|sk|sj|利息资本化'";
		}
		
		if(!"all".equals(info.getMonth()))
		{
			if("t_cost".equals(info.getTableName()))
			{
				sql+=" and left(DATE_FORMAT(busi_date,'%Y%m%d'),6) = '"+info.getMonth()+"' ";
			}
			else if("t_payment".equals(info.getTableName()))
			{
				sql+=" and month = '"+info.getMonth()+"' ";
			}
			else if("t_signature".equals(info.getTableName()))
			{
				sql+=" and substring(batch_no,3,6) = '"+info.getMonth()+"' ";
			}
		}
		
		if(!"all".equals(info.getCategory()))
		{
			if("t_cost".equals(info.getTableName()))
			{
				sql+=" and supplier_name = '"+info.getCategory()+"'";
			}
			else if("t_payment".equals(info.getTableName()))
			{
				sql+=" and voucher_no = '"+info.getCategory()+"'";
			}
			else if("t_signature".equals(info.getTableName()))
			{
				sql+=" and supplier_name = '"+info.getCategory()+"' and batch_no = '"+info.getBatchNo()+"'";
			}
		}
		
		if("grid".equals(type))
		{
			if("t_payment".equals(info.getTableName())||"t_signature".equals(info.getTableName()))
			{
				sql+=" group by project_code,supplier_name";
			}
		}
		return sql;
	}
}
