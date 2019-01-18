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
import com.mvc.service.report.CancelReportServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class CancelReportServiceImpl implements CancelReportServiceI{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String, Object> dataGrid(String startMonth, String endMonth,
			String type,boolean hide,boolean cancelStock,boolean noMaterial, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 销项检查汇总DataGrid开始："+sdf.format(new Date()));
		String sql=returnSql(startMonth,endMonth,type,hide,cancelStock,noMaterial);
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
		System.out.println("###### 销项检查汇总DataGrid结束："+sdf.format(new Date()));
		return result;
	}
	
	private String returnSql(String startMonth,String endMonth,String type,boolean hide,boolean cancelStock,boolean noMaterial)
	{
		String sql="select ";
		if("1".equals(type))
		{
			sql+=" (select count(distinct project_code) from t_cancel_proj where org_id = a.org_id) as cancel_num,d.org_name as group_name,";
		}
		else
		{
			sql+=" '' as cancel_num,a.supplier_name as group_name,";
		}
		sql+="a.cost_num,format(a.cost_amt,2) as cost_amt,(case when b.pay_num is null then 0 else b.pay_num end) as pay_num,"
			+ "(case when b.pay_amt is null then 0 else format(b.pay_amt,2) end) as pay_amt from (select ";
		if("1".equals(type))
		{
			sql+="t.org_id,";
		}
		else
		{
			sql+="t.supplier_name,";
		}
		sql+="count(distinct t.project_code) as cost_num,sum(t.account_amt) as cost_amt from t_cost t inner join "
			+ " t_cancel_proj c on c.org_id = t.org_id and c.project_code = t.project_code where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" group by ";
		if("1".equals(type))
		{
			sql+="t.org_id ";
		}
		else
		{
			sql+="t.supplier_name ";
		}
		sql+=") a left join (select ";
		if("1".equals(type))
		{
			sql+="t.org_id,";
		}
		else
		{
			sql+="t.supplier_name,";
		}
		sql+="count(distinct t.project_code) as pay_num,sum(t.pay_amt_e_tax) as pay_amt from t_payment t inner join  "
				+ "t_cancel_proj c on c.org_id = t.org_id and c.project_code = t.project_code where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		if(cancelStock)
		{
			/*sql+=" and not exists(select 1 from t_cost c where c.org_id = t.org_id and c.project_code = t.project_code and c.supplier_name = t.supplier_name and c.account_name = t.account_name ";
			if(StringUtils.hasText(startMonth))
			{
				sql+=" and t.month >= '"+startMonth+"'";
			}
			if(StringUtils.hasText(endMonth))
			{
				sql+=" and t.month <= '"+endMonth+"'";
			}
			sql+=")";*/
			sql+=" and t.flag = 0 ";
		}
		if(noMaterial)
		{
			sql+=" and t.account_name <> '物资采购订单' ";
		}
		sql+=" group by ";
		if("1".equals(type))
		{
			sql+="t.org_id ";
		}
		else
		{
			sql+="t.supplier_name ";
		}
		sql+=") b on ";
		if("1".equals(type))
		{
			sql+="a.org_id =b.org_id left join department_info d on d.org_id = a.org_id";
		}
		else
		{
			sql+="a.supplier_name =b.supplier_name ";
		}
		if(hide)
		{
			sql+=" where a.cost_amt <> 0 or (case when b.pay_amt is null then 0 else b.pay_amt end) <> 0";
		}
		return sql;
	}

}
