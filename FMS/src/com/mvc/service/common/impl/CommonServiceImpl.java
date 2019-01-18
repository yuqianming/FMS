package com.mvc.service.common.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.mvc.model.sys.TAccount;
import com.mvc.model.sys.TLog;
import com.mvc.model.sys.TSupplier;
import com.mvc.model.sys.TTableExcel;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.common.CommonServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExcelReaderUtil;

@Service
public class CommonServiceImpl implements CommonServiceI{
	@Autowired
	private BaseDaoI<TLog> logDao;
	@Autowired
	private BaseDaoI<TTableExcel> tableDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public String tip(String tableName,String org_id) throws Exception {
		// TODO Auto-generated method stub
		String result="";
		String sql="select u.user_name,t.log_time from t_log t,user_info u where t.user_id=u.user_id and t.table_name ='"+tableName+"' ";
		if(!"t_tax_rate".equals(tableName))
		{
			sql+=" and t.org_id = '"+org_id+"' ";
		}
		sql+=" order by t.log_time desc";
		List<Object[]> logList=logDao.findBySql(sql);
		if(logList!=null&&logList.size()>0)
		{
			Object[] object=logList.get(0);
			if(object[0]!=null)
			{
				result+=object[0].toString();
			}
			result+="   ";
			if(object[1]!=null)
			{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				result+=sdf.format(object[1]);
			}

		}
		return result;
	}
	@Override
	public int upload(UploadInfo info,HttpSession session) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("###### 数据导入开始 ："+sdf.format(new Date()));
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
		CacheUtils.cacheMe("upload_"+info.getUserId(), allList);
		CacheUtils.cacheMe("fileName_"+info.getUserId(), info.getUpFile().getOriginalFilename());
		System.out.println("###### 文件解析为数据结束 ："+sdf.format(new Date()));
		return allList.size();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void save(UploadInfo info) throws Exception {
		// TODO Auto-generated method stub
		List<String[]> allList=(List<String[]>) CacheUtils.getCache("upload_"+info.getUserId());
		String fileName=(String) CacheUtils.getCache("fileName_"+info.getUserId());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		List<String> sqlList = new ArrayList<String>();
		List<TAccount> accountList=new ArrayList<TAccount>();
		List<TSupplier> supplierList=new ArrayList<TSupplier>();
		boolean synchronize=false;
		if("t_cost".equals(info.getTableName())&&"1".equals(info.getIsSynchronize()))
		{
			synchronize=true;
		}
		List<TTableExcel> columns=tableDao.find("from TTableExcel t where t.table_name ='"+info.getTableName()+"'");
		//供应商调整表
		List<Map<String,Object>> adjList=jdbcTemplate.queryForList("select s_supplier_name,t_supplier_name from t_supplier_adj where org_id = '"+info.getOrgId()+"'");
		if(columns!=null&&columns.size()>0)
		{
			if(allList!=null&&allList.size()>0)
			{
				if(!"1".equals(info.getIsFull()))
				{
					if(synchronize)//同步数据字典（科目/供应商）
					{
						supplierList=jdbcTemplate.query("select * from t_supplier where org_id = '"+info.getOrgId()+"'", new Object[]{}, new BeanPropertyRowMapper<TSupplier>(TSupplier.class));
						accountList=jdbcTemplate.query("select * from t_account where org_id = '"+info.getOrgId()+"'", new Object[]{}, new BeanPropertyRowMapper<TAccount>(TAccount.class));
					}
				}
				
				List<Map<String,Object>> tableList=jdbcTemplate.queryForList("select TABLE_NAME from information_schema.TABLES where table_schema ='fms' and table_name = '"+info.getTableName()+"_"+info.getUserId()+"'");
				if(tableList!=null&&tableList.size()>0)
				{
					jdbcTemplate.execute("delete from "+info.getTableName()+"_"+info.getUserId());
				}
				else
				{
					jdbcTemplate.execute("create table "+info.getTableName()+"_"+info.getUserId()+" like "+info.getTableName());
				}
				
				//通用表导入sql
				String sql="insert into "+info.getTableName()+"_"+info.getUserId()+" (";
				//字段
				String fields="";
				//用于整体浏览的数据sql
				//String overview="";
				for(TTableExcel tt:columns)
				{
					fields+=tt.getField().trim()+",";
				}
				if("t_cost".equals(info.getTableName())||"t_signature".equals(info.getTableName()))
				{
					fields+="update_by,org_id,update_time,uuid";
					//overview="insert into t_over_view(project_code,supplier_name,account_name,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid) values ";
				}
				else
				{
					fields+="update_by,org_id,update_time";
				}
				sql+=fields+") values";
				//科目导入sql
				String accountSql="insert into t_account(account_code,account_name,update_by,org_id,update_time) values ";
				//供应商导入结果
				String supplierSql="insert into t_supplier(supplier_code,supplier_name,unique_name,update_by,org_id,update_time) values ";
				
				StringBuffer t1 = new StringBuffer();
				StringBuffer t2 = new StringBuffer();
				StringBuffer t3 = new StringBuffer();
				StringBuffer t4 = new StringBuffer();
				for(int i=0;i<allList.size();i++)
				{
					String[] object=allList.get(i);
					TAccount account=new TAccount();
					TSupplier supplier=new TSupplier();
					t1.append("(");
					/*if(object.length<columns.size())
					{
						throw new Exception("导入的Excel格式不正确！");
					}*/
					t4.append("(");
					for(TTableExcel tt:columns)
					{
						String temp="";
						if(object.length>tt.getColumn_num()-1)
						{
							temp=object[tt.getColumn_num()-1];
							if("t_cost".equals(info.getTableName())||"t_signature".equals(info.getTableName()))
							{
								if("supplier_name".equals(tt.getField().trim()))
								{
									for(Map<String,Object> map:adjList)
									{
										if(map.get("s_supplier_name").equals(temp))
										{
											temp=map.get("t_supplier_name").toString();
											break;
										}
									}
								}
							}	
						}
						
						if(synchronize)//同步数据字典（科目/供应商）
						{
							if("account_code".equals(tt.getField().trim()))
							{
								account.setAccount_code(temp);
							}
							else if("account_name".equals(tt.getField().trim()))
							{
								account.setAccount_name(temp);
							}
							else if("supplier_code".equals(tt.getField().trim()))
							{
								supplier.setSupplier_code(temp);
							}
							else if("supplier_name".equals(tt.getField().trim()))
							{
								supplier.setSupplier_name(temp);
							}
						}
						
						if(StringUtils.hasText(temp))
						{
							if("BigDecimal".equals(tt.getType().trim()))
							{
								t1.append(temp.contains(",")?temp.replace(",", ""):temp);
								t1.append(",");
							}
							else if("int".equals(tt.getType().trim()))
							{
								t1.append(temp);
								t1.append(",");
							}
							else
							{
								t1.append("'");
								t1.append(temp);
								t1.append("',");
							}
						}
						else
						{
							if("BigDecimal".equals(tt.getType().trim()))
							{
								t1.append(0);
								t1.append(",");
							}
							else if("int".equals(tt.getType().trim()))
							{
								t1.append(0);
								t1.append(",");
							}
							else
							{
								t1.append("'',");
							}
						}
					}
					t1.append("'");
					t1.append(info.getUserId());
					t1.append("','");
					t1.append(info.getOrgId());
					t1.append("','");
					t1.append(sdf.format(new Date()));
					if("t_cost".equals(info.getTableName())||"t_signature".equals(info.getTableName()))
					{
						t1.append("','");
						String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
						t1.append(uuid);
					}
					t1.append("'),");
					
					if(i!=0&&i%1000==0)
					{
						jdbcTemplate.execute(sql+t1.toString().substring(0, t1.length() - 1));
						t1.delete(0,t1.length());
					}
					
					if(synchronize)//同步数据字典（科目/供应商）
					{
						boolean hasAccount=false;
						for(int j=0;j<accountList.size();j++)
						{
							if(accountList.get(j).getAccount_name().equals(account.getAccount_name()))
							{
								hasAccount=true;
								break;
							}
						}
						if(!hasAccount)
						{
							accountList.add(account);
							t2.append("('");
							t2.append(account.getAccount_code());
							t2.append("','");
							t2.append(account.getAccount_name());
							t2.append("','");
							t2.append(info.getUserId());
							t2.append("','");
							t2.append(info.getOrgId());
							t2.append("','");
							t2.append(sdf.format(new Date()));
							t2.append("'),");
						}

						boolean hasSupplier=false;
						for(int j=0;j<supplierList.size();j++)
						{
							if(supplierList.get(j).getSupplier_name().equals(supplier.getSupplier_name()))
							{
								hasSupplier=true;
								break;
							}
						}
						if(!hasSupplier)
						{
							supplierList.add(supplier);
							t3.append("('");
							t3.append(supplier.getSupplier_code());
							t3.append("','");
							t3.append(supplier.getSupplier_name());
							t3.append("','");
							t3.append(supplier.getSupplier_name());
							t3.append("','");
							t3.append(info.getUserId());
							t3.append("','");
							t3.append(info.getOrgId());
							t3.append("','");
							t3.append(sdf.format(new Date()));
							t3.append("'),");
						}
					}
				}
				
				if(t1.length()>0)
				{
					jdbcTemplate.execute(sql+t1.toString().substring(0, t1.length() - 1));
				}
				System.out.println("###### 导入到临时表完成："+sdf.format(new Date()));
				if("1".equals(info.getIsFull()))
				{
					jdbcTemplate.execute("delete from "+info.getTableName()+" where org_id = '"+info.getOrgId()+"'");
					if(synchronize)//同步数据字典（科目/供应商）
					{
						jdbcTemplate.execute("delete from t_account where org_id = '"+info.getOrgId()+"'");
						jdbcTemplate.execute("delete from t_supplier where org_id = '"+info.getOrgId()+"'");
					}
					if("t_cost".equals(info.getTableName())||"t_signature".equals(info.getTableName()))
					{
						System.out.println("###### 删除t_over_view数据开始："+sdf.format(new Date()));
						jdbcTemplate.execute("delete from t_over_view where org_id = '"+info.getOrgId()+"' and table_name = '"+info.getTableName()+"'");
						System.out.println("###### 删除t_over_view数据完成："+sdf.format(new Date()));
					}
				}
				
				jdbcTemplate.execute("insert into "+info.getTableName()+" ("+fields+") select "+fields+" from "+info.getTableName()+"_"+info.getUserId());
				System.out.println("###### 导入到正式表完成："+sdf.format(new Date()));
				if("t_cost".equals(info.getTableName())||"t_signature".equals(info.getTableName()))
				{
					String view_fields="";
					if("t_cost".equals(info.getTableName()))
					{
						view_fields="project_code,supplier_name,account_name,date_format(busi_date,'%Y%m'),account_amt,null,0,0,0,'t_cost',org_id,uuid,order_type,order_code";
						String update_sql="update t_payment t set t.flag=1 where exists (select 1 from "+info.getTableName()+"_"+info.getUserId()+" c where c.org_id = t.org_id and c.project_code = t.project_code and c.supplier_name = t.supplier_name and c.account_name = t.account_name and c.order_type like '退库红冲%')";
						jdbcTemplate.execute(update_sql);
					}
					else if("t_signature".equals(info.getTableName()))
					{
					    view_fields="project_code,supplier_name,account_name,null,null,0,pay_amt_e_tax,invoice_amt,invoice_amt_e_tax,'t_signature',org_id,uuid,null,null";
					}
					System.out.println("###### 插入t_over_view数据开始："+sdf.format(new Date()));
					jdbcTemplate.execute("insert into t_over_view (project_code,supplier_name,account_name,month,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid,order_type,order_code) select "+view_fields+" from "+info.getTableName()+"_"+info.getUserId());
					System.out.println("###### 导入到汇总表完成："+sdf.format(new Date()));
					/*if(t4.length()>0)
					{
						jdbcTemplate.execute(overview+t4.toString().substring(0, t4.length() - 1));
					}*/
				}
				jdbcTemplate.execute("drop table "+info.getTableName()+"_"+info.getUserId());
				
				
				String logSql="insert into t_log(log_type,table_name,file_name,user_id,log_time,org_id) values ('1','"+info.getTableName()+"','"+fileName+"','"+info.getUserId()+"','"+sdf.format(new Date())+"','"+info.getOrgId()+"')";
				sqlList.add(sql);
				if(synchronize)//同步数据字典（科目/供应商）
				{
					logSql+=",('2','t_account','','"+info.getUserId()+"','"+sdf.format(new Date())+"','"+info.getOrgId()+"')";
					logSql+=",('2','t_supplier','','"+info.getUserId()+"','"+sdf.format(new Date())+"','"+info.getOrgId()+"')";
					if(t2.length()>0)
					{
						jdbcTemplate.execute(accountSql+t2.toString().substring(0, t2.length() - 1));
					}
					if(t3.length()>0)
					{
						jdbcTemplate.execute(supplierSql+t3.toString().substring(0, t3.length() - 1));
						List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select s_supplier_name,t_supplier_name from t_supplier_adj where org_id = '"+info.getOrgId()+"'");
						for(Map<String,Object> map:mapList)
						{
							jdbcTemplate.execute("update t_supplier set unique_name = (select a.unique_name from (select unique_name from t_supplier  where supplier_name='"+map.get("t_supplier_name")+"' and org_id = '"+info.getOrgId()+"') a) where supplier_name='"+map.get("s_supplier_name")+"' and org_id = '"+info.getOrgId()+"'");
						}
					}	
				}
				jdbcTemplate.execute(logSql);
				System.out.println("###### 数据导入结束 ："+sdf.format(new Date()));
				CacheUtils.removeMe("upload_"+info.getUserId());
				CacheUtils.removeMe("fileName_"+info.getUserId());
			}
			else
			{
				throw new Exception("文件没有数据！");
			}
		}
		else
		{
			throw new Exception("未配置表"+info.getTableName()+"字段与Excel文件列的对应关系！");
		}
	}

}
