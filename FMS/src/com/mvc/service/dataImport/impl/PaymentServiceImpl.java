package com.mvc.service.dataImport.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.catalina.tribes.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TPayment;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.dataImport.PaymentServiceI;
import com.mvc.utils.ExcelReaderUtil;
@Service
public class PaymentServiceImpl implements PaymentServiceI{
	@Autowired
	private BaseDaoI<TPayment> payDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	@Override
	public List<Map<String,Object>> dataGrid(TPayment info, PageFilter ph) {
		// TODO Auto-generated method stub
		//String hql="from TGodownEntry t order by t." + ph.getSort() + " " + ph.getOrder();
		System.out.println("###### pay datagrid start time : "+sdf.format(new Date()));
		String sql="select t.id,t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,"
				+ "t.address_code,t.address_name,format(t.invoice_amt,2) as invoice_amt_txt,t.invoice_amt,format(t.invoice_amt_e_tax,2) "
				+ "as invoice_amt_e_tax_txt,t.invoice_amt_e_tax,format(t.pay_amt,2) as pay_amt_txt,t.pay_amt,format(t.pay_amt_e_tax,2) as pay_amt_e_tax_txt,t.pay_amt_e_tax,t.month,"
				+ "t.voucher_no,t.online_offline,t.settle_no,t.remark,"
				+ "floor(t.tax_rate*100) as rate_txt,t.tax_rate,u.user_name as update_by,t.update_time,t.uuid from t_payment t inner join user_info u on t.update_by = u.user_id "+whereSql(info);
		if(ph==null)
		{
			sql+=" order by t.id asc";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		System.out.println("###### pay datagrid end time : "+sdf.format(new Date()));
		return mapList;
	}

	@Override
	public Long count(TPayment info, PageFilter ph) {
		// TODO Auto-generated method stub
		System.out.println("###### pay count start time : "+sdf.format(new Date()));
		String hql="select count(*) from TPayment t "+whereSql(info);
		Long count=payDao.count(hql);
		System.out.println("###### pay count end time : "+sdf.format(new Date()));
		return count;
	}
	
	private String whereSql(TPayment info)
	{
		String sql=" where t.org_id = '"+info.getOrg_id()+"' ";
		if(StringUtils.hasText(info.getProject_code()))
		{
			sql+=" and t.project_code like '%"+info.getProject_code()+"%'";
		}
		if(StringUtils.hasText(info.getSupplier_name()))
		{
			sql+=" and t.supplier_name like '%"+info.getSupplier_name()+"%'";
		}
		if(StringUtils.hasText(info.getMonth()))
		{
			sql+=" and t.month = '"+info.getMonth()+"'";
		}
		if(StringUtils.hasText(info.getVoucher_no()))
		{
			sql+=" and t.voucher_no like '%"+info.getVoucher_no()+"%'";
		}
		return sql;
	}

	@Override
	public void delete(String ids,String org_id) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("###### pay delete start time : "+sdf.format(new Date()));
		if(StringUtils.hasText(ids))
		{
			if("all".equals(ids))
			{
				jdbcTemplate.execute("delete from t_payment where org_id = '"+org_id+"'");
				jdbcTemplate.execute("delete from t_over_view where table_name = 't_payment' and org_id = '"+org_id+"'");
			}
			else
			{
				jdbcTemplate.execute("delete v from t_over_view v where v.table_name = 't_payment' and v.org_id = '"+org_id+"' and exists (select 1 from t_payment t where t.id in ("+ids+") and t.uuid = v.uuid)");
				System.out.println("###### 删除t_over_view 数据完成："+sdf.format(new Date()));
				jdbcTemplate.execute("delete from t_payment where id in ("+ids+")");
				System.out.println("###### 删除t_payment 数据完成："+sdf.format(new Date()));
			}
		}
		System.out.println("###### pay delete end time : "+sdf.format(new Date()));
	}

	@Override
	public void edit(TPayment info) throws Exception {
		// TODO Auto-generated method stub
		payDao.update(info);
		jdbcTemplate.execute("update t_over_view set project_code = '"+info.getProject_code()+"',supplier_name = '"+info.getSupplier_name()+"',account_name = '"+info.getAccount_name()+"',t_pay_amt = "+info.getPay_amt_e_tax()+",t_invoice_amt = "+info.getInvoice_amt()+",t_invoice_amt_e_tax = "+info.getInvoice_amt_e_tax()+"  where uuid = '"+info.getUuid()+"'");
	}

	@Override
	public void upload(UploadInfo info) throws Exception {
		// TODO Auto-generated method stub
		int count=0;
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("###### upload start time : "+sdf.format(new Date()));
		List<String[]> allList=ExcelReaderUtil.readExcel(info.getUpFile());
		try
		{	
			/*boolean isExist=tableExistOrNot(info.getUserId());
			if(!isExist)
			{
				createTable(info.getUserId());
			}
			else
			{
				jdbcTemplate.execute("delete from payment_temp_"+info.getUserId());
			}*/
			List<Map<String,Object>> tableList=jdbcTemplate.queryForList("select TABLE_NAME from information_schema.TABLES where table_schema ='fms' and table_name = 'payment_temp_"+info.getUserId()+"'");
			if(tableList!=null&&tableList.size()>0)
			{
				jdbcTemplate.execute("delete from payment_temp_"+info.getUserId());
			}
			else
			{
				jdbcTemplate.execute("create table payment_temp_"+info.getUserId()+" like "+info.getTableName());
			}
			System.out.println("###### 文件解析完成 : "+sdf.format(new Date()));
			//供应商调整表
			List<Map<String,Object>> adjList=jdbcTemplate.queryForList("select s_supplier_name,t_supplier_name from t_supplier_adj where org_id = '"+info.getOrgId()+"'");
			StringBuffer sb=new StringBuffer("insert into payment_temp_"+info.getUserId()+"(project_code,supplier_code,supplier_name,order_code,account_name,address_code,address_name,invoice_amt,invoice_amt_e_tax,pay_amt,pay_amt_e_tax,month,voucher_no,online_offline,settle_no,remark,tax_rate,update_by,org_id,update_time,uuid) values ");
			StringBuffer tt=new StringBuffer();
			String importType=info.getImportType();
			if("0".equals(importType))
			{
				if(allList!=null&&allList.size()>0)
				{
					for(int i=0;i<allList.size();i++)
					{
						String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
						count=i;
						String[] str=allList.get(i);
						BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
						BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
						BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
						//System.out.println("###### : "+str[10]);
						BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
						String supplier_name=str[2];
						for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
						tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+str[4]+"','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
						if(i!=0&&i%1000==0)
						{
							jdbcTemplate.execute(sb+tt.toString().substring(1));
							tt.delete(0,tt.length());
						}
					}
					if(tt.length()>0)
					{
						jdbcTemplate.execute(sb+tt.toString().substring(1));
						tt.delete(0,tt.length());
					}
				}
			}
			else if("1".equals(importType))
			{
				for(int i=0;i<allList.size();i++)
				{
					count=i;
					String[] str=allList.get(i);
					String account_name=str[4];
					if(StringUtils.hasText(account_name))//判断科目名称是否为空
					{
						if(account_name.contains("!"))//判断科目名称是否含有！
						{
							String[] temp=account_name.split("!");
							String accounts="";
							for(String s:temp)
							{
								if(StringUtils.hasText(s))
								{
									accounts+=",'"+s+"'";
								}
							}
							List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select account_name,sum(account_amt) as account_amt from t_cost where project_code='"+str[0]+"' and supplier_name = '"+str[2]+"' and account_name not in ("+accounts.substring(1)+") and org_id = '"+info.getOrgId()+"' group by account_name");
							if(mapList!=null&&mapList.size()>0)
							{
								BigDecimal account_amt_all=new BigDecimal(0);
								BigDecimal invoice_amt_all=new BigDecimal(0);
								BigDecimal invoice_amt_e_tax_all=new BigDecimal(0);
								BigDecimal pay_amt_all=new BigDecimal(0);
								BigDecimal pay_amt_e_tax_all=new BigDecimal(0);
								for(Map<String,Object> map:mapList)
								{
									account_amt_all=account_amt_all.add(new BigDecimal(map.get("account_amt").toString()));
								}
								if(account_amt_all.compareTo(new BigDecimal(0))==0)
								{
									String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
									String account_name_new=mapList.get(0).get("account_name").toString();
									BigDecimal invoice_amt_new=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
									BigDecimal invoice_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
									BigDecimal pay_amt_new=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
									BigDecimal pay_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
									String supplier_name=str[2];
									for(Map<String,Object> map:adjList)
									{
										if(map.get("s_supplier_name").equals(supplier_name))
										{
											supplier_name=map.get("t_supplier_name").toString();
											break;
										}
									}
									tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt_new+","+invoice_amt_e_tax_new+","+pay_amt_new+","+pay_amt_e_tax_new+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
								}
								else
								{
									for(int j=0;j<mapList.size()-1;j++)
									{
										String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
										String account_name_new=mapList.get(j).get("account_name").toString();
										BigDecimal account_amt_new=new BigDecimal(mapList.get(j).get("account_amt").toString());
																
										BigDecimal invoice_amt_new=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
										BigDecimal invoice_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
										BigDecimal pay_amt_new=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
										BigDecimal pay_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
										invoice_amt_all=invoice_amt_all.add(invoice_amt_new);
										invoice_amt_e_tax_all=invoice_amt_e_tax_all.add(invoice_amt_e_tax_new);
										pay_amt_all=pay_amt_all.add(pay_amt_new);
										pay_amt_e_tax_all=pay_amt_e_tax_all.add(pay_amt_e_tax_new);
										String supplier_name=str[2];
										for(Map<String,Object> map:adjList)
										{
											if(map.get("s_supplier_name").equals(supplier_name))
											{
												supplier_name=map.get("t_supplier_name").toString();
												break;
											}
										}
										tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt_new+","+invoice_amt_e_tax_new+","+pay_amt_new+","+pay_amt_e_tax_new+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
									}
									
									String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
									//处理尾差
									String account_name_new=mapList.get(mapList.size()-1).get("account_name").toString();
									
									BigDecimal invoice_amt_new=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0").subtract(invoice_amt_all);
									BigDecimal invoice_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0").subtract(invoice_amt_e_tax_all);
									BigDecimal pay_amt_new=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0").subtract(pay_amt_all);
									BigDecimal pay_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0").subtract(pay_amt_e_tax_all);
									String supplier_name=str[2];
									for(Map<String,Object> map:adjList)
									{
										if(map.get("s_supplier_name").equals(supplier_name))
										{
											supplier_name=map.get("t_supplier_name").toString();
											break;
										}
									}
									tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt_new+","+invoice_amt_e_tax_new+","+pay_amt_new+","+pay_amt_e_tax_new+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
									
								}
							}
							else
							{
								String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
								//throw new Exception("项目编号："+str[0]+",供应商名称："+str[2]+",不存在除（"+accounts.substring(1)+"）以外的成本单数据！");
								BigDecimal invoice_amt_new=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
								BigDecimal invoice_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
								BigDecimal pay_amt_new=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
								BigDecimal pay_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
								String supplier_name=str[2];
								for(Map<String,Object> map:adjList)
								{
									if(map.get("s_supplier_name").equals(supplier_name))
									{
										supplier_name=map.get("t_supplier_name").toString();
										break;
									}
								}
								tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','初始化待调整','"+str[5]+"','"+str[6]+"',"+invoice_amt_new+","+invoice_amt_e_tax_new+","+pay_amt_new+","+pay_amt_e_tax_new+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
							}
						}
						else
						{
							List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select * from t_account where account_name = '"+account_name+"' and org_id = '"+info.getOrgId()+"'");
							if(mapList!=null&&mapList.size()>0)//判断科目名称是否正确
							{
								String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
								BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
								BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
								BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
								BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
								String supplier_name=str[2];
								for(Map<String,Object> map:adjList)
								{
									if(map.get("s_supplier_name").equals(supplier_name))
									{
										supplier_name=map.get("t_supplier_name").toString();
										break;
									}
								}
								tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name+"','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
							}
							else
							{
								String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
								BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
								BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
								BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
								BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
								String supplier_name=str[2];
								for(Map<String,Object> map:adjList)
								{
									if(map.get("s_supplier_name").equals(supplier_name))
									{
										supplier_name=map.get("t_supplier_name").toString();
										break;
									}
								}
								tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','初始化待调整','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
							}
						}
						
					}
					else
					{
						List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select account_name,sum(account_amt) as account_amt from t_cost where project_code='"+str[0]+"' and supplier_name = '"+str[2]+"' and org_id = '"+info.getOrgId()+"' group by account_name");
						if(mapList!=null&&mapList.size()>0)
						{
							BigDecimal account_amt_all=new BigDecimal(0);
							BigDecimal invoice_amt_all=new BigDecimal(0);
							BigDecimal invoice_amt_e_tax_all=new BigDecimal(0);
							BigDecimal pay_amt_all=new BigDecimal(0);
							BigDecimal pay_amt_e_tax_all=new BigDecimal(0);
							for(Map<String,Object> map:mapList)
							{
								account_amt_all=account_amt_all.add(new BigDecimal(map.get("account_amt").toString()));
							}
							
							if(account_amt_all.compareTo(new BigDecimal(0))==0)
							{
								String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
								String account_name_new=mapList.get(0).get("account_name").toString();
								BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
								BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
								BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
								BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
								String supplier_name=str[2];
								for(Map<String,Object> map:adjList)
								{
									if(map.get("s_supplier_name").equals(supplier_name))
									{
										supplier_name=map.get("t_supplier_name").toString();
										break;
									}
								}
								tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
							}
							else
							{
								for(int j=0;j<mapList.size()-1;j++)
								{
									String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
									String account_name_new=mapList.get(j).get("account_name").toString();
									BigDecimal account_amt_new=new BigDecimal(mapList.get(j).get("account_amt").toString());
									
									BigDecimal invoice_amt_new=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
									BigDecimal invoice_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
									BigDecimal pay_amt_new=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
									BigDecimal pay_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
									invoice_amt_all=invoice_amt_all.add(invoice_amt_new);
									invoice_amt_e_tax_all=invoice_amt_e_tax_all.add(invoice_amt_e_tax_new);
									pay_amt_all=pay_amt_all.add(pay_amt_new);
									pay_amt_e_tax_all=pay_amt_e_tax_all.add(pay_amt_e_tax_new);
									String supplier_name=str[2];
									for(Map<String,Object> map:adjList)
									{
										if(map.get("s_supplier_name").equals(supplier_name))
										{
											supplier_name=map.get("t_supplier_name").toString();
											break;
										}
									}
									tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt_new+","+invoice_amt_e_tax_new+","+pay_amt_new+","+pay_amt_e_tax_new+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
									
								}
								
								String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
								//处理尾差
								String account_name_new=mapList.get(mapList.size()-1).get("account_name").toString();
								
								BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0").subtract(invoice_amt_all);
								BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0").subtract(invoice_amt_e_tax_all);
								BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0").subtract(pay_amt_all);
								BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0").subtract(pay_amt_e_tax_all);
								String supplier_name=str[2];
								for(Map<String,Object> map:adjList)
								{
									if(map.get("s_supplier_name").equals(supplier_name))
									{
										supplier_name=map.get("t_supplier_name").toString();
										break;
									}
								}
								tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
								
							}
						}
						else
						{
							String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
							//throw new Exception("项目编号："+str[0]+",供应商名称："+str[2]+",不存在成本单数据！");
							BigDecimal invoice_amt_new=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
							BigDecimal invoice_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
							BigDecimal pay_amt_new=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
							BigDecimal pay_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
							String supplier_name=str[2];
							for(Map<String,Object> map:adjList)
							{
								if(map.get("s_supplier_name").equals(supplier_name))
								{
									supplier_name=map.get("t_supplier_name").toString();
									break;
								}
							}
							tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','初始化待调整','"+str[5]+"','"+str[6]+"',"+invoice_amt_new+","+invoice_amt_e_tax_new+","+pay_amt_new+","+pay_amt_e_tax_new+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
						}
					}
					
					if(i!=0&&i%1000==0)
					{
						jdbcTemplate.execute(sb+tt.toString().substring(1));
						tt.delete(0,tt.length());
					}
				}
				
				if(tt.length()>0)
				{
					jdbcTemplate.execute(sb+tt.toString().substring(1));
					tt.delete(0,tt.length());
				}
			}
			else if("2".equals(importType))
			{
				for(int i=0;i<allList.size();i++)
				{
					count=i;
					String[] str=allList.get(i);
					List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select account_name,sum(account_amt) as account_amt from t_cost where order_code = '"+str[3]+"' and org_id = '"+info.getOrgId()+"' group by account_name");
					if(mapList!=null&&mapList.size()>0)
					{
						BigDecimal account_amt_all=new BigDecimal(0);
						BigDecimal invoice_amt_all=new BigDecimal(0);
						BigDecimal invoice_amt_e_tax_all=new BigDecimal(0);
						BigDecimal pay_amt_all=new BigDecimal(0);
						BigDecimal pay_amt_e_tax_all=new BigDecimal(0);
						for(Map<String,Object> map:mapList)
						{
							account_amt_all=account_amt_all.add(new BigDecimal(map.get("account_amt").toString()));
						}
						
						if(account_amt_all.compareTo(new BigDecimal(0))==0)
						{
							String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
							String account_name_new=mapList.get(0).get("account_name").toString();
							BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
							BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
							BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
							BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
							String supplier_name=str[2];
							for(Map<String,Object> map:adjList)
							{
								if(map.get("s_supplier_name").equals(supplier_name))
								{
									supplier_name=map.get("t_supplier_name").toString();
									break;
								}
							}
							tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
						}
						else
						{
							for(int j=0;j<mapList.size()-1;j++)
							{
								String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
								String account_name_new=mapList.get(j).get("account_name").toString();
								BigDecimal account_amt_new=new BigDecimal(mapList.get(j).get("account_amt").toString());
								
								BigDecimal invoice_amt_new=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
								BigDecimal invoice_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
								BigDecimal pay_amt_new=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
								BigDecimal pay_amt_e_tax_new=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0").multiply(account_amt_new).divide(account_amt_all, 2, BigDecimal.ROUND_HALF_UP);
								invoice_amt_all=invoice_amt_all.add(invoice_amt_new);
								invoice_amt_e_tax_all=invoice_amt_e_tax_all.add(invoice_amt_e_tax_new);
								pay_amt_all=pay_amt_all.add(pay_amt_new);
								pay_amt_e_tax_all=pay_amt_e_tax_all.add(pay_amt_e_tax_new);
								String supplier_name=str[2];
								for(Map<String,Object> map:adjList)
								{
									if(map.get("s_supplier_name").equals(supplier_name))
									{
										supplier_name=map.get("t_supplier_name").toString();
										break;
									}
								}
								tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt_new+","+invoice_amt_e_tax_new+","+pay_amt_new+","+pay_amt_e_tax_new+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
							}
							
							String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
							//处理尾差
							String account_name_new=mapList.get(mapList.size()-1).get("account_name").toString();
							BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0").subtract(invoice_amt_all);
							BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0").subtract(invoice_amt_e_tax_all);
							BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0").subtract(pay_amt_all);
							BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0").subtract(pay_amt_e_tax_all);
							String supplier_name=str[2];
							for(Map<String,Object> map:adjList)
							{
								if(map.get("s_supplier_name").equals(supplier_name))
								{
									supplier_name=map.get("t_supplier_name").toString();
									break;
								}
							}
							tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','"+account_name_new+"','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
						}
					}
					else
					{
						String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
						//String account_name_new=mapList.get(0).get("account_name").toString();
						BigDecimal invoice_amt=new BigDecimal(StringUtils.hasText(str[7])?str[7].contains(",")?str[7].replace(",", ""):str[7]:"0");
						BigDecimal invoice_amt_e_tax=new BigDecimal(StringUtils.hasText(str[8])?str[8].contains(",")?str[8].replace(",", ""):str[8]:"0");
						BigDecimal pay_amt=new BigDecimal(StringUtils.hasText(str[9])?str[9].contains(",")?str[9].replace(",", ""):str[9]:"0");
						BigDecimal pay_amt_e_tax=new BigDecimal(StringUtils.hasText(str[10])?str[10].contains(",")?str[10].replace(",", ""):str[10]:"0");
						String supplier_name=str[2];
						for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
						tt.append(",('"+str[0]+"','"+str[1]+"','"+supplier_name+"','"+str[3]+"','物资采购订单','"+str[5]+"','"+str[6]+"',"+invoice_amt+","+invoice_amt_e_tax+","+pay_amt+","+pay_amt_e_tax+",'"+str[11]+"','"+str[12]+"','"+str[13]+"','"+str[14]+"','"+str[15]+"','"+str[16]+"','"+info.getUserId()+"','"+info.getOrgId()+"','"+sd.format(new Date())+"','"+uuid+"')");
						//throw new Exception("订单号："+str[3]+",不存在成本单数据！");
					}
					if(i!=0&&i%1000==0)
					{
						jdbcTemplate.execute(sb+tt.toString().substring(1));
						tt.delete(0,tt.length());
					}
				}
				
				if(tt.length()>0)
				{
					jdbcTemplate.execute(sb+tt.toString().substring(1));
					tt.delete(0,tt.length());
				}
			}
			else
			{
				throw new Exception("未知导入方式！");
			}
			
			if("1".equals(info.getIsFull()))
			{
				jdbcTemplate.execute("delete from t_payment where org_id = '"+info.getOrgId()+"'");
				jdbcTemplate.execute("delete from t_over_view where table_name = 't_payment' and org_id = '"+info.getOrgId()+"'");
			}
			//jdbcTemplate.execute("insert into t_payment(project_code,supplier_code,supplier_name,order_code,account_name,address_code,address_name,invoice_amt,invoice_amt_e_tax,pay_amt,pay_amt_e_tax,month,voucher_no,online_offline,settle_no,remark,tax_rate,accounting_org,update_by,org_id,update_time,uuid) select  t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,t.address_code,t.address_name,t.invoice_amt,t.invoice_amt_e_tax,t.pay_amt,t.pay_amt_e_tax,t.month,t.voucher_no,t.online_offline,t.settle_no,t.remark,t.tax_rate,t.accounting_org,t.update_by,t.org_id,t.update_time,t.uuid from payment_temp_"+info.getUserId()+" t");
			//jdbcTemplate.execute("insert into t_over_view(project_code,supplier_name,account_name,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid) select t.project_code,t.supplier_name,t.account_name,null,t.pay_amt_e_tax,0,t.invoice_amt,t.invoice_amt_e_tax,'t_payment',t.org_id,t.uuid from payment_temp_"+info.getUserId()+" t");
			jdbcTemplate.execute("insert into t_payment(project_code,supplier_code,supplier_name,order_code,account_name,address_code,address_name,invoice_amt,invoice_amt_e_tax,pay_amt,pay_amt_e_tax,month,voucher_no,online_offline,settle_no,remark,tax_rate,accounting_org,update_by,org_id,update_time,uuid,flag) select  t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,t.address_code,t.address_name,t.invoice_amt,t.invoice_amt_e_tax,t.pay_amt,t.pay_amt_e_tax,t.month,t.voucher_no,t.online_offline,t.settle_no,t.remark,t.tax_rate,t.accounting_org,t.update_by,t.org_id,t.update_time,t.uuid,case when exists(select 1 from t_cost c where c.org_id = t.org_id and c.project_code = t.project_code and c.supplier_name = t.supplier_name and c.account_name = t.account_name and c.order_type like '退库红冲%') then 1 else 0 end from payment_temp_"+info.getUserId()+" t");
			jdbcTemplate.execute("insert into t_over_view(project_code,supplier_name,account_name,month,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid,flag) select t.project_code,t.supplier_name,t.account_name,t.month,null,t.pay_amt_e_tax,0,t.invoice_amt,t.invoice_amt_e_tax,'t_payment',t.org_id,t.uuid,case when exists(select 1 from t_cost c where c.org_id = t.org_id and c.project_code = t.project_code and c.supplier_name = t.supplier_name and c.account_name = t.account_name and c.order_type like '退库红冲%') then 1 else 0 end from payment_temp_"+info.getUserId()+" t");
			jdbcTemplate.execute("drop table payment_temp_"+info.getUserId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("第"+(count+2)+"行数据存在问题，请核对！ ### "+Arrays.toString(allList.get(count)).toString());
		}
		System.out.println("###### upload end time : "+new Date());
	}
	
	/*private boolean tableExistOrNot(String user_id)
	{
		boolean isExist=false;
		String sql="";
		try
		{
			sql="select count(*) from payment_temp_"+user_id;
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
         String sql="CREATE TABLE payment_temp_"+user_id+" (`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,"
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
	}*/
	
}
