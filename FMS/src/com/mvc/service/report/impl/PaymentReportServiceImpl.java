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
import com.mvc.service.report.PaymentReportServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class PaymentReportServiceImpl implements PaymentReportServiceI{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String, Object> dataGrid(String startMonth, String endMonth,
			String type, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 已付款情况统计DataGrid开始："+sdf.format(new Date()));
		String sql=returnSql(startMonth,endMonth,type);
		if(ph!=null)
		{
			sql+=" order by convert(" + ph.getSort() + " using gbk) " + ph.getOrder();
		}
		else
		{
			sql+=" order by convert(group_name using gbk) asc";
		}
		System.out.println("###### datagrid sql : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		System.out.println("###### 已付款情况统计DataGrid结束："+sdf.format(new Date()));
		return result;
	}
	
	private String returnSql(String startMonth,String endMonth,String type)
	{
		String sql=" select ";
		if("1".equals(type))//按分公司统计
		{
			sql+="d.org_name as group_name,";
		}
		else
		{
			sql+="a.supplier_name as group_name,";
		}
		sql+="sum(a.pay_num) as pay_num,sum(a.pay_amt) as pay_amt,format(sum(a.pay_amt),2) as pay_amt_txt,sum(a.sign_num) as sign_num,sum(a.sign_amt) as sign_amt,format(sum(a.sign_amt),2) as sign_amt_txt "
		+ "from (select ";
		if("1".equals(type))//按分公司统计
		{
			sql+="p.org_id,";
		}
		else
		{
			sql+="p.supplier_name,";
		}
		sql+="count(distinct p.project_code) as pay_num,0 as sign_num,sum(p.pay_amt_e_tax) as pay_amt,0 as sign_amt from t_payment p where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and p.month >= '"+startMonth+"' ";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and p.month <= '"+endMonth+"' ";
		}
		sql+=" group by ";
		if("1".equals(type))//按分公司统计
		{
			sql+="p.org_id";
		}
		else
		{
			sql+="p.supplier_name";
		}
		sql+=" union select ";
		if("1".equals(type))//按分公司统计
		{
			sql+="s.org_id,";
		}
		else
		{
			sql+="s.supplier_name,";
		}
		sql+="0 as pay_num,count(distinct s.project_code) as sign_num,0 as pay_amt,sum(s.pay_amt_e_tax) as sign_amt from t_signature s where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and s.month >= '"+startMonth+"' ";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and s.month <= '"+endMonth+"' ";
		}
		sql+=" group by ";
		if("1".equals(type))//按分公司统计
		{
			sql+="s.org_id";
		}
		else
		{
			sql+="s.supplier_name";
		}
		sql+=") a ";
		if("1".equals(type))
		{
			sql+="left join department_info d on a.org_id = d.org_id group by a.org_id";
		}
		else
		{
			sql+="group by a.supplier_name";
		}
		return sql;
	}
}
