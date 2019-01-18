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
import com.mvc.service.report.CostlReportServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class CostReportServiceImpl implements CostlReportServiceI{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String, Object> dataGrid(String startMonth,String endMonth,boolean no_cancel,String asset_trans_sts,String assemble_sts,String doc_type) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 成本单账龄分析DataGrid开始："+sdf.format(new Date()));
		String sql=returnSql(startMonth,endMonth,no_cancel,asset_trans_sts,assemble_sts,doc_type);
		System.out.println("###### datagrid sql : "+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, null);
		System.out.println("###### 成本单账龄分析DataGrid结束："+sdf.format(new Date()));
		return result;
	}
	
	private String returnSql(String startMonth,String endMonth,boolean no_cancel,String asset_trans_sts,String assemble_sts,String doc_type)
	{
		String sql="select d.org_name,a.build_mode,a.account_amt0,a.account_amt1,a.account_amt2,a.account_amt3,a.account_amt4,a.account_amt5 from ("
				+ "select c.org_id,p.build_mode,format(sum(case when datediff(now(),c.busi_date) >=0 and datediff(now(),c.busi_date)<=90 then c.account_amt else 0 end),2) as account_amt0,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=91 and datediff(now(),c.busi_date)<=180 then c.account_amt else 0 end),2) as account_amt1,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=181 and datediff(now(),c.busi_date)<=270 then c.account_amt else 0 end),2) as account_amt2,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=271 and datediff(now(),c.busi_date)<=360 then c.account_amt else 0 end),2) as account_amt3,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=361 and datediff(now(),c.busi_date)<=720 then c.account_amt else 0 end),2) as account_amt4,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=721 and datediff(now(),c.busi_date)<=9999 then c.account_amt else 0 end),2) as account_amt5 "
				+ "from t_pms p "
				+ "inner join t_cost c on p.org_id=c.org_id and p.project_code=c.project_code ";
		if(StringUtils.hasText(asset_trans_sts))
		{
			sql+=" and c.asset_trans_sts = '"+asset_trans_sts+"'";
		}
		if(StringUtils.hasText(assemble_sts))
		{
			sql+=" and c.assemble_sts = '"+assemble_sts+"'";
		}
		if(StringUtils.hasText(doc_type))
		{
			sql+=" and c.doc_type = '"+doc_type+"'";
		}
		sql+=" where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and c.busi_date >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and c.busi_date <= '"+endMonth+"'";
		}
		if(no_cancel)
		{
			sql+=" and not exists(select 1 from t_cancel_proj x where x.org_id = p.org_id and x.project_code = p.project_code)";
		}
		sql+=" group by p.org_id,p.build_mode "
				+ "union select c.org_id,'合计',format(sum(case when datediff(now(),c.busi_date) >=0 and datediff(now(),c.busi_date)<=90 then c.account_amt else 0 end),2) as account_amt0,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=91 and datediff(now(),c.busi_date)<=180 then c.account_amt else 0 end),2) as account_amt4,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=181 and datediff(now(),c.busi_date)<=270 then c.account_amt else 0 end),2) as account_amt7,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=271 and datediff(now(),c.busi_date)<=360 then c.account_amt else 0 end),2) as account_amt10,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=361 and datediff(now(),c.busi_date)<=720 then c.account_amt else 0 end),2) as account_amt13,"
				+ "format(sum(case when datediff(now(),c.busi_date) >=721 and datediff(now(),c.busi_date)<=9999 then c.account_amt else 0 end),2) as account_amt25 "
				+ "from t_pms p "
				+ "inner join t_cost c on p.org_id=c.org_id and p.project_code=c.project_code ";
		if(StringUtils.hasText(asset_trans_sts))
		{
			sql+=" and c.asset_trans_sts = '"+asset_trans_sts+"'";
		}
		if(StringUtils.hasText(assemble_sts))
		{
			sql+=" and c.assemble_sts = '"+assemble_sts+"'";
		}
		if(StringUtils.hasText(doc_type))
		{
			sql+=" and c.doc_type = '"+doc_type+"'";
		}
		sql+=" where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and c.busi_date >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and c.busi_date <= '"+endMonth+"'";
		}
		if(no_cancel)
		{
			sql+=" and not exists(select 1 from t_cancel_proj x where x.org_id = p.org_id and x.project_code = p.project_code)";
		}
		sql+=" group by p.org_id) a inner join department_info d on a.org_id = d.org_id "
				+ "left join build_mode_order o on a.build_mode = o.build_mode order by d.org_id asc,o.order_num asc ";
		return sql;
	}

}
