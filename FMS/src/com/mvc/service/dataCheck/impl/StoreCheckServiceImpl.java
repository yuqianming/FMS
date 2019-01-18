package com.mvc.service.dataCheck.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dataCheck.StoreCheckServiceI;
import com.mvc.utils.PackResultUtils;
@Service
public class StoreCheckServiceImpl implements StoreCheckServiceI{
	@Autowired
	JdbcTemplate jdbcTemplate;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public List<Map<String, Object>> addGrid(String startMonth,String endMonth,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("######加项统计开始："+sdf.format(new Date()));
		DecimalFormat df = new DecimalFormat("###,##0.00");
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("type", "出库回滚");
		//计算"出库回滚"的"仓储有核算无"数据
		String sql="select (case when sum(t.total_amt_e_tax) is null then 0 else sum(t.total_amt_e_tax) end) as store_amt from t_store_deal t where t.opration_type = '出库回滚' and t.is_check <> '否' and t.org_id = '"+org_id+"'";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" and not exists (select 1 from t_cost c where c.order_type = '出库回滚' and c.org_id = '"+org_id+"' and c.order_code = t.batch_no";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=")";
		System.out.println("###### 出库回滚仓储1："+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		BigDecimal store_amt_ckhg=new BigDecimal(0);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_ckhg=store_amt_ckhg.add(new BigDecimal(mapList.get(0).get("store_amt").toString()));
		}
		System.out.println("######出库回滚仓储1统计完成："+sdf.format(new Date()));
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as store_amt from t_material_adj t where t.operate_type = '出库回滚' and t.system_type = '核算系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_ckhg=store_amt_ckhg.subtract(new BigDecimal(mapList.get(0).get("store_amt").toString()));
		}
		map.put("store_amt", store_amt_ckhg);
		map.put("store_amt_txt", df.format(store_amt_ckhg));
		System.out.println("######出库回滚仓储2统计完成："+sdf.format(new Date()));
		//计算"出库回滚"的"核算有仓储无"数据
		sql="select (case when sum(t.account_amt) is null then 0 else abs(sum(t.account_amt)) end) as deal_amt from t_cost t where t.order_type = '出库回滚' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" and not exists (select 1 from t_store_deal s where s.opration_type = '出库回滚' and s.is_check <> '否' and s.org_id = '"+org_id+"' and s.batch_no = t.order_code";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=")";
		BigDecimal deal_amt_ckhg=new BigDecimal(0);
		System.out.println("###### 出库回滚核算1："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_ckhg=deal_amt_ckhg.add(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		System.out.println("######出库回滚核算1统计完成："+sdf.format(new Date()));
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as deal_amt from t_material_adj t where t.operate_type = '出库回滚' and t.system_type = '仓储系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_ckhg=deal_amt_ckhg.subtract(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		map.put("deal_amt", deal_amt_ckhg);
		map.put("deal_amt_txt", df.format(deal_amt_ckhg));
		System.out.println("######出库回滚核算2统计完成："+sdf.format(new Date()));
		//计算"出库回滚"的"订单相同金额不同（仓储 - 核算）"数据
		sql="select (case when (abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else (abs(a.store_amt) - abs(b.deal_amt)) end) as all_amt,format((case when (abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else (abs(a.store_amt) - abs(b.deal_amt)) end),2) as all_amt_txt from "
				+ "(select ";
		sql+="s.batch_no as order_code";
		sql+=",sum(s.total_amt_e_tax) as store_amt from t_store_deal s where";
		//表5t_store_deal的操作类型
		sql+=" s.opration_type = '出库回滚' and s.is_check <> '否' and s.org_id = '"+org_id+"' ";
		
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <='"+endMonth+"'";
		}
		
		sql+="group by s.batch_no) a";
		sql+=" inner join (select c.order_code,sum(c.account_amt) as deal_amt from t_cost c where ";
		//表1t_cost的订单类型
		sql+=" c.order_type = '出库回滚' and c.org_id = '"+org_id+"' ";
				
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') <='"+endMonth+"'";
		}
		sql+="group by c.order_code) b on a.order_code = b.order_code ";
		System.out.println("###### 出库回滚完成："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			map.put("all_amt", mapList.get(0).get("all_amt"));
			map.put("all_amt_txt", mapList.get(0).get("all_amt_txt"));
		}
		else
		{
			map.put("all_amt", 0);
			map.put("all_amt_txt", "0.00");
		}
		result.add(map);
		System.out.println("######出库回滚统计完成："+sdf.format(new Date()));
		//"退库"
		map=new HashMap<String,Object>();
		map.put("type", "退库");
		//计算"退库"的"仓储有核算无"数据
		sql="select (case when sum(t.total_amt_e_tax) is null then 0 else sum(t.total_amt_e_tax) end) as store_amt from t_store_deal t where t.opration_type = '入库' and t.busi_type = '在建工程物资退库入库' and t.is_check <> '否' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" and not exists (select 1 from t_cost c where c.order_type = '退库红冲' and c.org_id = '"+org_id+"' and c.order_code = t.order_code";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=")";
		BigDecimal store_amt_tk=new BigDecimal(0);
		System.out.println("###### 退库仓储1："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_tk=store_amt_tk.add(new BigDecimal(mapList.get(0).get("store_amt").toString()));
		}
		System.out.println("######退库仓储1统计完成："+sdf.format(new Date()));
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as store_amt from t_material_adj t where t.operate_type = '退库' and t.system_type = '核算系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_tk=store_amt_tk.subtract(new BigDecimal(mapList.get(0).get("store_amt").toString()));
		}
		map.put("store_amt", store_amt_tk);
		map.put("store_amt_txt", df.format(store_amt_tk));
		System.out.println("######退库仓储2统计完成："+sdf.format(new Date()));
		//计算"退库"的"核算有仓储无"数据
		sql="select (case when sum(t.account_amt) is null then 0 else abs(sum(t.account_amt)) end) as deal_amt from t_cost t where t.order_type = '退库红冲' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" and not exists (select 1 from t_store_deal s where s.opration_type = '入库' and s.busi_type = '在建工程物资退库入库' and s.is_check <> '否' and s.org_id = '"+org_id+"' and s.order_code = t.order_code";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=")";
		BigDecimal deal_amt_tk=new BigDecimal(0);
		System.out.println("###### 退库核算1："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_tk=deal_amt_tk.add(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		System.out.println("######退库核算1统计完成："+sdf.format(new Date()));
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as deal_amt from t_material_adj t where t.operate_type = '退库' and t.system_type = '仓储系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_tk=deal_amt_tk.subtract(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		map.put("deal_amt", deal_amt_tk);
		map.put("deal_amt_txt", df.format(deal_amt_tk));
		System.out.println("######退库核算2统计完成："+sdf.format(new Date()));
		//计算"退库"的"订单相同金额不同（仓储 - 核算）"数据
		sql="select (case when (abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else (abs(a.store_amt) - abs(b.deal_amt)) end) as all_amt,format((case when (abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else (abs(a.store_amt) - abs(b.deal_amt)) end),2) as all_amt_txt from "
				+ "(select ";
		sql+="s.order_code";
		sql+=",sum(s.total_amt_e_tax) as store_amt from t_store_deal s where";
		//表5t_store_deal的操作类型
		sql+=" s.opration_type = '入库' and s.busi_type = '在建工程物资退库入库' and s.is_check <> '否' and s.org_id = '"+org_id+"' ";
		
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <='"+endMonth+"'";
		}
		
		sql+="group by s.order_code) a";
		sql+=" inner join (select c.order_code,sum(c.account_amt) as deal_amt from t_cost c where ";
		//表1t_cost的订单类型
		sql+=" c.order_type = '退库红冲' and c.org_id = '"+org_id+"' ";
				
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') <='"+endMonth+"'";
		}
		sql+="group by c.order_code) b on a.order_code = b.order_code ";
		System.out.println("###### 退库完成："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			map.put("all_amt", mapList.get(0).get("all_amt"));
			map.put("all_amt_txt", mapList.get(0).get("all_amt_txt"));
		}
		else
		{
			map.put("all_amt", 0);
			map.put("all_amt_txt", "0.00");
		}
		result.add(map);
		System.out.println("######退库统计完成："+sdf.format(new Date()));
		//"入库差异"
		map=new HashMap<String,Object>();
		map.put("type", "入库差异");
		//计算"入库差异"的"仓储有核算无"数据
		sql="select (case when sum(case when t.opration_type = '入库' and t.busi_type <> '在建工程物资退库入库' then t.total_amt_e_tax when t.opration_type = '入库回滚' then -t.total_amt_e_tax end) is null then 0 else sum(case when t.opration_type = '入库' and t.busi_type <> '在建工程物资退库入库' then t.total_amt_e_tax when t.opration_type = '入库回滚' then -t.total_amt_e_tax end) end) as store_amt"
				+ " from t_store_deal t where t.is_check <> '否' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" and not exists (select 1 from t_godown_entry g where g.entry_code = t.batch_no and g.org_id = '"+org_id+"' and g.is_check <> '否' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and g.entry_date >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and g.entry_date <= '"+endMonth+"'";
		}
		sql+=")";
		BigDecimal store_amt_rkcy=new BigDecimal(0);
		System.out.println("###### 入库差异仓储1："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_rkcy=store_amt_rkcy.add(new BigDecimal(mapList.get(0).get("store_amt").toString()));
		}
		System.out.println("######入库差异仓储1统计完成："+sdf.format(new Date())+" 值："+store_amt_rkcy);
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as store_amt from t_material_adj t where t.operate_type = '入库' and t.system_type = '核算系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_rkcy=store_amt_rkcy.subtract(new BigDecimal(mapList.get(0).get("store_amt").toString()));
			System.out.println("###### 值："+new BigDecimal(mapList.get(0).get("store_amt").toString()));
		}
		map.put("store_amt", store_amt_rkcy);
		map.put("store_amt_txt", df.format(store_amt_rkcy));
		System.out.println("######入库差异仓储2统计完成："+sdf.format(new Date()));
		//计算"入库差异"的"核算有仓储无"数据
		sql="select (case when sum(t.amount) is null then 0 else abs(sum(t.amount)) end) as deal_amt from t_godown_entry t where t.is_check <> '否' and t.org_id = '"+org_id+"' and not exists (select 1 from t_store_deal s where ((s.opration_type = '入库' and s.busi_type <> '在建工程物资退库入库') or s.opration_type = '入库回滚') and s.org_id = '"+org_id+"' and t.entry_code = s.batch_no and s.is_check <> '否' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=")";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.entry_date >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.entry_date <= '"+endMonth+"'";
		}
		BigDecimal deal_amt_rkcy=new BigDecimal(0);
		System.out.println("###### 入库差异核算1："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_rkcy=deal_amt_rkcy.add(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		System.out.println("######入库差异核算1统计完成："+sdf.format(new Date()));
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as deal_amt from t_material_adj t where t.operate_type = '入库' and t.system_type = '仓储系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_rkcy=deal_amt_rkcy.subtract(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		map.put("deal_amt", deal_amt_rkcy);
		map.put("deal_amt_txt", df.format(deal_amt_rkcy));
		System.out.println("######入库差异核算2统计完成："+sdf.format(new Date()));
		//计算"入库差异"的"订单相同金额不同（仓储 - 核算）"数据
		sql="select (case when sum(abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else sum(abs(a.store_amt) - abs(b.deal_amt)) end) as all_amt,format((case when sum(abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else sum(abs(a.store_amt) - abs(b.deal_amt)) end),2) as all_amt_txt from (select s.batch_no as order_code,sum(case when s.opration_type = '入库' and s.busi_type <> '在建工程物资退库入库' then s.total_amt_e_tax when s.opration_type = '入库回滚' then -s.total_amt_e_tax end) as store_amt from t_store_deal s where s.is_check <> '否' and s.org_id = '"+org_id+"' and ((s.opration_type = '入库' and s.busi_type <> '在建工程物资退库入库') or s.opration_type = '入库回滚') ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" group by s.batch_no) a,"
		   + "(select g.entry_code as order_code,sum(g.amount) as deal_amt from t_godown_entry g where g.is_check <> '否' and g.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and g.entry_date >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and g.entry_date <= '"+endMonth+"'";
		}
		sql+=" group by g.entry_code) b where a.order_code = b.order_code";
		System.out.println("###### 入库差异核完成："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			map.put("all_amt", mapList.get(0).get("all_amt"));
			map.put("all_amt_txt", mapList.get(0).get("all_amt_txt"));
		}
		else
		{
			map.put("all_amt", 0);
			map.put("all_amt_txt", "0.00");
		}
		result.add(map);
		System.out.println("######入库差异统计完成："+sdf.format(new Date()));
		return result;
	}

	@Override
	public List<Map<String, Object>> subGrid(String startMonth,String endMonth,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("------减项统计开始："+sdf.format(new Date()));
		DecimalFormat df = new DecimalFormat("###,##0.00");
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("type", "出库");
		String sql="select (case when sum(t.total_amt_e_tax) is null then 0 else sum(t.total_amt_e_tax) end) as store_amt,format((case when sum(t.total_amt_e_tax) is null then 0 else sum(t.total_amt_e_tax) end),2) as store_amt_txt from t_store_deal t where t.opration_type = '出库' and t.is_check <> '否' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" and not exists (select 1 from t_cost c where c.order_type = '领用出库' and c.org_id = '"+org_id+"' and c.order_code = t.batch_no";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=")";
		BigDecimal store_amt_ck=new BigDecimal(0);
		System.out.println("###### 出库仓储1："+sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_ck=store_amt_ck.add(new BigDecimal(mapList.get(0).get("store_amt").toString()));
		}
		System.out.println("------出库仓储1统计完成："+sdf.format(new Date())+"  值："+store_amt_ck);
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as store_amt from t_material_adj t where t.operate_type = '出库' and t.system_type = '核算系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt_ck=store_amt_ck.subtract(new BigDecimal(mapList.get(0).get("store_amt").toString()));
			System.out.println("###### t_material_adj sql :"+sql+" 值："+mapList.get(0).get("store_amt").toString());
		}
		map.put("store_amt", store_amt_ck);
		map.put("store_amt_txt", df.format(store_amt_ck));
		System.out.println("------出库仓储2统计完成："+sdf.format(new Date()));
		
		sql="select (case when sum(t.account_amt) is null then 0 else abs(sum(t.account_amt)) end) as deal_amt,format((case when sum(t.account_amt) is null then 0 else abs(sum(t.account_amt)) end),2) as deal_amt_txt from t_cost t where t.order_type = '领用出库' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=" and not exists (select 1 from t_store_deal s where s.opration_type = '出库' and s.is_check <> '否' and s.org_id = '"+org_id+"' and s.batch_no = t.order_code";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		sql+=")";
		BigDecimal deal_amt_ck=new BigDecimal(0);
		System.out.println("###### 出库核算1："+sql);
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_ck=deal_amt_ck.add(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		System.out.println("------出库核算1统计完成："+sdf.format(new Date()));
		sql="select abs((case when sum(t.amt) is null then 0 else sum(t.amt) end)) as deal_amt from t_material_adj t where t.operate_type = '出库' and t.system_type = '仓储系统' and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt_ck=deal_amt_ck.subtract(new BigDecimal(mapList.get(0).get("deal_amt").toString()));
		}
		map.put("deal_amt", deal_amt_ck);
		map.put("deal_amt_txt", df.format(deal_amt_ck));
		System.out.println("------出库核算2统计完成："+sdf.format(new Date()));
		sql="select (case when (abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else (abs(a.store_amt) - abs(b.deal_amt)) end) as all_amt,format((case when (abs(a.store_amt) - abs(b.deal_amt)) is null then 0 else (abs(a.store_amt) - abs(b.deal_amt)) end),2) as all_amt_txt from "
			+ "(select ";
		sql+="s.batch_no as order_code";
		sql+=",sum(abs(s.total_amt_e_tax)) as store_amt from t_store_deal s where";
		//表5t_store_deal的操作类型
		sql+=" s.opration_type = '出库' and s.is_check <> '否' and s.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(s.create_time,'%Y%m') <='"+endMonth+"'";
		}
		
		sql+="group by s.batch_no) a";
		sql+=" inner join (select c.order_code,sum(abs(c.account_amt)) as deal_amt from t_cost c where ";
		//表1t_cost的订单类型
		sql+=" c.order_type = '领用出库' and c.org_id = '"+org_id+"' ";	
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(c.busi_date,'%Y%m') <='"+endMonth+"'";
		}
		sql+="group by c.order_code) b on a.order_code = b.order_code ";
		System.out.println("###### 出库完成："+sql);
		
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			map.put("all_amt", mapList.get(0).get("all_amt"));
			map.put("all_amt_txt", mapList.get(0).get("all_amt_txt"));
		}
		else
		{
			map.put("all_amt", 0);
			map.put("all_amt_txt", "0.00");
		}
		result.add(map);
		System.out.println("------减项统计完成："+sdf.format(new Date()));
		return result;
	}

	@Override
	public Map<String, Object> poolData(String startMonth,String endMonth,String org_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		BigDecimal deal_amt=new BigDecimal(0);
		String sql="select (case when sum(t.amount) is null then 0 else sum(t.amount) end) as amount from t_godown_entry t where t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.entry_date >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.entry_date <= '"+endMonth+"'";
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt=deal_amt.add(new BigDecimal(mapList.get(0).get("amount").toString()));
		}
		
		sql="select (case when sum(case when t.operate_type = '入库' then t.amt when t.operate_type = '退库' then t.amt when t.operate_type='出库' then - t.amt when t.operate_type = '出库回滚' then t.amt end) is null then 0 else sum(case when t.operate_type = '入库' then t.amt when t.operate_type = '退库' then t.amt when t.operate_type='出库' then - t.amt when t.operate_type = '出库回滚' then t.amt end) end) as amt from t_material_adj t where t.system_type = '核算系统' and t.detail_adj <> '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <='"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt=deal_amt.add(new BigDecimal(mapList.get(0).get("amt").toString()));
		}
		
		sql="select (case when sum(t.account_amt) is null then 0 else sum(t.account_amt) end) as amt from t_cost t where t.order_type in ('出库回滚','领用出库','退库红冲') and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.busi_date,'%Y%m') <='"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			deal_amt=deal_amt.subtract(new BigDecimal(mapList.get(0).get("amt").toString()));
		}
		map.put("deal_amt", deal_amt);
		BigDecimal store_amt=new BigDecimal(0);
		sql="select (case when sum(case when t.operate_type = '入库' then t.amt when t.operate_type = '退库' then t.amt when t.operate_type='出库' then - t.amt when t.operate_type = '出库回滚' then t.amt end) is null then 0 else sum(case when t.operate_type = '入库' then t.amt when t.operate_type = '退库' then t.amt when t.operate_type='出库' then - t.amt when t.operate_type = '出库回滚' then t.amt end) end) as amt from t_material_adj t where t.system_type = '仓储系统' and t.detail_adj <> '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >='"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <='"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt=store_amt.add(new BigDecimal(mapList.get(0).get("amt").toString()));
		}
		
		
		sql="select (case when sum(case when t.opration_type = '入库' then t.total_amt_e_tax when t.opration_type = '入库回滚' then - t.total_amt_e_tax when t.opration_type = '出库' then - t.total_amt_e_tax when t.opration_type = '出库回滚' then t.total_amt_e_tax end) is null then 0 else sum(case when t.opration_type = '入库' then t.total_amt_e_tax when t.opration_type = '入库回滚' then - t.total_amt_e_tax when t.opration_type = '出库' then - t.total_amt_e_tax when t.opration_type = '出库回滚' then t.total_amt_e_tax end) end) as total_amt_e_tax from t_store_deal t where t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt=store_amt.add(new BigDecimal(mapList.get(0).get("total_amt_e_tax").toString()));
		}
		
		/*sql="select (case when sum(t.total_amt_e_tax) is null then 0 else sum(t.total_amt_e_tax) end) as total_amt_e_tax from t_store_deal t where t.opration_type in ('入库回滚','出库') ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and date_format(t.create_time,'%Y%m') <= '"+endMonth+"'";
		}
		mapList=jdbcTemplate.queryForList(sql);
		if(mapList!=null&&mapList.size()>0)
		{
			store_amt=store_amt.subtract(new BigDecimal(mapList.get(0).get("total_amt_e_tax").toString()));
		}*/
		map.put("store_amt", store_amt);
		return map;
	}

	@Override
	public Map<String, Object> detailGrid(String startMonth,
			String endMonth, String type, String mode,boolean isAll,boolean hideZero,PageFilter ph,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("######detailGrid start : "+sdf.format(new Date()));
		//List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		String sql=returnSql(startMonth,endMonth,type,mode,isAll,hideZero,org_id);
		if(ph==null)
		{
			sql+=" order by month";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		}
		System.out.println("######detailGrid sql : "+sql);
		//result=jdbcTemplate.queryForList(sql);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		System.out.println("######detailGrid end : "+sdf.format(new Date()));
		return result;
	}

	@Override
	public Long detailCount(String startMonth, String endMonth, String type,
			String mode, boolean isAll,boolean hideZero,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("######detailCount start : "+sdf.format(new Date()));
		String temp=returnSql(startMonth,endMonth,type,mode,isAll,hideZero,org_id);
		String sql="select count(*) from ("+temp+") m";
		Long count=jdbcTemplate.queryForObject(sql, Long.class);
		System.out.println("######detailCount end : "+sdf.format(new Date()));
		return count;
	}
	
	private String returnSql(String startMonth,String endMonth, String type, String mode,boolean isAll,boolean hideZero,String org_id)
	{
		String sql="";
		if("入库差异".equals(type))
		{
			sql="select b.month,b.order_code,format(b.store_amt,2) as store_amt,format(b.deal_amt,2) as deal_amt,(case when b.store_amt is not null and b.deal_amt is not null then format(b.store_amt - b.deal_amt,2) else '' end) as all_amt from (select a.month,a.order_code,sum(a.store_amt) as store_amt,sum(a.deal_amt) as deal_amt from (";
			if("store_amt_txt".equals(mode)||"all_amt_txt".equals(mode))
			{
				sql+="select m.month,m.order_code,m.store_amt,n.deal_amt ";
			}
			else if("deal_amt_txt".equals(mode))
			{
				sql+="select n.month,n.order_code,m.store_amt,n.deal_amt ";
			}
			sql+=" from (select date_format(s.create_time,'%Y%m') as month,s.batch_no as order_code,sum(case when s.opration_type = '入库' and s.busi_type <> '在建工程物资退库入库' then s.total_amt_e_tax when s.opration_type = '入库回滚' then -s.total_amt_e_tax end) as store_amt from t_store_deal s where ((s.opration_type = '入库' and s.busi_type <> '在建工程物资退库入库') or s.opration_type = '入库回滚') and s.is_check <> '否' and s.org_id = '"+org_id+"' ";
			if(StringUtils.hasText(startMonth))
			{
				sql+=" and date_format(s.create_time,'%Y%m') >= '"+startMonth+"'";
			}
			if(StringUtils.hasText(endMonth))
			{
				sql+=" and date_format(s.create_time,'%Y%m') <= '"+endMonth+"'";
			}
			sql+=" group by s.batch_no) m ";
			if("store_amt_txt".equals(mode))
			{
				sql+=" left join ";
			}
			else if("deal_amt_txt".equals(mode))
			{
				sql+=" right join ";
			}
			else if("all_amt_txt".equals(mode))
			{
				sql+=" inner join ";
			}
			sql+="(select g.entry_date as month,g.entry_code as order_code,abs(sum(g.amount)) as deal_amt from t_godown_entry g where g.is_check <> '否' and g.org_id = '"+org_id+"' ";
			if(StringUtils.hasText(startMonth))
			{
				sql+=" and g.entry_date >= '"+startMonth+"'";
			}
			if(StringUtils.hasText(endMonth))
			{
				sql+=" and g.entry_date <= '"+endMonth+"'";
			}
			sql+=" group by g.entry_code) n on m.order_code = n.order_code ";
			sql+=" union select t.month,t.doc_code as order_code,abs(sum(case when t.system_type = '仓储系统' then t.amt else null end)) as store_amt,abs(sum(case when t.system_type = '核算系统' then t.amt else null end)) as deal_amt from t_material_adj t where t.operate_type = '入库' ";
		}
		else
		{
			sql="select b.month,b.order_code,format(b.store_amt,2) as store_amt,format(b.deal_amt,2) as deal_amt,(case when b.store_amt is not null and b.deal_amt is not null then format(b.store_amt - b.deal_amt,2) else '' end) as all_amt from (select a.month,a.order_code,sum(a.store_amt) as store_amt,sum(a.deal_amt) as deal_amt from (";
			if("store_amt_txt".equals(mode)||"all_amt_txt".equals(mode))
			{
				sql+="select m.month,m.order_code,m.store_amt,n.deal_amt ";
			}
			else if("deal_amt_txt".equals(mode))
			{
				sql+="select n.month,n.order_code,m.store_amt,n.deal_amt ";
			}
			sql+=" from (select date_format(s.create_time, '%Y%m') as month,";
			if("出库回滚".equals(type)||"出库".equals(type))
			{
				sql+="s.batch_no as order_code";
			}
			else
			{
				sql+="s.order_code";
			}
			sql+=",sum(s.total_amt_e_tax) as store_amt,null as deal_amt from t_store_deal s where";
			//表5t_store_deal的操作类型
			if("出库回滚".equals(type))
			{
				sql+=" s.opration_type = '出库回滚' ";
			}
			else if("退库".equals(type))
			{
				sql+=" s.opration_type = '入库' and s.busi_type = '在建工程物资退库入库' ";
			}
			else if("出库".equals(type))
			{
				sql+=" s.opration_type = '出库' ";
			}
			sql+=" and s.is_check <> '否' and s.org_id = '"+org_id+"' ";
			if(StringUtils.hasText(startMonth))
			{
				sql+=" and date_format(s.create_time,'%Y%m') >='"+startMonth+"'";
			}
			if(StringUtils.hasText(endMonth))
			{
				sql+=" and date_format(s.create_time,'%Y%m') <='"+endMonth+"'";
			}
			
			if("出库回滚".equals(type)||"出库".equals(type))
			{
				sql+="group by s.batch_no ";
			}
			else
			{
				sql+="group by s.order_code ";
			}
			sql+=") m ";
			if("store_amt_txt".equals(mode))
			{
				sql+=" left join ";
			}
			else if("deal_amt_txt".equals(mode))
			{
				sql+=" right join ";
			}
			else if("all_amt_txt".equals(mode))
			{
				sql+=" inner join ";
			}
			sql+=" (select date_format(c.busi_date, '%Y%m') as month,c.order_code,null as store_amt,abs(sum(c.account_amt)) as deal_amt from t_cost c where c.org_id = '"+org_id+"' ";
			//表1t_cost的订单类型
			if("出库回滚".equals(type))
			{
				sql+=" and c.order_type = '出库回滚'";
			}
			else if("退库".equals(type))
			{
				sql+=" and c.order_type = '退库红冲'";
			}
			else  if("出库".equals(type))
			{
				sql+=" and c.order_type = '领用出库'";
			}
					
			if(StringUtils.hasText(startMonth))
			{
				sql+=" and date_format(c.busi_date,'%Y%m') >='"+startMonth+"'";
			}
			if(StringUtils.hasText(endMonth))
			{
				sql+=" and date_format(c.busi_date,'%Y%m') <='"+endMonth+"'";
			}
			sql+="group by c.order_code ) n on n.order_code = m.order_code";
			sql+=" union select t.month,t.doc_code as order_code,abs(sum(case when t.system_type = '仓储系统' then t.amt else null end)) as store_amt,abs(sum(case when t.system_type = '核算系统' then t.amt else null end)) as deal_amt from t_material_adj t where ";
			if("出库回滚".equals(type))
			{
				sql+=" t.operate_type = '出库回滚' ";
			}
			else if("退库".equals(type))
			{
				sql+=" t.operate_type = '退库' ";
			}
			else  if("出库".equals(type))
			{
				sql+=" t.operate_type = '出库' ";
			}
		}
		if("store_amt_txt".equals(mode))
		{
			sql+=" and t.system_type = '核算系统'";
		}
		else if("deal_amt_txt".equals(mode))
		{
			sql+=" and t.system_type = '仓储系统'";
		}
		sql+=" and t.detail_adj = '是' and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(startMonth))
		{
			sql+=" and t.month >= '"+startMonth+"'";
		}
		if(StringUtils.hasText(endMonth))
		{
			sql+=" and t.month <= '"+endMonth+"'";
		}
		sql+=" group by t.doc_code ) a group by a.order_code ) b where 1=1 ";
		if("store_amt_txt".equals(mode)||"deal_amt_txt".equals(mode))
		{
			if(!isAll)
			{
				sql+=" and (b.store_amt is null or b.deal_amt is null) ";
			}
		}
		else if("all_amt_txt".equals(mode))
		{
			sql+=" and b.store_amt is not null and b.deal_amt is not null";
		}
		if(hideZero)
		{
			sql+=" and (case when b.store_amt is null then 0 else b.store_amt end + case when b.deal_amt is null then 0 else b.deal_amt end) <> 0";
		}
		return sql;
	}

}
