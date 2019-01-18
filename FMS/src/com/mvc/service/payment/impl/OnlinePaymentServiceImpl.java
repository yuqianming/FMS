package com.mvc.service.payment.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TCost;
import com.mvc.model.sys.TPayment;
import com.mvc.model.sys.TPaymentTemp;
import com.mvc.model.sys.TTableExcel;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.payment.OnlinePaymentServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExcelReaderUtil;
import com.mvc.utils.ReadFileUtil_2;

@Service
public class OnlinePaymentServiceImpl implements OnlinePaymentServiceI{
	@Autowired
	private BaseDaoI<TPaymentTemp> paymentTempDao;
	@Autowired
	private BaseDaoI<TCost> costDao;
	@Autowired
	private BaseDaoI<TPayment> payDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BaseDaoI<TTableExcel> tableDao;
	@Override
	public List<Map<String,Object>> dataGrid(String user_id,PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String sql="select t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,"
				+ "t.address_code,t.address_name,format(t.invoice_amt,2) as invoice_amt,format(t.invoice_amt_e_tax,2) "
				+ "as invoice_amt_e_tax,format(t.pay_amt,2) as pay_amt,format(t.pay_amt_e_tax,2) as pay_amt_e_tax,t.month,"
				+ "t.voucher_no,t.online_offline,t.settle_no,t.remark,floor(tax_rate*100) as tax_rate,t.accounting_org from payment_online_"+user_id+" t "
				+ "order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}
	
	@Override
	public Long count(String user_id, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String sql="select count(*) from payment_online_"+user_id;
		return payDao.countBySql(sql).longValue();
	}

	@Override
	public int upload(UploadInfo info,HttpSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 解析数据 start : "+new Date());
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("has", false);
		/*ReadFileUtil handle = new ReadFileUtil();
		List<Object[]> allList=new ArrayList<Object[]>();
		Map<String, List<String[]>> map = handle.readFile(info.getUpFile());
		String fileName=info.getUpFile().getOriginalFilename();
		Iterator iter = map.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next();
			List<Object[]> temp=(List<Object[]>)entry.getValue();
			if(temp!=null&&temp.size()>0)
			{
				temp.remove(0);
			}
			if(temp!=null&&temp.size()>0)
			{
				allList.addAll(temp);
			}
		}*/
		//List<String[]> allList=ReadFileUtil_2.readFile(session, info.getUserId(), info.getUpFile());
		List<String[]> allList=ExcelReaderUtil.readExcel(info.getUpFile());
		List<String> importList=new ArrayList<String>();//要导入的结算单号
		if(allList.size()>0)
		{
			for(Object[] object:allList)
			{
				if(!importList.contains(object[37].toString()))
				{
					importList.add(object[37].toString());
				}
			}
			CacheUtils.cacheMe("importList_"+info.getUserId(),importList);
			CacheUtils.cacheMe("payTempList_"+info.getUserId(),allList);
				
		}
		else
		{
			throw new Exception("请上传非空文件！");
		}
		System.out.println("###### 解析数据 end : "+new Date());
		return allList.size();
	}

	private boolean tableExistOrNot(String user_id)
	{
		boolean isExist=false;
		String sql="";
		try
		{
			sql="select count(*) from payment_online_"+user_id;
			jdbcTemplate.execute(sql);
			isExist=true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isExist;
	}
	
	private void createTable(String user_id)
	{
         String sql="CREATE TABLE payment_online_"+user_id+" (`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,"
         		+ "`project_code`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目编码' ,"
         		+ "`supplier_code`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商编号' ,"
         		+ "`supplier_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '供应商名称' ,"
         		+ "`order_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号' ,"
         		+ "`account_name`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目名称' ,"
         		+ "`address_code`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站址编码' ,"
         		+ "`address_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '站址名称' ,"
         		+ "`invoice_amt`  decimal(11,2) NULL DEFAULT NULL COMMENT '本次发票' ,`invoice_amt_e_tax`  decimal(11,2) NULL DEFAULT NULL COMMENT '本次发票净额' ,"
         		+ "`pay_amt`  decimal(11,2) NULL DEFAULT NULL COMMENT '本次付款' ,`pay_amt_e_tax`  decimal(11,2) NULL DEFAULT NULL COMMENT '本次付款净额' ,"
         		+ "`month`  varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日期' ,"
         		+ "`voucher_no`  varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '凭证编号' ,"
         		+ "`online_offline`  varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线上线下' ,"
         		+ "`settle_no`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算单号' ,"
         		+ "`remark`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注' ,"
         		+ "`tax_rate`  decimal(7,4) NULL DEFAULT NULL COMMENT '税率' ,"
         		+ "`accounting_org`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核算组织' ,"
         		+ "`update_by`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后修改人' ,"
         		+ "`org_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属机构' ,"
         		+ "`update_time`  timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间' ,"
         		+ "`uuid`  varchar(255) NULL DEFAULT NULL COMMENT 'uuid' ,"
         		+ "PRIMARY KEY (`id`))ENGINE=MyISAM;";
         payDao.executeSql(sql);
	}
	
	@Override
	public void dataHandle(String month,String user_id,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### 拆分导入数据到临时表start : "+new Date());
		boolean isExist=tableExistOrNot(user_id);
		if(!isExist)
		{
			createTable(user_id);
		}
		else
		{
			jdbcTemplate.execute("delete from payment_online_"+user_id);
		}

		@SuppressWarnings("unchecked")
		List<String[]> allList=(List<String[]>) CacheUtils.getCache("payTempList_"+user_id);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sqlTemp="insert into payment_online_"+user_id+"(month,project_code,supplier_code,supplier_name,order_code,address_code,address_name,account_name,invoice_amt,invoice_amt_e_tax,pay_amt,"
				+ "pay_amt_e_tax,tax_rate,remark,online_offline,settle_no,voucher_no,accounting_org,update_by,org_id,update_time,uuid) values ";
		StringBuffer tt = new StringBuffer();
		int count=0;
		//供应商调整表
		List<Map<String,Object>> adjList=jdbcTemplate.queryForList("select s_supplier_name,t_supplier_name from t_supplier_adj where org_id = '"+org_id+"'");
		for(String[] object:allList)
		{
			String order_code=object[2];
	    	String sql="select t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,t.address_code,t.address_name,"
	    			+ "(case when sum(t.account_amt) is null then 0 else sum(t.account_amt) end) as account_amt from t_cost t "
	    			+ "where t.order_code='"+order_code+"' and org_id = '"+org_id+"' group by t.account_name";
	    	List<TCost> costList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<TCost>(TCost.class));
			if(costList!=null&&costList.size()>0)
			{
				BigDecimal total=new BigDecimal(0);
	    		BigDecimal all_invoice_amt=new BigDecimal(0);
	    		BigDecimal all_invoice_amt_e_tax=new BigDecimal(0);
	    		BigDecimal tax_rate=(new BigDecimal(StringUtils.hasText(object[25])?object[25].contains(",")?object[25].replace(",", ""):object[25].toString():"0")).divide(new BigDecimal(StringUtils.hasText(object[24])?object[24].contains(",")?object[24].replace(",", ""):object[24].toString():"0"),2,BigDecimal.ROUND_HALF_UP);
				for(TCost cost:costList)
				{
					total=total.add(cost.getAccount_amt());
				}
				
				if(total.compareTo(new BigDecimal(0))==0)
				{
					String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
					TCost cost=costList.get(0);
					BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(object[24])?object[24].contains(",")?object[24].replace(",", ""):object[24].toString():"0");
					BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(object[26])?object[26].contains(",")?object[26].replace(",", ""):object[26].toString():"0");
					tt.append("('"+month+"','"+object[4].toString()+"','"+cost.getSupplier_code()+"',");
					String supplier_name=cost.getSupplier_name();
					for(Map<String,Object> map:adjList)
					{
						if(map.get("s_supplier_name").equals(supplier_name))
						{
							supplier_name=map.get("t_supplier_name").toString();
							break;
						}
					}
					tt.append("'"+supplier_name+"','"+cost.getOrder_code()+"','"+object[6].toString()+"',");
					tt.append("'"+object[7].toString()+"','"+cost.getAccount_name()+"',"+invoice_amt+","+invoice_amt_e_tax+",");
					tt.append(""+invoice_amt+","+invoice_amt_e_tax+","+tax_rate+",'"+object[34].toString()+"',");
					tt.append("'线上','"+object[37].toString()+"','"+object[51].toString()+"','"+object[1].toString()+"',");
					tt.append("'"+user_id+"','"+org_id+"','"+sdf.format(new Date())+"','"+uuid+"'),");
					count++;
				}
				else
				{
					for(int i=0;i<costList.size()-1;i++)
					{
						count++;
						String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
						TCost cost=costList.get(i);
						BigDecimal invoice_amt_e_tax=cost.getAccount_amt().divide(total,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(StringUtils.hasText(object[24])?object[24].contains(",")?object[24].replace(",", ""):object[24].toString():"0")).setScale(2, BigDecimal.ROUND_HALF_UP);
						all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(invoice_amt_e_tax);
						BigDecimal invoice_amt=cost.getAccount_amt().divide(total,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(StringUtils.hasText(object[26])?object[26].contains(",")?object[26].replace(",", ""):object[26].toString():"0")).setScale(2, BigDecimal.ROUND_HALF_UP);
						all_invoice_amt=all_invoice_amt.add(invoice_amt);
						tt.append("('"+month+"','"+object[4].toString()+"','"+cost.getSupplier_code()+"',");
						String supplier_name=cost.getSupplier_name();
						for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
						tt.append("'"+supplier_name+"','"+cost.getOrder_code()+"','"+object[6].toString()+"',");
						tt.append("'"+object[7].toString()+"','"+cost.getAccount_name()+"',"+invoice_amt+","+invoice_amt_e_tax+",");
						tt.append(""+invoice_amt+","+invoice_amt_e_tax+","+tax_rate+",'"+object[34].toString()+"',");
						tt.append("'线上','"+object[37].toString()+"','"+object[51].toString()+"','"+object[1].toString()+"',");
						tt.append("'"+user_id+"','"+org_id+"','"+sdf.format(new Date())+"','"+uuid+"'),");
					}
					
					count++;
					String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
					TCost cost=costList.get(costList.size()-1);
					BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(object[24])?object[24].contains(",")?object[24].replace(",", ""):object[24].toString():"0").subtract(all_invoice_amt_e_tax);
					BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(object[26])?object[26].contains(",")?object[26].replace(",", ""):object[26].toString():"0").subtract(all_invoice_amt);
					tt.append("('"+month+"','"+object[4].toString()+"','"+cost.getSupplier_code()+"',");
					String supplier_name=cost.getSupplier_name();
					for(Map<String,Object> map:adjList)
					{
						if(map.get("s_supplier_name").equals(supplier_name))
						{
							supplier_name=map.get("t_supplier_name").toString();
							break;
						}
					}
					tt.append("'"+supplier_name+"','"+cost.getOrder_code()+"','"+object[6].toString()+"',");
					tt.append("'"+object[7].toString()+"','"+cost.getAccount_name()+"',"+invoice_amt+","+invoice_amt_e_tax+",");
					tt.append(""+invoice_amt+","+invoice_amt_e_tax+","+tax_rate+",'"+object[34].toString()+"',");
					tt.append("'线上','"+object[37].toString()+"','"+object[51].toString()+"','"+object[1].toString()+"',");
					tt.append("'"+user_id+"','"+org_id+"','"+sdf.format(new Date())+"','"+uuid+"'),");
					
					//count+=costList.size();
				}
			}
			else
			{
				count++;
				String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
				//CacheUtils.removeMe("payTempList_"+user_id);
				//throw new Exception("订单号："+order_code+"相关成本信息不存在！");
				BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(object[24])?object[24].contains(",")?object[24].replace(",", ""):object[24].toString():"0");
				BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(object[26])?object[26].contains(",")?object[26].replace(",", ""):object[26].toString():"0");
				BigDecimal tax_rate=(new BigDecimal(StringUtils.hasText(object[25])?object[25].contains(",")?object[25].replace(",", ""):object[25].toString():"0")).divide(new BigDecimal(StringUtils.hasText(object[24])?object[24].contains(",")?object[24].replace(",", ""):object[24].toString():"0"),2,BigDecimal.ROUND_HALF_UP);
				tt.append("('"+month+"','"+object[4].toString()+"','',");
				String supplier_name=object[10].toString();
				for(Map<String,Object> map:adjList)
				{
					if(map.get("s_supplier_name").equals(supplier_name))
					{
						supplier_name=map.get("t_supplier_name").toString();
						break;
					}
				}
				tt.append("'"+supplier_name+"','"+order_code+"','"+object[6].toString()+"',");
				tt.append("'"+object[7].toString()+"','物资采购订单',"+invoice_amt+","+invoice_amt_e_tax+",");
				tt.append(""+invoice_amt+","+invoice_amt_e_tax+","+tax_rate+",'"+object[34].toString()+"',");
				tt.append("'线上','"+object[37].toString()+"','"+object[51].toString()+"','"+object[1].toString()+"',");
				tt.append("'"+user_id+"','"+org_id+"','"+sdf.format(new Date())+"','"+uuid+"'),");
			}
			
			if(count!=0&&count%1000==0)
			{
				jdbcTemplate.execute(sqlTemp+tt.toString().substring(0, tt.length() - 1));
				tt.delete(0, tt.length());
			}
		}
		System.out.println("###### 要导入临时表数据的size："+count+" 完成时间："+new Date());
		if(tt.length()>0)
		{
			jdbcTemplate.execute(sqlTemp+tt.toString().substring(0, tt.length() - 1));
			tt.delete(0, tt.length());
		}
		CacheUtils.removeMe("payTempList_"+user_id);
		System.out.println("###### 拆分导入数据到临时表 end : "+new Date());
	}

	@Override
	public void confirm(String user_id) throws Exception {
		// TODO Auto-generated method stub
		CacheUtils.removeMe("importList_"+user_id);
		System.out.println("###### 将payment_online_xxx表数据转到t_payment start : "+new Date());
		String sql="insert into t_payment(month,project_code,supplier_code,supplier_name,order_code,address_code,address_name,account_name,invoice_amt,invoice_amt_e_tax,pay_amt,"
				+ "pay_amt_e_tax,tax_rate,remark,online_offline,settle_no,voucher_no,accounting_org,update_by,org_id,update_time,uuid,flag) select t.month,t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.address_code,"
				+ "t.address_name,t.account_name,t.invoice_amt,t.invoice_amt_e_tax,t.pay_amt,t.pay_amt_e_tax,t.tax_rate,t.remark,t.online_offline,t.settle_no,t.voucher_no,t.accounting_org,t.update_by,t.org_id,t.update_time,t.uuid,case when exists(select 1 from t_cost c where c.org_id = t.org_id and c.project_code = t.project_code and c.supplier_name = t.supplier_name and c.account_name = t.account_name and c.order_type like '退库红冲%') then 1 else 0 end from payment_online_"+user_id+" t";
		/*String sql="insert into t_payment(month,project_code,supplier_code,supplier_name,order_code,address_code,address_name,account_name,invoice_amt,invoice_amt_e_tax,pay_amt,"
				+ "pay_amt_e_tax,tax_rate,remark,online_offline,settle_no,voucher_no,accounting_org,update_by,org_id,update_time,uuid) select t.month,t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.address_code,"
				+ "t.address_name,t.account_name,t.invoice_amt,t.invoice_amt_e_tax,t.pay_amt,t.pay_amt_e_tax,t.tax_rate,t.remark,t.online_offline,t.settle_no,t.voucher_no,t.accounting_org,t.update_by,t.org_id,t.update_time,t.uuid from payment_online_"+user_id+" t";*/
		jdbcTemplate.execute(sql);
		jdbcTemplate.execute("insert into t_over_view(project_code,supplier_name,account_name,month,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid,flag) select project_code,supplier_name,account_name,month,0,pay_amt_e_tax,0,invoice_amt,invoice_amt_e_tax,'t_payment',org_id,uuid,case when exists(select 1 from t_cost c where c.org_id = t.org_id and c.project_code = t.project_code and c.supplier_name = t.supplier_name and c.account_name = t.account_name and c.order_type like '退库红冲%') then 1 else 0 end from payment_online_"+user_id+" t");
		//jdbcTemplate.execute("insert into t_over_view(project_code,supplier_name,account_name,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid) select project_code,supplier_name,account_name,0,pay_amt_e_tax,0,invoice_amt,invoice_amt_e_tax,'t_payment',org_id,uuid from payment_online_"+user_id);
		jdbcTemplate.execute("drop table payment_online_"+user_id);
		System.out.println("###### 将payment_online_xxx表数据转到t_payment end : "+new Date());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> check(String user_id,String org_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> result=new HashMap<String,Object>();
		List<String> importList=(List<String>) CacheUtils.getCache("importList_"+user_id);
		if(importList!=null&&importList.size()>0)
		{
			System.out.println("###### 存在的结算单号数量："+importList.size()+" 时间："+new Date());
			String sql="select distinct settle_no from t_payment where org_id = '"+org_id+"'";
			List<String> hasList=jdbcTemplate.queryForList(sql, String.class);
			if(hasList!=null&&hasList.size()>0)
			{
				System.out.println("###### t_payment中存在的结算单号数量："+hasList.size()+" 时间："+new Date());
				boolean has=false;
				String tip="";
				for(String str:importList)
				{
					if(hasList.contains(str))
					{
						has=true;
						tip+=str;
						break;
					}
				}
				if(has)
				{
					System.out.println("###### 重复的结算单号："+tip+" 时间："+new Date());
					tip="结算单号："+tip+"已存在于付款记录表，请确认是否继续导入?";
					result.put("has", true);
					result.put("tip", tip);
				}
			}
		}
		else
		{
			throw new Exception("没有可确认导入的数据，请先上传！");
		}
		return result;
	}

}
