package com.mvc.service.test.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TCancelProj;
import com.mvc.model.sys.TCost;
import com.mvc.model.sys.TCrm;
import com.mvc.model.sys.TOldProj;
import com.mvc.model.sys.TPayment;
import com.mvc.model.sys.TPms;
import com.mvc.model.sys.TSignature;
import com.mvc.model.sys.TSupplierAdj;
import com.mvc.model.sys.TTableExcel;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.ProjectTest;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.test.ProjectTestServiceI;
import com.mvc.utils.ExcelReaderUtil;
import com.mvc.utils.ReadFileUtil;


@Service
public class ProjectTestServiceImpl implements ProjectTestServiceI{
	@Autowired
	private BaseDaoI<TCost> costDao;
	@Autowired
	private BaseDaoI<TPayment> paymentDao;
	@Autowired
	private BaseDaoI<TSignature> signatureDao;
	@Autowired
	private BaseDaoI<TPms> pmsDao;
	@Autowired
	private BaseDaoI<TCrm> crmDao;
	@Autowired
	private BaseDaoI<TCancelProj> cancelProjDao;
	@Autowired
	private BaseDaoI<TOldProj> oldProjDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BaseDaoI<TTableExcel> tableDao;
	@Autowired
	private BaseDaoI<TSupplierAdj> supplierAdjDao;
	@Override
	public void upload(UploadInfo info) throws Exception {
		// TODO Auto-generated method stub
		/*ReadFileUtil handle = new ReadFileUtil();
		List<Object[]> allList=new ArrayList<Object[]>();
		Map<String, List<String[]>> map = handle.readFile(info.getUpFile());
		//String fileName=info.getUpFile().getOriginalFilename();
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
		List<String[]> allList=ExcelReaderUtil.readExcel(info.getUpFile());
		
		if(allList.size()>0)
		{
			//String key=info.getUserId()+"-OrderTest";
			//CacheUtils.cacheMe(key, testList);
			boolean isExist=tableExistOrNot(info.getUserId());
			if(!isExist)
			{
				createTable(info.getUserId());
			}
			else
			{
				jdbcTemplate.execute("delete from project_test_"+info.getUserId());
			}
			List<String> sqlList=new ArrayList<String>();
			List<TTableExcel> columns=tableDao.find("from TTableExcel t where t.table_name ='project_test'");
			if(columns!=null&&columns.size()>0)
			{
				for(Object[] object:allList)
				{
					String sql="insert into project_test_"+info.getUserId()+" (";
					for(TTableExcel tt:columns)
					{
						sql+=tt.getField().trim()+",";
					}
					sql=sql.substring(0, sql.length()-1)+") values (";
					for(TTableExcel tt:columns)
					{
						String temp=object[tt.getColumn_num()-1].toString();
						if(StringUtils.hasText(temp))
						{
							if("BigDecimal".equals(tt.getType().trim()))
							{
								sql+=temp.contains(",")?temp.replace(",", ""):temp;
								sql+=",";
							}
							else
							{
								sql+="'"+temp+"',";
							}
						}
						else
						{
							if("BigDecimal".equals(tt.getType().trim()))
							{
								sql+=0+",";
							}
							else
							{
								sql+="'',";
							}
						}
					}
					sql=sql.substring(0, sql.length()-1)+")";
					sqlList.add(sql);
				}
				jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
				List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select distinct project_code,sum(t_invoice_amt) as t_invoice_amt,sum(t_pay_amt) as t_pay_amt from project_test_"+info.getUserId()+" group by project_code");
				if(mapList!=null&&mapList.size()>0)
				{
					jdbcTemplate.execute("delete from project_test_"+info.getUserId());
					StringBuffer sqlBuff=new StringBuffer("insert into project_test_"+info.getUserId()+"(project_code,t_invoice_amt,t_pay_amt) values ");
					for(Map<String,Object> m:mapList)
					{
						sqlBuff.append("('"+m.get("project_code")+"',"+new BigDecimal(m.get("t_invoice_amt").toString())+","+new BigDecimal(m.get("t_pay_amt").toString())+"),");
					}
					jdbcTemplate.execute(sqlBuff.substring(0, sqlBuff.length()-1));
				}
			}
			else
			{
				throw new Exception("未配置项目测试表字段与Excel文件列的对应关系！");
			}
		}
		else
		{
			throw new Exception("请上传非空文件！");
		}
	}
	
	private void InitToProjectTest(ProjectTest project) throws Exception
	{
		System.out.println("###### 项目测试开始："+new Date());
    	/*String supplier_name="";
		String supplierQuery="select supplier_name from t_supplier where unique_name = (select unique_name from t_supplier where supplier_name = '"+project.getSupplier_name()+"' and org_id = '"+project.getOrg_id()+"') and org_id = '"+project.getOrg_id()+"'";
		List<Map<String,Object>> supplierList=jdbcTemplate.queryForList(supplierQuery);
		for(Map<String,Object> supplier:supplierList)
		{
			supplier_name+=",'"+supplier.get("supplier_name")+"'";
		}
		supplier_name=supplier_name.substring(1);*/
    	String[] account_names=project.getAccount_name().split(",");
		String account_name="";
		for(String temp:account_names)
		{
			account_name+=",'"+temp+"'";
		}
		account_name=account_name.substring(1);
		/*String sql="select a.project_code,a.address_name,(case when a.account_amt is null then 0 else a.account_amt end) as c_account_amt,"
				+ "(case when b.c_invoice_amt is null then 0 else b.c_invoice_amt end) as c_invoice_amt,"
				+ "(case when b.c_pay_amt is null then 0 else b.c_pay_amt end) as c_pay_amt,"
				+ "(case when c.n_pay_amt is null then 0 else c.n_pay_amt end) as n_pay_amt from (select t.project_code,t.address_name,sum(t.account_amt) as account_amt from t_cost t where t.project_code ='"+project.getProject_code()+"' and t.supplier_name in ("+supplier_name+") and t.org_id = '"+project.getOrg_id()+"' and t.account_name in ("+account_name+")) a left join "
				+ "(select p.project_code,sum(p.invoice_amt_e_tax) as c_invoice_amt,"
				+ "sum(p.pay_amt_e_tax) as c_pay_amt from t_payment p where p.project_code ='"+project.getProject_code()+"' and p.supplier_name in ("+supplier_name+") and p.org_id = '"+project.getOrg_id()+"' and p.account_name in ("+account_name+")) b on b.project_code = a.project_code left join "
				+ "(select s.project_code,sum(s.pay_amt_e_tax) as n_pay_amt  from t_signature s where s.project_code ='"+project.getProject_code()+"' and s.supplier_name in ("+supplier_name+") and s.org_id = '"+project.getOrg_id()+"' and s.account_name in ("+account_name+")) c on c.project_code = a.project_code ";*/
		String sql="select t.project_code,t.address_name,sum(t.account_amt) as c_account_amt from t_cost t where t.project_code ='"+project.getProject_code()+"' and t.supplier_name = '"+project.getSupplier_name()+"' and t.account_name in ("+account_name+") and t.org_id = '"+project.getOrg_id()+"' group by t.project_code";
		List<ProjectTest> testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<ProjectTest>(ProjectTest.class));
		if(testList!=null&&testList.size()>0)
		{
			ProjectTest test=testList.get(0);
			project.setAddress_name(test.getAddress_name());
			project.setC_account_amt(test.getC_account_amt());
		}
		
		sql="select p.project_code,sum(p.invoice_amt_e_tax) as c_invoice_amt,sum(p.pay_amt_e_tax) as c_pay_amt from t_payment p where p.project_code ='"+project.getProject_code()+"' and p.supplier_name = '"+project.getSupplier_name()+"' and p.account_name in ("+account_name+") and p.org_id = '"+project.getOrg_id()+"' group by p.project_code";
		testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<ProjectTest>(ProjectTest.class));
		if(testList!=null&&testList.size()>0)
		{
			ProjectTest test=testList.get(0);
			project.setC_invoice_amt(test.getC_invoice_amt());
			project.setC_pay_amt(test.getC_pay_amt());
		}
		sql="select s.project_code,sum(s.pay_amt_e_tax) as n_pay_amt from t_signature s where s.project_code ='"+project.getProject_code()+"' and s.supplier_name = '"+project.getSupplier_name()+"' and s.account_name in ("+account_name+") and s.org_id = '"+project.getOrg_id()+"' group by s.project_code";
		testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<ProjectTest>(ProjectTest.class));
		if(testList!=null&&testList.size()>0)
		{
			ProjectTest test=testList.get(0);
			project.setN_pay_amt(test.getN_pay_amt());
		}
		
		if(StringUtils.hasText(project.getProject_code()))
		{
			List<TPms> pmsList=pmsDao.find("from TPms t where t.project_code ='"+project.getProject_code()+"' and t.org_id = '"+project.getOrg_id()+"'");
			if(pmsList!=null&&pmsList.size()>0)
			{
				TPms pms=pmsList.get(0);
				project.setBuild_mode(pms.getBuild_mode());
				project.setAccept_date(pms.getAccept_date());
				project.setDeliver_date(pms.getDeliver_date());
				project.setProject_status(pms.getProject_status());
			}
			
			List<TCancelProj> cancelList=cancelProjDao.find("from TCancelProj t where t.project_code = '"+project.getProject_code()+"' and t.org_id = '"+project.getOrg_id()+"'");
			if(cancelList!=null&&cancelList.size()>0)
			{
				project.setIs_cancel("已销项");
			}
			
			List<TOldProj> oldList=oldProjDao.find("from TOldProj t where t.project_code = '"+project.getProject_code()+"' and t.org_id = '"+project.getOrg_id()+"'");
			if(oldList!=null&&oldList.size()>0)
			{
				project.setIs_old("旧项目");
			}
			else
			{
				project.setIs_old("新项目");
			}
		}
		System.out.println("###### 项目测试结束："+new Date());
		List<TCrm> crmList=crmDao.find("select t from TCrm t,TPms p where t.order_code = p.order_code and p.project_code = '"+project.getProject_code()+"' and t.org_id = '"+project.getOrg_id()+"' and p.org_id = '"+project.getOrg_id()+"'");
		if(crmList!=null&&crmList.size()>0)
		{
			TCrm crm=crmList.get(0);
			project.setRent_status(crm.getRent_status());
		}
	}
	
	private boolean tableExistOrNot(String user_id)
	{
		boolean isExist=false;
		String sql="";
		try
		{
			/*sql="select count(*) from project_test_"+user_id;
			jdbcTemplate.execute(sql);
			isExist=true;
			*/
			List<Map<String,Object>> tableList=jdbcTemplate.queryForList("select TABLE_NAME from information_schema.TABLES where table_schema ='fms' and table_name = 'project_test_"+user_id+"'");
			if(tableList!=null&&tableList.size()>0)
			{
				isExist=true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isExist;
	}
	
	private void createTable(String user_id)
	{
         String sql="create table project_test_"+user_id+" (id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',project_code varchar(30) DEFAULT NULL COMMENT '项目编码',"
					+ "address_name varchar(100) DEFAULT NULL COMMENT '站址名称',supplier_name varchar(100) DEFAULT NULL COMMENT '供应商名称',"
					+ "account_name varchar(2000) DEFAULT NULL COMMENT '科目名称',c_account_amt decimal(11,2) DEFAULT NULL COMMENT '已入账金额',"
					+ "c_invoice_amt decimal(11,2) DEFAULT NULL COMMENT '已开发票金额',c_pay_amt decimal(11,2) DEFAULT NULL COMMENT '已付款金额',"
					+ "n_pay_amt decimal(11,2) DEFAULT NULL COMMENT '签字未登记',t_invoice_amt decimal(11,2) DEFAULT NULL COMMENT '本次开票金额',"
					+ "t_pay_amt decimal(11,2) DEFAULT NULL COMMENT '本次交付金额',is_pass varchar(20) DEFAULT NULL COMMENT '符合性测试',pay_rate varchar(50) DEFAULT NULL COMMENT '本次付款比例',"
					+ "build_mode varchar(20) DEFAULT NULL COMMENT '建设方式',accept_date varchar(20) DEFAULT NULL COMMENT '内验时间',"
					+ "deliver_date varchar(20) DEFAULT NULL COMMENT '交付时间',project_status varchar(20) DEFAULT NULL COMMENT '项目状态',"
					+ "rent_status varchar(20) DEFAULT NULL COMMENT '起租状态',is_cancel varchar(20) DEFAULT NULL COMMENT '是否销项',"
					+ "is_old varchar(20) DEFAULT NULL COMMENT '是否旧项目',tax_rate decimal(7,4) DEFAULT NULL COMMENT '税率',PRIMARY KEY (`id`))";
			costDao.executeSql(sql);
	}
	
	
	@Override
	public List<Map<String,Object>> dataGrid(String user_id,PageFilter ph) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> testList=new ArrayList<Map<String,Object>>();
		if(!tableExistOrNot(user_id))
		{
			return testList;
		}
		String sql="select id,project_code,account_name,supplier_name,address_name,format(c_account_amt,2) as c_account_amt,"
				+ "format(c_invoice_amt,2) as c_invoice_amt,format(c_pay_amt,2) as c_pay_amt,format(n_pay_amt,2) as n_pay_amt,"
				+ "t_invoice_amt,format(t_invoice_amt,2) as t_invoice_amt_txt,t_pay_amt,format(t_pay_amt,2) as t_pay_amt_txt,(case when (is_pass = '通过' or is_pass = '无效项目') then "
				+ "is_pass else format(is_pass,2) end) as is_pass,pay_rate,build_mode,accept_date,"
				+ "deliver_date,project_status,rent_status,is_cancel,is_old from project_test_"+user_id;
		if(ph!=null)
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		testList=jdbcTemplate.queryForList(sql);
		return testList;
	}
	@Override
	public Long count(String user_id,PageFilter ph) {
		// TODO Auto-generated method stub
		if(!tableExistOrNot(user_id))
		{
			return 0L;
		}
		String sql="select count(*) from project_test_"+user_id;
		return costDao.countBySql(sql).longValue();
	}

	@Override
	public void delete(String user_id,String ids) throws Exception {
		// TODO Auto-generated method stub
		jdbcTemplate.execute("delete from project_test_"+user_id+" where id in ("+ids+")");
	}

	@Override
	public void add(String user_id, ProjectTest info) throws Exception {
		// TODO Auto-generated method stub
		boolean isExist=tableExistOrNot(user_id);
		if(!isExist)
		{
			createTable(user_id);
		}
		jdbcTemplate.execute("insert into project_test_"+user_id+"(project_code,t_invoice_amt,t_pay_amt) values ('"+info.getProject_code()+"',"+info.getT_invoice_amt()+","+info.getT_pay_amt()+")");
	}

	@Override
	public void edit(String user_id, ProjectTest info) throws Exception {
		// TODO Auto-generated method stub
		jdbcTemplate.execute("update project_test_"+user_id+" set t_invoice_amt = "+info.getT_invoice_amt()+",t_pay_amt = "+info.getT_pay_amt()+",project_code ='"+info.getProject_code()+"',is_pass=null where id = "+info.getId());
	}

	private String checkString(String value)
	{
		if(StringUtils.hasText(value))
		{
			return value;
		}
		return "";
	}
	@Override
	public Map<String,Object> startTest(ProjectTest info,String user_id,BigDecimal deviation) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("###### 测试开始："+sdf.format(new Date()));
		List<String> sqlList=new ArrayList<String>();
		String sql="select * from project_test_"+user_id;
		List<ProjectTest> projectList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<ProjectTest>(ProjectTest.class));
		if(projectList!=null&&projectList.size()>0)
		{
			BigDecimal invoice_amt_i_tax=new BigDecimal(0);
			BigDecimal pay_amt_i_tax=new BigDecimal(0);
			for(ProjectTest project:projectList)
			{
				invoice_amt_i_tax=invoice_amt_i_tax.add(project.getT_invoice_amt());
				pay_amt_i_tax=pay_amt_i_tax.add(project.getT_pay_amt());
				project.setSupplier_name(info.getSupplier_name());
				project.setTax_rate(info.getTax_rate());
				project.setAccount_name(info.getAccount_name());
				project.setOrg_id(info.getOrg_id());
				InitToProjectTest(project);
				BigDecimal c_account_amt=project.getC_account_amt()==null?new BigDecimal(0):project.getC_account_amt();
				BigDecimal c_pay_amt=project.getC_pay_amt()==null?new BigDecimal(0):project.getC_pay_amt();
				BigDecimal n_pay_amt=project.getN_pay_amt()==null?new BigDecimal(0):project.getN_pay_amt();
				BigDecimal t_pay_amt=project.getT_pay_amt()==null?new BigDecimal(0):project.getT_pay_amt();
				BigDecimal c_invoice_amt=project.getC_invoice_amt()==null?new BigDecimal(0):project.getC_invoice_amt();
				//BigDecimal result=c_account_amt.multiply(info.getTax_rate().add(new BigDecimal(1))).subtract(c_pay_amt).subtract(n_pay_amt);
				BigDecimal result=(c_account_amt.subtract(c_pay_amt).subtract(n_pay_amt)).multiply(info.getTax_rate().add(new BigDecimal(1))).subtract(t_pay_amt).setScale(2, BigDecimal.ROUND_HALF_UP);
				String pay_rate="";
				if(c_account_amt.compareTo(BigDecimal.ZERO)==0)
				{
					pay_rate="错误";
				}
				else
				{
					pay_rate=t_pay_amt.divide(c_account_amt.multiply(info.getTax_rate().add(new BigDecimal(1))),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue()+"%";
				}
				sql="update project_test_"+user_id+" set project_code='"+checkString(project.getProject_code())+"',address_name='"+checkString(project.getAddress_name())+"',"
					+ "supplier_name='"+checkString(project.getSupplier_name())+"',account_name='"+checkString(project.getAccount_name())+"',c_account_amt="+c_account_amt+",c_invoice_amt="+c_invoice_amt+","
					+ "c_pay_amt="+c_pay_amt+",n_pay_amt="+n_pay_amt+",";
				/*if("无效项目".equals(project.getIs_pass()))
				{
					sql+="is_pass = '"+project.getIs_pass()+"',";
				}
				else
				{*/
					if(result.add(deviation==null?new BigDecimal(0):deviation).compareTo(new BigDecimal(0))>=0)
					{

						sql+="is_pass = '通过',";
					}
					else
					{
						sql+="is_pass = "+result+",";
					}
				//}
				sql+="pay_rate='"+pay_rate+"',build_mode='"+checkString(project.getBuild_mode())+"',accept_date='"+checkString(project.getAccept_date())+"',"
						+ "deliver_date='"+checkString(project.getDeliver_date())+"',project_status='"+checkString(project.getProject_status())+"',"
						+ "rent_status='"+checkString(project.getRent_status())+"',is_cancel='"+checkString(project.getIs_cancel())+"',"
						+ "is_old='"+checkString(project.getIs_old())+"',tax_rate="+info.getTax_rate()+" where id = "+project.getId();
				sqlList.add(sql);
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			map.put("invoice_amt_i_tax", invoice_amt_i_tax);
			map.put("pay_amt_i_tax", pay_amt_i_tax);
			map.put("invoice_amt_i_tax_txt", df.format(invoice_amt_i_tax));
			map.put("pay_amt_i_tax_txt", df.format(pay_amt_i_tax));
			BigDecimal invoice_amt_e_tax=invoice_amt_i_tax.divide(new BigDecimal(1).add(info.getTax_rate()),BigDecimal.ROUND_HALF_UP);
			BigDecimal pay_amt_e_tax=pay_amt_i_tax.divide(new BigDecimal(1).add(info.getTax_rate()),BigDecimal.ROUND_HALF_UP);
			map.put("invoice_amt_e_tax", invoice_amt_e_tax);
			map.put("pay_amt_e_tax", pay_amt_e_tax);
			map.put("invoice_amt_e_tax_txt", df.format(invoice_amt_e_tax));
			map.put("pay_amt_e_tax_txt", df.format(pay_amt_e_tax));
		}
		jdbcTemplate.batchUpdate(sqlList.toArray(new String[sqlList.size()]));
		System.out.println("###### 测试结束："+sdf.format(new Date()));
		return map;
	}

	@Override
	public void confirmSign(BigDecimal invoice_amt_e_tax,BigDecimal pay_amt_e_tax,String user_id,String org_id) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String batch_no=org_id+sdf.format(new Date());
		System.out.println("###### 批次号："+batch_no);
		String sql="select * from project_test_"+user_id;
	    List<ProjectTest> testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<ProjectTest>(ProjectTest.class));
	    if(testList!=null&&testList.size()>0)
	    {
	    	String overview="insert into t_over_view(project_code,supplier_name,account_name,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid) values ";
	    	StringBuffer buff=new StringBuffer();
    		BigDecimal all_invoice_amt_e_tax=new BigDecimal(0);
    		BigDecimal all_pay_amt_e_tax=new BigDecimal(0);
    		//供应商调整表
			List<Map<String,Object>> adjList=jdbcTemplate.queryForList("select s_supplier_name,t_supplier_name from t_supplier_adj where org_id = '"+org_id+"'");
    		//处理项目测试前n-1条记录
    		for(int j=0;j<testList.size()-1;j++)
    		{
    			ProjectTest test=testList.get(j);
		    	String supplier_name="";
				String supplierQuery="select supplier_name from t_supplier where unique_name = (select unique_name from t_supplier where supplier_name = '"+test.getSupplier_name()+"' and org_id = '"+org_id+"') and org_id = '"+org_id+"'";
				List<Map<String,Object>> supplierList=jdbcTemplate.queryForList(supplierQuery);
				for(Map<String,Object> supplier:supplierList)
				{
					supplier_name+=",'"+supplier.get("supplier_name")+"'";
				}
				supplier_name=supplier_name.substring(1);
		    	String[] account_names=test.getAccount_name().split(",");
				String account_name="";
				for(String temp:account_names)
				{
					account_name+=",'"+temp+"'";
				}
				account_name=account_name.substring(1);
		    	//List<TCost> costList=costDao.find("from TCost t where t.project_code ='"+test.getProject_code()+"' and t.supplier_name = '"+test.getSupplier_name()+"' and t.account_name in ("+account_name+")");
		    	sql="select t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,t.address_code,t.address_name,"
		    			+ "(case when t.account_amt is null then 0 else t.account_amt end) as account_amt from t_cost t "
		    			+ "where t.project_code='"+test.getProject_code()+"' and t.supplier_name in ("+supplier_name+") and t.account_name in ("+account_name+") and org_id = '"+org_id+"'";
		    	List<TCost> costList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<TCost>(TCost.class));
		    	if(costList!=null&&costList.size()>0)
		    	{
		    		BigDecimal all_account_amt=new BigDecimal(0);
		    		BigDecimal all_invoice_amt=new BigDecimal(0);
		    		BigDecimal all_pay_amt=new BigDecimal(0);
		    		for(TCost cost:costList)
		    		{
		    			all_account_amt=all_account_amt.add(cost.getAccount_amt());
		    		}
		    		
		    		if(all_account_amt.compareTo(new BigDecimal(0))==0)
		    		{
		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		    			TCost cost=costList.get(0);
		    			TSignature sign=new TSignature();
		    			sign.setBatch_no(batch_no);
		    			sign.setProject_code(cost.getProject_code());
		    			sign.setOrder_code(cost.getOrder_code());
		    			sign.setAccount_name(cost.getAccount_name());
		    			sign.setAddress_code(cost.getAddress_code());
		    			sign.setAddress_name(cost.getAddress_name());
		    			//BigDecimal invoice_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
		    			sign.setInvoice_amt(test.getT_invoice_amt());
		    			BigDecimal new_invoice_amt_e_tax=test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
		    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
		    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
		    			//BigDecimal pay_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_pay_amt());
		    			sign.setPay_amt(test.getT_pay_amt());
		    			BigDecimal new_pay_amt_e_tax=test.getT_pay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
		    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
		    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
		    			sign.setTax_rate(test.getTax_rate());
		    			sign.setUpdate_by(user_id);
		    			sign.setUpdate_time(new Date());
		    			sign.setOrg_id(org_id);
			    		sign.setSupplier_code(cost.getSupplier_code());
	 		    		String supplier_temp=cost.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_temp))
							{
								supplier_temp=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_temp);
		    			sign.setUuid(uuid);
		    			signatureDao.save(sign);
		    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
		    		}
		    		else
		    		{
		    			//BigDecimal invoice_amt_e_tax=test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),BigDecimal.ROUND_HALF_UP);//本次开票净额
		    			//BigDecimal pay_amt_e_tax=test.getT_pay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),BigDecimal.ROUND_HALF_UP);//本次付款净额（根据Excel数据来计算的）
		    			for(int i=0;i<costList.size()-1;i++)
			    		{
		    				String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			    			TCost cost=costList.get(i);
			    			TSignature sign=new TSignature();
			    			sign.setBatch_no(batch_no);
			    			sign.setProject_code(cost.getProject_code());
			    			sign.setSupplier_code(cost.getSupplier_code());
			    			sign.setSupplier_name(cost.getSupplier_name());
			    			sign.setOrder_code(cost.getOrder_code());
			    			sign.setAccount_name(cost.getAccount_name());
			    			sign.setAddress_code(cost.getAddress_code());
			    			sign.setAddress_name(cost.getAddress_name());
	 		    			BigDecimal invoice_amt=cost.getAccount_amt().multiply(test.getT_invoice_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP);
	 		    			sign.setInvoice_amt(invoice_amt);
	 		    			BigDecimal new_invoice_amt_e_tax=invoice_amt.divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 		    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	 		    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	 		    			all_invoice_amt=all_invoice_amt.add(invoice_amt);
	 		    			BigDecimal pay_amt=cost.getAccount_amt().multiply(test.getT_pay_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP);
	 		    			sign.setPay_amt(pay_amt);
	 		    			BigDecimal new_pay_amt_e_tax=pay_amt.divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 		    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	 		    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	 		    			all_pay_amt=all_pay_amt.add(pay_amt);
			    			sign.setTax_rate(test.getTax_rate());
			    			sign.setUpdate_by(user_id);
			    			sign.setUpdate_time(new Date());
			    			sign.setOrg_id(org_id);
				    		sign.setSupplier_code(cost.getSupplier_code());
		 		    		String supplier_temp=cost.getSupplier_name();
		 		    		for(Map<String,Object> map:adjList)
							{
								if(map.get("s_supplier_name").equals(supplier_temp))
								{
									supplier_temp=map.get("t_supplier_name").toString();
									break;
								}
							}
		 		    		sign.setSupplier_name(supplier_temp);
			    			sign.setUuid(uuid);
			    			signatureDao.save(sign);
			    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
			    		}
			    		
		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			    		TCost cost=costList.get(costList.size()-1);
		    			TSignature sign=new TSignature();
		    			sign.setBatch_no(batch_no);
		    			sign.setProject_code(cost.getProject_code());
		    			sign.setSupplier_code(cost.getSupplier_code());
		    			sign.setSupplier_name(cost.getSupplier_name());
		    			sign.setOrder_code(cost.getOrder_code());
		    			sign.setAccount_name(cost.getAccount_name());
		    			sign.setAddress_code(cost.getAddress_code());
		    			sign.setAddress_name(cost.getAddress_name());
	 	    			sign.setInvoice_amt(test.getT_invoice_amt().subtract(all_invoice_amt));
	 	    			sign.setRemark("第一次尾差处理，开票含税："+sign.getInvoice_amt().subtract(cost.getAccount_amt().multiply(test.getT_invoice_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP)));
	 	    			BigDecimal new_invoice_amt_e_tax=sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 	    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	 	    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	 	    			sign.setPay_amt(test.getT_pay_amt().subtract(all_pay_amt));
	 	    			BigDecimal new_pay_amt_e_tax=sign.getPay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 	    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	 	    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
		    			sign.setTax_rate(test.getTax_rate());
		    			sign.setUpdate_by(user_id);
		    			sign.setUpdate_time(new Date());
		    			sign.setOrg_id(org_id);
			    		sign.setSupplier_code(cost.getSupplier_code());
	 		    		String supplier_temp=cost.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_temp))
							{
								supplier_temp=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_temp);
		    			sign.setUuid(uuid);
		    			signatureDao.save(sign);
		    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
		    		}
		    	}
		    	else
		    	{
		    		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	    			TSignature sign=new TSignature();
	    			sign.setBatch_no(batch_no);
	    			sign.setProject_code(test.getProject_code());
	    			sign.setAccount_name(test.getAccount_name());
	    			//BigDecimal invoice_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
	    			sign.setInvoice_amt(test.getT_invoice_amt());
 	    			BigDecimal new_invoice_amt_e_tax=sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
 	    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	    			sign.setRemark("成本单中不存在该项目编号数据");
	    			//BigDecimal pay_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_pay_amt());
	    			sign.setPay_amt(test.getT_pay_amt());
	    			BigDecimal new_pay_amt_e_tax=sign.getPay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
 	    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	    			sign.setTax_rate(test.getTax_rate());
	    			sign.setUpdate_by(user_id);
	    			sign.setUpdate_time(new Date());
	    			sign.setOrg_id(org_id);
			        sign.setSupplier_name(test.getSupplier_name());
	    			sign.setUuid(uuid);
	    			signatureDao.save(sign);
	    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
		    	}
    		}
    		
    		//在项目测试第n条记录处理本次文件的尾差

			ProjectTest test=testList.get(testList.size()-1);
	    	String supplier_name="";
			String supplierQuery="select supplier_name from t_supplier where unique_name = (select unique_name from t_supplier where supplier_name = '"+test.getSupplier_name()+"' and org_id = '"+org_id+"') and org_id = '"+org_id+"'";
			List<Map<String,Object>> supplierList=jdbcTemplate.queryForList(supplierQuery);
			for(Map<String,Object> supplier:supplierList)
			{
				supplier_name+=",'"+supplier.get("supplier_name")+"'";
			}
			supplier_name=supplier_name.substring(1);
	    	String[] account_names=test.getAccount_name().split(",");
			String account_name="";
			for(String temp:account_names)
			{
				account_name+=",'"+temp+"'";
			}
			account_name=account_name.substring(1);
	    	//List<TCost> costList=costDao.find("from TCost t where t.project_code ='"+test.getProject_code()+"' and t.supplier_name = '"+test.getSupplier_name()+"' and t.account_name in ("+account_name+")");
	    	sql="select t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,t.address_code,t.address_name,"
	    			+ "(case when t.account_amt is null then 0 else t.account_amt end) as account_amt from t_cost t "
	    			+ "where t.project_code='"+test.getProject_code()+"' and t.supplier_name in ("+supplier_name+") and t.account_name in ("+account_name+") and t.org_id = '"+org_id+"'";
	    	List<TCost> costList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<TCost>(TCost.class));
	    	if(costList!=null&&costList.size()>0)
	    	{
	    		BigDecimal all_account_amt=new BigDecimal(0);
	    		BigDecimal all_invoice_amt=new BigDecimal(0);
	    		BigDecimal all_pay_amt=new BigDecimal(0);
	    		for(TCost cost:costList)
	    		{
	    			all_account_amt=all_account_amt.add(cost.getAccount_amt());
	    		}
	    		
	    		if(all_account_amt.compareTo(new BigDecimal(0))==0)
	    		{
	    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	    			TCost cost=costList.get(0);
	    			TSignature sign=new TSignature();
	    			sign.setBatch_no(batch_no);
	    			sign.setProject_code(cost.getProject_code());
	    			sign.setOrder_code(cost.getOrder_code());
	    			sign.setAccount_name(cost.getAccount_name());
	    			sign.setAddress_code(cost.getAddress_code());
	    			sign.setAddress_name(cost.getAddress_name());
	    			//BigDecimal invoice_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
	    			sign.setInvoice_amt(test.getT_invoice_amt());
	    			//BigDecimal new_invoice_amt_e_tax=test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	    			//all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	    			BigDecimal new_invoice_amt_e_tax=invoice_amt_e_tax.subtract(all_invoice_amt_e_tax);
	    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	    			sign.setRemark("第二次尾差处理，开票不含税"+new_invoice_amt_e_tax.subtract(sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)));
	    			//BigDecimal pay_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_pay_amt());
	    			sign.setPay_amt(test.getT_pay_amt());
	    			//BigDecimal new_pay_amt_e_tax=test.getT_pay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	    			//all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	    			BigDecimal new_pay_amt_e_tax=pay_amt_e_tax.subtract(all_pay_amt_e_tax);
	    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	    			sign.setTax_rate(test.getTax_rate());
	    			sign.setUpdate_by(user_id);
	    			sign.setUpdate_time(new Date());
	    			sign.setOrg_id(org_id);
		    	    sign.setSupplier_code(cost.getSupplier_code());
 		    		String supplier_temp=cost.getSupplier_name();
 		    		for(Map<String,Object> map:adjList)
					{
						if(map.get("s_supplier_name").equals(supplier_temp))
						{
							supplier_temp=map.get("t_supplier_name").toString();
							break;
						}
					}
 		    		sign.setSupplier_name(supplier_temp);
	    			sign.setUuid(uuid);
	    			signatureDao.save(sign);
	    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	    		}
	    		else
	    		{
	    			//BigDecimal invoice_amt_e_tax=test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),BigDecimal.ROUND_HALF_UP);//本次开票净额
	    			//BigDecimal pay_amt_e_tax=test.getT_pay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),BigDecimal.ROUND_HALF_UP);//本次付款净额（根据Excel数据来计算的）
	    			for(int i=0;i<costList.size()-1;i++)
		    		{
	    				String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		    			TCost cost=costList.get(i);
		    			TSignature sign=new TSignature();
		    			sign.setBatch_no(batch_no);
		    			sign.setProject_code(cost.getProject_code());
		    			sign.setSupplier_code(cost.getSupplier_code());
		    			sign.setSupplier_name(cost.getSupplier_name());
		    			sign.setOrder_code(cost.getOrder_code());
		    			sign.setAccount_name(cost.getAccount_name());
		    			sign.setAddress_code(cost.getAddress_code());
		    			sign.setAddress_name(cost.getAddress_name());
 		    			BigDecimal invoice_amt=cost.getAccount_amt().multiply(test.getT_invoice_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP);
 		    			sign.setInvoice_amt(invoice_amt);
 		    			BigDecimal new_invoice_amt_e_tax=invoice_amt.divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
 		    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
 		    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
 		    			all_invoice_amt=all_invoice_amt.add(invoice_amt);
 		    			BigDecimal pay_amt=cost.getAccount_amt().multiply(test.getT_pay_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP);
 		    			sign.setPay_amt(pay_amt);
 		    			BigDecimal new_pay_amt_e_tax=pay_amt.divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
 		    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
 		    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
 		    			all_pay_amt=all_pay_amt.add(pay_amt);
		    			sign.setTax_rate(test.getTax_rate());
		    			sign.setUpdate_by(user_id);
		    			sign.setUpdate_time(new Date());
		    			sign.setOrg_id(org_id);
			    		sign.setSupplier_code(cost.getSupplier_code());
	 		    		String supplier_temp=cost.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_temp))
							{
								supplier_temp=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_temp);
		    			sign.setUuid(uuid);
		    			signatureDao.save(sign);
		    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
		    		}
		    		
	    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		    		TCost cost=costList.get(costList.size()-1);
	    			TSignature sign=new TSignature();
	    			sign.setBatch_no(batch_no);
	    			sign.setProject_code(cost.getProject_code());
	    			sign.setSupplier_code(cost.getSupplier_code());
	    			sign.setSupplier_name(cost.getSupplier_name());
	    			sign.setOrder_code(cost.getOrder_code());
	    			sign.setAccount_name(cost.getAccount_name());
	    			sign.setAddress_code(cost.getAddress_code());
	    			sign.setAddress_name(cost.getAddress_name());
 	    			sign.setInvoice_amt(test.getT_invoice_amt().subtract(all_invoice_amt));
 	    			//sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)
 	    			BigDecimal new_invoice_amt_e_tax=invoice_amt_e_tax.subtract(all_invoice_amt_e_tax);
 	    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
 	    			sign.setRemark("第二次尾差处理，开票含税："+sign.getInvoice_amt().subtract(cost.getAccount_amt().multiply(test.getT_invoice_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP))+"  开票不含税："+new_invoice_amt_e_tax.subtract(sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)));
 	    			sign.setPay_amt(test.getT_pay_amt().subtract(all_pay_amt));
 	    			//sign.getPay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)
 	    			BigDecimal new_pay_amt_e_tax=pay_amt_e_tax.subtract(all_pay_amt_e_tax);
 	    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	    			sign.setTax_rate(test.getTax_rate());
	    			sign.setUpdate_by(user_id);
	    			sign.setUpdate_time(new Date());
	    			sign.setOrg_id(org_id);
		    		sign.setSupplier_code(cost.getSupplier_code());
 		    		String supplier_temp=cost.getSupplier_name();
 		    		for(Map<String,Object> map:adjList)
					{
						if(map.get("s_supplier_name").equals(supplier_temp))
						{
							supplier_temp=map.get("t_supplier_name").toString();
							break;
						}
					}
 		    		sign.setSupplier_name(supplier_temp);
	    			sign.setUuid(uuid);
	    			signatureDao.save(sign);
	    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	    		}
	    	}
	    	else
	    	{
	    		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
    			TSignature sign=new TSignature();
    			sign.setBatch_no(batch_no);
    			sign.setProject_code(test.getProject_code());
    			sign.setAccount_name(test.getAccount_name());
    			//BigDecimal invoice_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
    			sign.setInvoice_amt(test.getT_invoice_amt());
    			BigDecimal new_invoice_amt_e_tax=invoice_amt_e_tax.subtract(all_invoice_amt_e_tax);
    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
    			sign.setRemark("成本单中不存在该项目编号数据");
    			//BigDecimal pay_amt=cost.getAccount_amt().divide(all_account_amt,2).multiply(test.getT_pay_amt());
    			sign.setPay_amt(test.getT_pay_amt());
    			BigDecimal new_pay_amt_e_tax=pay_amt_e_tax.subtract(all_pay_amt_e_tax);
    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
    			sign.setTax_rate(test.getTax_rate());
    			sign.setUpdate_by(user_id);
    			sign.setUpdate_time(new Date());
    			sign.setOrg_id(org_id);
		        sign.setSupplier_name(test.getSupplier_name());
    			sign.setUuid(uuid);
    			signatureDao.save(sign);
    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	    	}
	    	
	    	if(buff.length()>0)
	    	{
	    		jdbcTemplate.execute(overview+buff.toString().substring(0, buff.length()-1));
	    	}
	    }
	   
		jdbcTemplate.execute("drop table project_test_"+user_id);
	}

	@Override
	public Map<String, Object> getNopassCount(String user_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		/*String sql="select count(*) from project_test_"+user_id+" where is_pass != '通过'";
		int count=costDao.countBySql(sql).intValue();
		map.put("not_pass", count);
		sql="select count(*) from project_test_"+user_id+" where is_pass = '通过'";
		count=costDao.countBySql(sql).intValue();
		map.put("is_pass", count);
		sql="select count(*) from project_test_"+user_id+" where is_pass = '' or is_pass is null";
		count=costDao.countBySql(sql).intValue();
		map.put("no_test", count);*/
		String sql="select sum(case when is_pass <> '通过' then 1 else 0 end) as not_pass,sum(case when is_pass = '通过' then 1 else 0 end) as is_pass,"
				+ "sum(case when is_cancel = '已销项' then 1 else 0 end) as is_cancel,sum(case when is_pass = '' or is_pass is null then 1 else 0 end) as no_test from project_test_"+user_id;
		map=jdbcTemplate.queryForMap(sql);
		return map;
	}

	@Override
	public List<Map<String,Object>> getAccountBySupplier(String supplier_name,String user_id,String org_id) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
		if(tableExistOrNot(user_id))
		{
			/*String supplier="";
			String supplierSql="select supplier_name from t_supplier where unique_name = (select unique_name from t_supplier where supplier_name='"+supplier_name+"' and org_id = '"+org_id+"') and org_id = '"+org_id+"'";
			List<Map<String,Object>> supplierList=jdbcTemplate.queryForList(supplierSql);
			if(supplierList!=null&&supplierList.size()>0)
			{
				for(Map<String,Object> map:supplierList)
				{
					supplier+=",'"+map.get("supplier_name")+"'";
				}
			}*/
			String sql="select distinct account_name from t_cost t where t.supplier_name ='"+supplier_name+"' and exists (select 1 from project_test_"+user_id+" p where p.project_code = t.project_code) and org_id = '"+org_id+"'";
			mapList=jdbcTemplate.queryForList(sql);
		}
		return mapList;
	}

	@Override
	public void deleteOrderTest(String user_id) throws Exception {
		// TODO Auto-generated method stub
		if(tableExistOrNot(user_id))
		{
			jdbcTemplate.execute("drop table project_test_"+user_id);
		}
	}

}
