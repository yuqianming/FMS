package com.mvc.service.report.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.report.OverpayReportServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class OverpayReportServiceImpl implements OverpayReportServiceI{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String, Object> dataGrid(String startMonth, String endMonth,
			String type,boolean cancelStock,boolean cancelAudit,boolean noMaterial, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 超额付款检查汇总DataGrid开始："+sdf.format(new Date()));
		String sql=returnSql(startMonth,endMonth,type,cancelStock,cancelAudit,noMaterial);
		if(ph!=null)
		{
			//sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		}
		else
		{
			//sql+=" order by project_code asc";
		}
		System.out.println("###### datagrid sql : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		System.out.println("###### 超额付款检查汇总DataGrid结束："+sdf.format(new Date()));
		return result;
	}
	
	private String returnSql(String startMonth,String endMonth,String type,boolean cancelStock,boolean cancelAudit,boolean noMaterial)
	{
		String sql="select ";
		if("1".equals(type))
		{
			sql+=" d.org_name as group_name,";
		}
		else
		{
			sql+=" m.supplier_name as group_name,";
		}
		sql+="count(distinct case when m.account_amt is not null then m.project_code end) as cost_num,format((case when sum(m.account_amt) is null then 0 else sum(m.account_amt) end),2) as cost_amt,"
				+ "count(distinct m.project_code) as pay_num,format((case when sum(m.t_pay_amt) is null then 0 else sum(m.t_pay_amt) end),2) as pay_amt,"
				+ "format((case when sum(m.t_pay_amt) is null then 0 else sum(m.t_pay_amt) end) - (case when sum(m.account_amt) is null then 0 else sum(m.account_amt) end),2) as over_amt from ("
				+ "select t.org_id,t.project_code,t.supplier_name,sum(t.account_amt) as account_amt,sum(t.t_pay_amt) as t_pay_amt  "
				+ "from t_over_view t where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"' ";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"' ";
		}
		if(cancelStock)
		{
			sql+=" and t.flag = 0 ";
		}
		if(cancelAudit)
		{
			sql+=" and (t.order_type not like '%审计调整%' or t.order_type is null)";
		}
		if(noMaterial)
		{
			sql+=" and (t.order_code not like 'SK%' or t.order_code is null) ";
			sql+=" and t.account_name <> '物资采购订单' ";
		}
		sql+=" group by t.org_id,t.project_code,t.supplier_name) m ";
		if("1".equals(type))
		{
			sql+=" left join department_info d on m.org_id = d.org_id ";
		}
		sql+=" where m.t_pay_amt > 0 and m.t_pay_amt > (case when m.account_amt is null then 0 else m.account_amt end) ";
		if("1".equals(type))
		{
			sql+=" group by m.org_id";
		}
		else
		{
			sql+=" group by m.supplier_name";
		}
		return sql;
	}

}
