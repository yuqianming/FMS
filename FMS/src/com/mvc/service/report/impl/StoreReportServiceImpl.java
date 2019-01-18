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
import com.mvc.service.report.StoreReportServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class StoreReportServiceImpl implements StoreReportServiceI{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public Map<String, Object> dataGrid(String startMonth, String endMonth,
			boolean no_out, boolean no_in, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 销项检查汇总DataGrid开始："+sdf.format(new Date()));
		String sql=returnSql(startMonth,endMonth,no_out,no_in);
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
	
	private String returnSql(String startMonth, String endMonth,boolean no_out, boolean no_in)
	{
		String sql="select d.org_name,format(sum(a.deal_total),2) as deal_total,format(sum(a.deal_rk),2) as deal_rk,format(sum(a.deal_ck),2) as deal_ck,format(sum(a.deal_ckhg),2) as deal_ckhg,format(sum(a.deal_tk),2) as deal_tk,"
				+ "format(sum(a.deal_total) -  sum(a.deal_rk) + sum(a.deal_ck) - sum(a.deal_ckhg) - sum(a.deal_tk),2) as deal_cz,"
				+ "format(sum(a.store_total),2) as store_total,format(sum(a.store_rk),2) as store_rk,format(sum(a.store_ck),2) as store_ck,format(sum(a.store_ckhg),2) as store_ckhg,format(sum(a.store_tk),2) as store_tk,"
				+ "format(sum(a.store_total) -  sum(a.store_rk) + sum(a.store_ck) - sum(a.store_ckhg) - sum(a.store_tk),2) as store_cz, format(sum(a.store_total) - sum(a.deal_total),2) as dvalue_total,"
				+ "format(sum(a.store_rk) - sum(a.deal_rk),2) as dvalue_rk,format(sum(a.store_ck) - sum(a.deal_ck),2) as dvalue_ck,format(sum(a.store_ckhg) - sum(a.deal_ckhg),2) as dvalue_ckhg,"
				+ "format(sum(a.store_tk) - sum(a.deal_tk),2) as dvalue_tk,"
				+ "format(sum(a.store_total) - sum(a.store_rk) + sum(a.store_ck) - sum(a.store_ckhg) - sum(a.store_tk) - (sum(a.deal_total) - sum(a.deal_rk) + sum(a.deal_ck) - sum(a.deal_ckhg) - sum(a.deal_tk)),2) as dvalue_cz "
				+ "from (select t.org_id,sum(case when t.system_type = '核算系统' and t.operate_type = '入库' and t.detail_adj <> '是' then t.amt when t.system_type = '核算系统' and "
				+ "t.operate_type = '退库' and t.detail_adj <> '是' then t.amt when t.system_type = '核算系统' and t.operate_type='出库' and t.detail_adj <> '是' then - t.amt when t.system_type = '核算系统' and t.operate_type = '出库回滚' and t.detail_adj <> '是' then t.amt end) as deal_total,"
				+ "sum(case when t.system_type='核算系统' and t.operate_type='入库' and t.detail_adj = '是' then t.amt end) as deal_rk,"
				+ "sum(case when t.system_type='核算系统' and t.operate_type='出库' and t.detail_adj = '是' then t.amt end) as deal_ck,"
				+ "sum(case when t.system_type='核算系统' and t.operate_type='出库回滚' and t.detail_adj = '是' then t.amt end) as deal_ckhg,"
				+ "sum(case when t.system_type='核算系统' and t.operate_type='退库' and t.detail_adj = '是' then t.amt end) as deal_tk,"
				+ "sum(case when t.system_type='仓储系统' and t.operate_type = '入库' and t.detail_adj <> '是' then t.amt when t.system_type = '仓储系统' and "
				+ "t.operate_type = '退库' and t.detail_adj <> '是' then t.amt when t.system_type = '仓储系统' and t.operate_type='出库' and t.detail_adj <> '是' then - t.amt when t.system_type = '仓储系统' and t.operate_type = '出库回滚' and t.detail_adj <> '是' then t.amt end) as store_total,"
				+ "sum(case when t.system_type='仓储系统' and t.operate_type='入库' and t.detail_adj = '是' then t.amt end) as store_rk,"
				+ "sum(case when t.system_type='仓储系统' and t.operate_type='出库' and t.detail_adj = '是' then t.amt end) as store_ck,"
				+ "sum(case when t.system_type='仓储系统' and t.operate_type='出库回滚' and t.detail_adj = '是' then t.amt end) as store_ckhg,"
				+ "sum(case when t.system_type='仓储系统' and t.operate_type='退库' and t.detail_adj = '是' then t.amt end) as store_tk "
				+ "from t_material_adj t where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month>='"+startMonth+"' ";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month<='"+endMonth+"' ";
		}
		sql+="group by t.org_id "
				+ "union "
				+ "select t.org_id,0 as deal_total,0 as deal_rk,0 as deal_ck,0 as deal_ckhg,0 as deal_tk,"
				+ "sum(case when t.opration_type = '入库' then t.total_amt_e_tax when t.opration_type = '入库回滚' then - t.total_amt_e_tax when t.opration_type = '出库' then - t.total_amt_e_tax when t.opration_type = '出库回滚' then t.total_amt_e_tax end) as store_total,"
				+ "sum(case when t.opration_type = '入库' and t.busi_type <> '在建工程物资退库入库' then t.total_amt_e_tax when t.opration_type = '入库回滚' then -t.total_amt_e_tax end) as store_rk,"
				+ "sum(case when t.opration_type = '出库' then t.total_amt_e_tax end) as store_ck,"
				+ "sum(case when t.opration_type = '出库回滚' then t.total_amt_e_tax end) as store_ckhg,"
				+ "sum(case when t.opration_type = '入库' and t.busi_type = '在建工程物资退库入库' then t.total_amt_e_tax end ) as store_tk "
				+ "from t_store_deal t where t.is_check <> '否' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m')>='"+startMonth+"' ";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m')<='"+endMonth+"' ";
		}
		if(no_in)
		{
			sql+=" and busi_type <> '调拨入库' ";
		}
		if(no_out)
		{
			sql+=" and busi_type <> '调拨出库' ";
		}
		sql+="group by t.org_id "
				+ "union "
				+ "select t.org_id,sum(t.amount) as deal_total,sum(case when t.is_check <> '否' then t.amount end) as deal_rk,0 as deal_ck,0 as deal_ckhg,0 as deal_tk,0 as store_total,0 as store_rk,0 as store_ck,0 as store_ckhg,0 as store_tk "
				+ "from t_godown_entry t where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.entry_date>='"+startMonth+"' ";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.entry_date<='"+endMonth+"' ";
		}
		sql+="group by t.org_id "
				+ "union "
				+ "select t.org_id,-abs(sum(case when t.order_type in('领用出库','出库回滚','退库红冲') then t.account_amt end)) as deal_total,0 as deal_rk,"
				+ "sum(abs(case when t.order_type = '领用出库' then t.account_amt end)) as deal_ck,"
				+ "sum(abs(case when t.order_type = '出库回滚' then t.account_amt end)) as deal_ckhg,"
				+ "sum(abs(case when t.order_type = '退库红冲' then t.account_amt end)) as deal_tk,"
				+ "0 as store_total,0 as store_rk,0 as store_ck,0 as store_ckhg,0 as store_tk "
				+ "from t_cost t where 1=1 ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m')>='"+startMonth+"' ";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m')<='"+endMonth+"' ";
		}
		sql+="group by t.org_id) a inner join department_info d on a.org_id = d.org_id group by a.org_id";
		return sql;
	}

}
