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
import com.mvc.model.sys.TGodownEntry;
import com.mvc.model.sys.TOldProj;
import com.mvc.model.sys.TPayment;
import com.mvc.model.sys.TPms;
import com.mvc.model.sys.TSignature;
import com.mvc.model.sys.TTableExcel;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.OrderTest;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.test.OrderTestServiceI;
import com.mvc.utils.ExcelReaderUtil;
import com.mvc.utils.ReadFileUtil;


@Service
public class OrderTestServiceImpl implements OrderTestServiceI{
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
	private BaseDaoI<TGodownEntry> entryDao;
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
				jdbcTemplate.execute("delete from order_test_"+info.getUserId());
			}
			List<String> sqlList=new ArrayList<String>();
			List<TTableExcel> columns=tableDao.find("from TTableExcel t where t.table_name ='order_test'");
			if(columns!=null&&columns.size()>0)
			{
				for(Object[] object:allList)
				{
					String sql="insert into order_test_"+info.getUserId()+" (";
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
				List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select distinct order_code,sum(t_invoice_amt) as t_invoice_amt,sum(t_pay_amt) as t_pay_amt from order_test_"+info.getUserId()+" group by order_code");
				if(mapList!=null&&mapList.size()>0)
				{
					jdbcTemplate.execute("delete from order_test_"+info.getUserId());
					StringBuffer sqlBuff=new StringBuffer("insert into order_test_"+info.getUserId()+"(order_code,t_invoice_amt,t_pay_amt) values ");
					for(Map<String,Object> m:mapList)
					{
						sqlBuff.append("('"+m.get("order_code")+"',"+new BigDecimal(m.get("t_invoice_amt").toString())+","+new BigDecimal(m.get("t_pay_amt").toString())+"),");
					}
					jdbcTemplate.execute(sqlBuff.substring(0, sqlBuff.length()-1));
				}
			}
			else
			{
				throw new Exception("未配置订单测试表字段与Excel文件列的对应关系！");
			}
		}
		else
		{
			throw new Exception("请上传非空文件！");
		}
	}
	
	private void InitToOrderTest(OrderTest order) throws Exception
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("###### 订单测试格式化开始："+sdf.format(new Date()));
		String sql="select a.order_code,a.project_code,a.address_name,a.supplier_name,"
				+ "(case when a.c_account_amt is null then 0 else a.c_account_amt end) as c_account_amt,"
				+ "(case when b.c_invoice_amt is null then 0 else b.c_invoice_amt end) as c_invoice_amt,"
				+ "(case when b.c_pay_amt is null then 0 else b.c_pay_amt end) as c_pay_amt,"
				+ "(case when c.n_pay_amt is null then 0 else c.n_pay_amt end) as n_pay_amt "
				+ "from (select t.order_code,t.project_code,t.address_name,t.supplier_name,sum(t.account_amt) as c_account_amt from t_cost t where t.order_code ='"+order.getOrder_code()+"' and t.org_id = '"+order.getOrg_id()+"' group by t.order_code) a "
				+ "left join (select p.order_code,sum(p.invoice_amt_e_tax) as c_invoice_amt,sum(p.pay_amt_e_tax) as c_pay_amt from t_payment p where p.order_code ='"+order.getOrder_code()+"' and p.org_id = '"+order.getOrg_id()+"' group by p.order_code) b on b.order_code=a.order_code "
				+ "left join (select s.order_code,sum(s.pay_amt_e_tax) as n_pay_amt from t_signature s where s.order_code ='"+order.getOrder_code()+"' and s.org_id = '"+order.getOrg_id()+"' group by s.order_code) c  on c.order_code=a.order_code";
		List<OrderTest> testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<OrderTest>(OrderTest.class));
		if(testList!=null&&testList.size()>0)
		{
			OrderTest test=testList.get(0);
			order.setProject_code(test.getProject_code());
			order.setAddress_name(test.getAddress_name());
			order.setSupplier_name(test.getSupplier_name());
			order.setC_account_amt(test.getC_account_amt());
			order.setC_invoice_amt(test.getC_invoice_amt());
			order.setC_pay_amt(test.getC_pay_amt());
			order.setN_pay_amt(test.getN_pay_amt());
		}
		else
		{
			sql="select a.order_code,"
					+ "(case when a.c_account_amt is null then 0 else a.c_account_amt end) as c_account_amt,"
					+ "(case when b.c_invoice_amt is null then 0 else b.c_invoice_amt end) as c_invoice_amt,"
					+ "(case when b.c_pay_amt is null then 0 else b.c_pay_amt end) as c_pay_amt,"
					+ "(case when c.n_pay_amt is null then 0 else c.n_pay_amt end) as n_pay_amt "
					+ "from (select t.order_code,sum(t.amount) as c_account_amt from t_godown_entry t where t.order_code ='"+order.getOrder_code()+"' and t.org_id = '"+order.getOrg_id()+"' group by t.order_code) a "
					+ "left join (select p.order_code,sum(p.invoice_amt_e_tax) as c_invoice_amt,sum(p.pay_amt_e_tax) as c_pay_amt from t_payment p where p.order_code ='"+order.getOrder_code()+"' and p.org_id = '"+order.getOrg_id()+"' group by p.order_code) b on b.order_code=a.order_code "
					+ "left join (select s.order_code,sum(s.pay_amt_e_tax) as n_pay_amt from t_signature s where s.order_code ='"+order.getOrder_code()+"' and s.org_id = '"+order.getOrg_id()+"' group by s.order_code) c  on c.order_code=a.order_code";
			testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<OrderTest>(OrderTest.class));
			if(testList!=null&&testList.size()>0)
			{
				OrderTest test=testList.get(0);
				//order.setProject_code("物资采购订单");
				order.setC_account_amt(test.getC_account_amt());
				order.setC_invoice_amt(test.getC_invoice_amt());
				order.setC_pay_amt(test.getC_pay_amt());
				order.setN_pay_amt(test.getN_pay_amt());
				sql="select t.project_code,s.unique_name as supplier_name from t_store_deal t left join t_supplier s on s.supplier_name = t.supplier_name where t.order_code = '"+order.getOrder_code()+"' and t.org_id = '"+order.getOrg_id()+"' and s.org_id = '"+order.getOrg_id()+"'";
				List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
				if(mapList!=null&&mapList.size()>0)
				{
					Map<String,Object> map=mapList.get(0);
					order.setProject_code(map.get("project_code").toString());
					order.setSupplier_name(map.get("supplier_name").toString());
				}
			}
			else
			{
				//throw new Exception("订单号："+order.getOrder_code()+"相关数据不存在！");
				order.setIs_pass("无效订单");
			}
		}
		System.out.println("###### 订单测试金额数据格式化开始："+sdf.format(new Date()));
		if(StringUtils.hasText(order.getProject_code()))
		{
			List<TPms> pmsList=pmsDao.find("from TPms t where t.project_code ='"+order.getProject_code()+"' and t.org_id = '"+order.getOrg_id()+"'");
			if(pmsList!=null&&pmsList.size()>0)
			{
				TPms pms=pmsList.get(0);
				order.setBuild_mode(pms.getBuild_mode());
				order.setAccept_date(pms.getAccept_date());
				order.setDeliver_date(pms.getDeliver_date());
				order.setProject_status(pms.getProject_status());
			}
			
			List<TCancelProj> cancelList=cancelProjDao.find("from TCancelProj t where t.project_code = '"+order.getProject_code()+"' and t.org_id = '"+order.getOrg_id()+"'");
			if(cancelList!=null&&cancelList.size()>0)
			{
				order.setIs_cancel("已销项");
			}
			
			List<TOldProj> oldList=oldProjDao.find("from TOldProj t where t.project_code = '"+order.getProject_code()+"' and t.org_id = '"+order.getOrg_id()+"'");
			if(oldList!=null&&oldList.size()>0)
			{
				order.setIs_old("旧项目");
			}
			else
			{
				order.setIs_old("新项目");
			}
		}
		
		List<TCrm> crmList=crmDao.find("select t from TCrm t,TPms p where t.order_code = p.order_code and p.project_code = '"+order.getProject_code()+"' and t.org_id = '"+order.getOrg_id()+"' and p.org_id = '"+order.getOrg_id()+"'");
		if(crmList!=null&&crmList.size()>0)
		{
			TCrm crm=crmList.get(0);
			order.setRent_status(crm.getRent_status());
		}
		System.out.println("###### 订单测试格式化结束："+sdf.format(new Date()));
	}
	
	private boolean tableExistOrNot(String user_id)
	{
		boolean isExist=false;
		String sql="";
		try
		{
			/*sql="select count(*) from order_test_"+user_id;
			jdbcTemplate.execute(sql);
			isExist=true;*/
			List<Map<String,Object>> tableList=jdbcTemplate.queryForList("select TABLE_NAME from information_schema.TABLES where table_schema ='fms' and table_name = 'order_test_"+user_id+"'");
			if(tableList!=null&&tableList.size()>0)
			{
				isExist=true;
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		return isExist;
	}
	
	private void createTable(String user_id)
	{
         String sql="create table order_test_"+user_id+" (id int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',order_code varchar(50) DEFAULT NULL COMMENT '采购订单号',"
					+ "project_code varchar(30) DEFAULT NULL COMMENT '项目编码',address_name varchar(100) DEFAULT NULL COMMENT '站址名称',"
					+ "supplier_name varchar(100) DEFAULT NULL COMMENT '供应商名称',c_account_amt decimal(11,2) DEFAULT NULL COMMENT '已入账金额',"
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
		String sql="select id,order_code,project_code,address_name,supplier_name,format(c_account_amt,2) as c_account_amt,"
				+ "format(c_invoice_amt,2) as c_invoice_amt,format(c_pay_amt,2) as c_pay_amt,format(n_pay_amt,2) as n_pay_amt,"
				+ "t_invoice_amt,format(t_invoice_amt,2) as t_invoice_amt_txt,t_pay_amt,format(t_pay_amt,2) as t_pay_amt_txt,(case when (is_pass = '通过' or is_pass = '无效订单') then "
				+ "is_pass else format(is_pass,2) end) as is_pass,pay_rate,build_mode,accept_date,"
				+ "deliver_date,project_status,rent_status,is_cancel,is_old from order_test_"+user_id;
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
		String sql="select count(*) from order_test_"+user_id;
		return costDao.countBySql(sql).longValue();
	}

	@Override
	public void delete(String user_id,String ids) throws Exception {
		// TODO Auto-generated method stub
		jdbcTemplate.execute("delete from order_test_"+user_id+" where id in ("+ids+")");
	}

	@Override
	public void add(String user_id, OrderTest info) throws Exception {
		// TODO Auto-generated method stub
		boolean isExist=tableExistOrNot(user_id);
		if(!isExist)
		{
			createTable(user_id);
		}
		jdbcTemplate.execute("insert into order_test_"+user_id+"(order_code,t_invoice_amt,t_pay_amt) values ('"+info.getOrder_code()+"',"+info.getT_invoice_amt()+","+info.getT_pay_amt()+")");
	}

	@Override
	public void edit(String user_id, OrderTest info) throws Exception {
		// TODO Auto-generated method stub
		jdbcTemplate.execute("update order_test_"+user_id+" set t_invoice_amt = "+info.getT_invoice_amt()+",t_pay_amt = "+info.getT_pay_amt()+",order_code ='"+info.getOrder_code()+"',is_pass=null where id = "+info.getId());
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
	public Map<String,Object> startTest(String user_id,String org_id,BigDecimal tax_rate,BigDecimal deviation) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyyy-MM-dd HH:mm:ss");
		System.out.println("###### 测试开始："+sdf.format(new Date()));
		List<String> sqlList=new ArrayList<String>();
		String sql="select * from order_test_"+user_id;
		List<OrderTest> testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<OrderTest>(OrderTest.class));
		if(testList!=null&&testList.size()>0)
		{
			BigDecimal invoice_amt_i_tax=new BigDecimal(0);
			BigDecimal pay_amt_i_tax=new BigDecimal(0);
			for(OrderTest test:testList)
			{
				invoice_amt_i_tax=invoice_amt_i_tax.add(test.getT_invoice_amt());
				pay_amt_i_tax=pay_amt_i_tax.add(test.getT_pay_amt());
				test.setOrg_id(org_id);
				InitToOrderTest(test);
				BigDecimal c_account_amt=test.getC_account_amt()==null?new BigDecimal(0):test.getC_account_amt();
				BigDecimal c_pay_amt=test.getC_pay_amt()==null?new BigDecimal(0):test.getC_pay_amt();
				BigDecimal n_pay_amt=test.getN_pay_amt()==null?new BigDecimal(0):test.getN_pay_amt();
				BigDecimal t_pay_amt=test.getT_pay_amt()==null?new BigDecimal(0):test.getT_pay_amt();
				BigDecimal c_invoice_amt=test.getC_invoice_amt()==null?new BigDecimal(0):test.getC_invoice_amt();
				BigDecimal result=(c_account_amt.subtract(c_pay_amt).subtract(n_pay_amt)).multiply(tax_rate.add(new BigDecimal(1))).subtract(t_pay_amt).setScale(2, BigDecimal.ROUND_HALF_UP);
				String pay_rate="";
				if(c_account_amt.compareTo(BigDecimal.ZERO)==0)
				{
					pay_rate="错误";
				}
				else
				{
					pay_rate=t_pay_amt.divide(c_account_amt.multiply(tax_rate.add(new BigDecimal(1))),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).intValue()+"%";
				}
				sql="update order_test_"+user_id+" set project_code='"+checkString(test.getProject_code())+"',address_name='"+checkString(test.getAddress_name())+"',"
					+ "supplier_name='"+checkString(test.getSupplier_name())+"',c_account_amt="+c_account_amt+",c_invoice_amt="+c_invoice_amt+","
					+ "c_pay_amt="+c_pay_amt+",n_pay_amt="+n_pay_amt+",";
				if("无效订单".equals(test.getIs_pass()))
				{
					sql+="is_pass = '"+test.getIs_pass()+"',";
				}
				else
				{
					if(result.add(deviation==null?new BigDecimal(0):deviation).compareTo(new BigDecimal(0))>=0)
					{

						sql+="is_pass = '通过',";
					}
					else
					{
						sql+="is_pass = "+result+",";
					}
				}
				sql+="pay_rate='"+pay_rate+"',build_mode='"+checkString(test.getBuild_mode())+"',accept_date='"+checkString(test.getAccept_date())+"',"
						+ "deliver_date='"+checkString(test.getDeliver_date())+"',project_status='"+checkString(test.getProject_status())+"',"
						+ "rent_status='"+checkString(test.getRent_status())+"',is_cancel='"+checkString(test.getIs_cancel())+"',"
						+ "is_old='"+checkString(test.getIs_old())+"',tax_rate="+tax_rate+" where id = "+test.getId();
				sqlList.add(sql);
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			map.put("invoice_amt_i_tax", invoice_amt_i_tax);
			map.put("pay_amt_i_tax", pay_amt_i_tax);
			map.put("invoice_amt_i_tax_txt", df.format(invoice_amt_i_tax));
			map.put("pay_amt_i_tax_txt", df.format(pay_amt_i_tax));
			BigDecimal invoice_amt_e_tax=invoice_amt_i_tax.divide(new BigDecimal(1).add(tax_rate),BigDecimal.ROUND_HALF_UP);
			BigDecimal pay_amt_e_tax=pay_amt_i_tax.divide(new BigDecimal(1).add(tax_rate),BigDecimal.ROUND_HALF_UP);
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
		String sql="select * from order_test_"+user_id;
	    List<OrderTest> testList=jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<OrderTest>(OrderTest.class));
	    if(testList!=null&&testList.size()>0)
	    {
	    	String overview="insert into t_over_view(project_code,supplier_name,account_name,account_amt,t_pay_amt,s_pay_amt,t_invoice_amt,t_invoice_amt_e_tax,table_name,org_id,uuid) values ";
	    	StringBuffer buff=new StringBuffer();
	    	BigDecimal all_invoice_amt_e_tax=new BigDecimal(0);
	    	BigDecimal all_pay_amt_e_tax=new BigDecimal(0);
	    	//供应商调整表
			List<Map<String,Object>> adjList=jdbcTemplate.queryForList("select s_supplier_name,t_supplier_name from t_supplier_adj where org_id = '"+org_id+"'");
	    	//处理本次订单测试前n-1条记录
	    	for(int j=0;j<testList.size()-1;j++)
	 	    {
	    		OrderTest test=testList.get(j);
	 	    	//List<TCost> costList=costDao.find("from TCost t where t.order_code ='"+test.getOrder_code()+"'");
	 	    	sql="select t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,t.address_code,t.address_name,"
	 	    			+ "(case when sum(t.account_amt) is null then 0 else sum(t.account_amt) end) as account_amt from t_cost t "
	 	    			+ "where t.order_code='"+test.getOrder_code()+"' and t.org_id = '"+org_id+"' group by t.account_name";
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
	 	    			sign.setInvoice_amt(test.getT_invoice_amt());
	 	    			BigDecimal new_invoice_amt_e_tax=test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 	    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	 	    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	 	    			sign.setPay_amt(test.getT_pay_amt());
	 	    			BigDecimal new_pay_amt_e_tax=test.getT_pay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 	    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	 	    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	 	    			sign.setTax_rate(test.getTax_rate());
	 	    			sign.setUpdate_by(user_id);
	 	    			sign.setUpdate_time(new Date());
	 	    			sign.setOrg_id(org_id);
	 		    		sign.setSupplier_code(cost.getSupplier_code());
	 		    		String supplier_name=cost.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_name);
	 	    			sign.setUuid(uuid);
	 	    			signatureDao.save(sign);
	 	    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	 	    		}
	 	    		else
	 	    		{
	 	    			//根据t_cost记录的数据进行分割
	 		    		for(int i=0;i<costList.size()-1;i++)
	 		    		{
	 		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	 		    			TCost cost=costList.get(i);
	 		    			TSignature sign=new TSignature();
	 		    			sign.setBatch_no(batch_no);
	 		    			sign.setProject_code(cost.getProject_code());
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
		 		    		String supplier_name=cost.getSupplier_name();
		 		    		for(Map<String,Object> map:adjList)
							{
								if(map.get("s_supplier_name").equals(supplier_name))
								{
									supplier_name=map.get("t_supplier_name").toString();
									break;
								}
							}
		 		    		sign.setSupplier_name(supplier_name);
	 		    			sign.setUuid(uuid);
	 		    			signatureDao.save(sign);
	 		    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	 		    		}
	 		    		
	 		    		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	 		    		//处理最后一条记录
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
	 	    			sign.setRemark("第一次尾差处理,开票含税："+sign.getInvoice_amt().subtract(cost.getAccount_amt().multiply(test.getT_invoice_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP)));
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
	 		    		String supplier_name=cost.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_name);
	 	    			sign.setUuid(uuid);
	 	    			signatureDao.save(sign);
	 	    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	 	    		}
	 	    		
	 	    	}
	 	    	else
	 	    	{
	 	    		List<TGodownEntry> entryList=entryDao.find("from TGodownEntry t where t.order_code ='"+test.getOrder_code()+"' and t.org_id = '"+org_id+"'");
	 	    		if(entryList!=null&&entryList.size()>0)
	 	    		{
	 	    			BigDecimal all_account_amt=new BigDecimal(0);
	 		    		BigDecimal all_invoice_amt=new BigDecimal(0);
	 		    		BigDecimal all_pay_amt=new BigDecimal(0);
	 		    		for(TGodownEntry entry:entryList)
	 		    		{
	 		    			all_account_amt=all_account_amt.add(entry.getAmount());
	 		    		}
	 		    		
	 		    		if(all_account_amt.compareTo(new BigDecimal(0))==0)
	 		    		{
	 		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	 		    			TGodownEntry entry=entryList.get(0);
	 		    			TSignature sign=new TSignature();
	 		    			sign.setBatch_no(batch_no);
	 		    			sign.setOrder_code(entry.getOrder_code());
	 		    			sign.setProject_code(test.getProject_code());
		 		    		String supplier_name=test.getSupplier_name();
		 		    		for(Map<String,Object> map:adjList)
							{
								if(map.get("s_supplier_name").equals(supplier_name))
								{
									supplier_name=map.get("t_supplier_name").toString();
									break;
								}
							}
		 		    		sign.setSupplier_name(supplier_name);
	 		    			sign.setAccount_name("物资采购订单");
	 		    			sign.setInvoice_amt(test.getT_invoice_amt());
	 		    			BigDecimal new_invoice_amt_e_tax=test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 		    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	 		    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	 		    			sign.setPay_amt(test.getT_pay_amt());
	 		    			BigDecimal new_pay_amt_e_tax=test.getT_pay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 		    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	 		    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	 		    			sign.setTax_rate(test.getTax_rate());
	 		    			sign.setUpdate_by(user_id);
	 		    			sign.setUpdate_time(new Date());
	 		    			sign.setOrg_id(org_id);
	 		    			sign.setUuid(uuid);
	 		    			signatureDao.save(sign);
	 		    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','物资采购订单',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	 		    		}
	 		    		else
	 		    		{
	 		    			for(int i=0;i<entryList.size()-1;i++)
	 			    		{
	 		    				String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	 			    			TGodownEntry entry=entryList.get(i);
	 			    			TSignature sign=new TSignature();
	 			    			sign.setBatch_no(batch_no);
	 			    			sign.setOrder_code(entry.getOrder_code());
	 			    			sign.setProject_code(test.getProject_code());
	 		 		    		String supplier_name=test.getSupplier_name();
	 		 		    		for(Map<String,Object> map:adjList)
	 							{
	 								if(map.get("s_supplier_name").equals(supplier_name))
	 								{
	 									supplier_name=map.get("t_supplier_name").toString();
	 									break;
	 								}
	 							}
	 		 		    		sign.setSupplier_name(supplier_name);
	 			    			sign.setAccount_name("物资采购订单");
	 			    			BigDecimal invoice_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
	 			    			sign.setInvoice_amt(invoice_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
	 			    			BigDecimal new_invoice_amt_e_tax=sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 			    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	 			    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	 			    			all_invoice_amt=all_invoice_amt.add(invoice_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
	 			    			BigDecimal pay_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_pay_amt());
	 			    			sign.setPay_amt(pay_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
	 			    			BigDecimal new_pay_amt_e_tax=sign.getPay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 			    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	 			    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	 			    			all_pay_amt=all_pay_amt.add(pay_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
	 			    			sign.setTax_rate(test.getTax_rate());
	 			    			sign.setUpdate_by(user_id);
	 			    			sign.setUpdate_time(new Date());
	 			    			sign.setOrg_id(org_id);
	 			    			sign.setUuid(uuid);
	 			    			signatureDao.save(sign);
	 			    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','物资采购订单',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	 			    		}
	 			    		
	 		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
	 		    			TGodownEntry entry=entryList.get(entryList.size()-1);
	 		    			TSignature sign=new TSignature();
	 		    			sign.setBatch_no(batch_no);
	 		    			sign.setOrder_code(entry.getOrder_code());
	 		    			sign.setProject_code(test.getProject_code());
		 		    		String supplier_name=test.getSupplier_name();
		 		    		for(Map<String,Object> map:adjList)
							{
								if(map.get("s_supplier_name").equals(supplier_name))
								{
									supplier_name=map.get("t_supplier_name").toString();
									break;
								}
							}
		 		    		sign.setSupplier_name(supplier_name);
	 		    			sign.setAccount_name("物资采购订单");
	 		    			//BigDecimal invoice_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
	 		    			sign.setInvoice_amt(test.getT_invoice_amt().subtract(all_invoice_amt));
	 		    			sign.setRemark("第一次尾差处理，开票含税："+sign.getInvoice_amt().subtract(entry.getAmount().multiply(test.getT_invoice_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP)));
	 		    			BigDecimal new_invoice_amt_e_tax=sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 		    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
	 		    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
	 		    			//BigDecimal pay_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_pay_amt());
	 		    			sign.setPay_amt(test.getT_pay_amt().subtract(all_pay_amt));
	 		    			BigDecimal new_pay_amt_e_tax=sign.getPay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
	 		    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
	 		    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
	 		    			sign.setTax_rate(test.getTax_rate());
	 		    			sign.setUpdate_by(user_id);
	 		    			sign.setUpdate_time(new Date());
	 		    			sign.setOrg_id(org_id);
	 		    			sign.setUuid(uuid);
	 		    			signatureDao.save(sign);
	 		    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','物资采购订单',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
	 		    		}
	 		    		
	 	    		}
	 	    	}
	 	    }
	    	
	    	//在本次订单测试第n条记录处理本次文件的尾差
    		OrderTest test=testList.get(testList.size()-1);
 	    	//List<TCost> costList=costDao.find("from TCost t where t.order_code ='"+test.getOrder_code()+"'");
 	    	sql="select t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,t.address_code,t.address_name,"
 	    			+ "(case when sum(t.account_amt) is null then 0 else sum(t.account_amt) end) as account_amt from t_cost t "
 	    			+ "where t.order_code='"+test.getOrder_code()+"' and t.org_id = '"+org_id+"' group by t.account_name";
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
 	    			sign.setInvoice_amt(test.getT_invoice_amt());
 	    			//BigDecimal new_invoice_amt_e_tax=test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
 	    			//all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
 	    			BigDecimal new_invoice_amt_e_tax=invoice_amt_e_tax.subtract(all_invoice_amt_e_tax);
 	    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
 	    			sign.setRemark("第二次尾差处理，开票不含税："+new_invoice_amt_e_tax.subtract(sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)));
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
 		    		String supplier_name=cost.getSupplier_name();
 		    		for(Map<String,Object> map:adjList)
					{
						if(map.get("s_supplier_name").equals(supplier_name))
						{
							supplier_name=map.get("t_supplier_name").toString();
							break;
						}
					}
 		    		sign.setSupplier_name(supplier_name);
 	    			sign.setUuid(uuid);
 	    			signatureDao.save(sign);
 	    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
 	    		}
 	    		else
 	    		{
 	    			//根据t_cost记录的数据进行分割
 		    		for(int i=0;i<costList.size()-1;i++)
 		    		{
 		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
 		    			TCost cost=costList.get(i);
 		    			TSignature sign=new TSignature();
 		    			sign.setBatch_no(batch_no);
 		    			sign.setProject_code(cost.getProject_code());
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
	 		    		String supplier_name=cost.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_name);
 		    			sign.setUuid(uuid);
 		    			signatureDao.save(sign);
 		    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
 		    		}
 		    		
 		    		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
 		    		//处理最后一条记录
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
 		    		String supplier_name=cost.getSupplier_name();
 		    		for(Map<String,Object> map:adjList)
					{
						if(map.get("s_supplier_name").equals(supplier_name))
						{
							supplier_name=map.get("t_supplier_name").toString();
							break;
						}
					}
 		    		sign.setSupplier_name(supplier_name);
 	    			sign.setUuid(uuid);
 	    			signatureDao.save(sign);
 	    			buff.append("('"+cost.getProject_code()+"','"+cost.getSupplier_name()+"','"+cost.getAccount_name()+"',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
 	    		}
 	    		
 	    	}
 	    	else
 	    	{
 	    		List<TGodownEntry> entryList=entryDao.find("from TGodownEntry t where t.order_code ='"+test.getOrder_code()+"' and t.org_id = '"+org_id+"'");
 	    		if(entryList!=null&&entryList.size()>0)
 	    		{
 	    			BigDecimal all_account_amt=new BigDecimal(0);
 		    		BigDecimal all_invoice_amt=new BigDecimal(0);
 		    		BigDecimal all_pay_amt=new BigDecimal(0);
 		    		for(TGodownEntry entry:entryList)
 		    		{
 		    			all_account_amt=all_account_amt.add(entry.getAmount());
 		    		}
 		    		
 		    		if(all_account_amt.compareTo(new BigDecimal(0))==0)
 		    		{
 		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
 		    			TGodownEntry entry=entryList.get(0);
 		    			TSignature sign=new TSignature();
 		    			sign.setBatch_no(batch_no);
 		    			sign.setOrder_code(entry.getOrder_code());
 		    			sign.setProject_code(test.getProject_code());
	 		    		String supplier_name=test.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_name);
 		    			sign.setAccount_name("物资采购订单");
 		    			sign.setInvoice_amt(test.getT_invoice_amt());
 		    			//test.getT_invoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)
 		    			BigDecimal new_invoice_amt_e_tax=invoice_amt_e_tax.subtract(all_invoice_amt_e_tax);
 		    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
 		    			sign.setRemark("第二次尾差处理，开票不含税："+new_invoice_amt_e_tax.subtract(sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)));
 		    			sign.setPay_amt(test.getT_pay_amt());
 		    			//test.getT_pay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)
 		    			BigDecimal new_pay_amt_e_tax=pay_amt_e_tax.subtract(all_pay_amt_e_tax);
 		    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
 		    			sign.setTax_rate(test.getTax_rate());
 		    			sign.setUpdate_by(user_id);
 		    			sign.setUpdate_time(new Date());
 		    			sign.setOrg_id(org_id);
 		    			sign.setUuid(uuid);
 		    			signatureDao.save(sign);
 		    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','物资采购订单',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
 		    		}
 		    		else
 		    		{
 		    			for(int i=0;i<entryList.size()-1;i++)
 			    		{
 		    				String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
 			    			TGodownEntry entry=entryList.get(i);
 			    			TSignature sign=new TSignature();
 			    			sign.setBatch_no(batch_no);
 			    			sign.setOrder_code(entry.getOrder_code());
 			    			sign.setProject_code(test.getProject_code());
 		 		    		String supplier_name=test.getSupplier_name();
 		 		    		for(Map<String,Object> map:adjList)
 							{
 								if(map.get("s_supplier_name").equals(supplier_name))
 								{
 									supplier_name=map.get("t_supplier_name").toString();
 									break;
 								}
 							}
 		 		    		sign.setSupplier_name(supplier_name);
 			    			sign.setAccount_name("物资采购订单");
 			    			BigDecimal invoice_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
 			    			sign.setInvoice_amt(invoice_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
 			    			BigDecimal new_invoice_amt_e_tax=sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
 			    			all_invoice_amt_e_tax=all_invoice_amt_e_tax.add(new_invoice_amt_e_tax);
 			    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
 			    			all_invoice_amt=all_invoice_amt.add(invoice_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
 			    			BigDecimal pay_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_pay_amt());
 			    			sign.setPay_amt(pay_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
 			    			BigDecimal new_pay_amt_e_tax=sign.getPay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP);
 			    			all_pay_amt_e_tax=all_pay_amt_e_tax.add(new_pay_amt_e_tax);
 			    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
 			    			all_pay_amt=all_pay_amt.add(pay_amt.setScale(2, BigDecimal.ROUND_HALF_UP));
 			    			sign.setTax_rate(test.getTax_rate());
 			    			sign.setUpdate_by(user_id);
 			    			sign.setUpdate_time(new Date());
 			    			sign.setOrg_id(org_id);
 			    			sign.setUuid(uuid);
 			    			signatureDao.save(sign);
 			    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','物资采购订单',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
 			    		}
 			    		
 		    			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
 		    			TGodownEntry entry=entryList.get(entryList.size()-1);
 		    			TSignature sign=new TSignature();
 		    			sign.setBatch_no(batch_no);
 		    			sign.setOrder_code(entry.getOrder_code());
 		    			sign.setProject_code(test.getProject_code());
	 		    		String supplier_name=test.getSupplier_name();
	 		    		for(Map<String,Object> map:adjList)
						{
							if(map.get("s_supplier_name").equals(supplier_name))
							{
								supplier_name=map.get("t_supplier_name").toString();
								break;
							}
						}
	 		    		sign.setSupplier_name(supplier_name);
 		    			sign.setAccount_name("物资采购订单");
 		    			//BigDecimal invoice_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_invoice_amt());
 		    			sign.setInvoice_amt(test.getT_invoice_amt().subtract(all_invoice_amt));
 		    			//sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)
 		    			BigDecimal new_invoice_amt_e_tax=invoice_amt_e_tax.subtract(all_invoice_amt_e_tax);
 		    			sign.setInvoice_amt_e_tax(new_invoice_amt_e_tax);
 		    			sign.setRemark("第二次尾差处理，开票含税："+sign.getInvoice_amt().subtract(entry.getAmount().multiply(test.getT_invoice_amt()).divide(all_account_amt,2,BigDecimal.ROUND_HALF_UP))+"  开票不含税："+new_invoice_amt_e_tax.subtract(sign.getInvoice_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)));
 		    			//BigDecimal pay_amt=entry.getAmount().divide(all_account_amt,2).multiply(test.getT_pay_amt());
 		    			sign.setPay_amt(test.getT_pay_amt().subtract(all_pay_amt));
 		    			//sign.getPay_amt().divide(new BigDecimal(1).add(test.getTax_rate()),2, BigDecimal.ROUND_HALF_UP)
 		    			BigDecimal new_pay_amt_e_tax=pay_amt_e_tax.subtract(all_pay_amt_e_tax);
 		    			sign.setPay_amt_e_tax(new_pay_amt_e_tax);
 		    			sign.setTax_rate(test.getTax_rate());
 		    			sign.setUpdate_by(user_id);
 		    			sign.setUpdate_time(new Date());
 		    			sign.setOrg_id(org_id);
 		    			sign.setUuid(uuid);
 		    			signatureDao.save(sign);
 		    			buff.append("('"+test.getProject_code()+"','"+test.getSupplier_name()+"','物资采购订单',0,0,"+new_pay_amt_e_tax+","+test.getT_invoice_amt()+","+new_invoice_amt_e_tax+",'t_signature','"+org_id+"','"+uuid+"'),");
 		    		}
 		    		
 	    		}
 	    	}
 	    	if(buff.length()>0)
 	    	{
 	    		jdbcTemplate.execute(overview+buff.toString().substring(0, buff.length()-1));
 	    	}
	    }
		jdbcTemplate.execute("drop table order_test_"+user_id);
	}

	@Override
	public Map<String, Object> getNopassCount(String user_id) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<String,Object>();
		/*String sql="select count(*) from order_test_"+user_id+" where is_pass != '通过'";
		int count=costDao.countBySql(sql).intValue();
		map.put("not_pass", count);
		sql="select count(*) from order_test_"+user_id+" where is_pass = '通过'";
		count=costDao.countBySql(sql).intValue();
		map.put("is_pass", count);
		sql="select count(*) from order_test_"+user_id+" where is_pass = '无效订单'";
		count=costDao.countBySql(sql).intValue();
		map.put("invalid", count);*/
		String sql="select sum(case when is_pass <> '通过' then 1 else 0 end) as not_pass,sum(case when is_pass = '通过' then 1 else 0 end) as is_pass, sum(case when is_pass='无效订单' then 1 else 0 end) as invalid,"
				+ "sum(case when is_cancel = '已销项' then 1 else 0 end) as is_cancel,sum(case when is_pass = '' or is_pass is null then 1 else 0 end) as no_test from order_test_"+user_id;
		map=jdbcTemplate.queryForMap(sql);
		return map;
	}

	@Override
	public void deleteOrderTest(String user_id) throws Exception {
		// TODO Auto-generated method stub
		if(tableExistOrNot(user_id))
		{
			jdbcTemplate.execute("drop table order_test_"+user_id);
		}
	}

}
