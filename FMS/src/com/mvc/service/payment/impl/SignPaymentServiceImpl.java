package com.mvc.service.payment.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TPayment;
import com.mvc.model.sys.TSignature;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.SignInfo;
import com.mvc.service.payment.SignPaymentServiceI;

@Service
public class SignPaymentServiceImpl implements SignPaymentServiceI{
	@Autowired
	private BaseDaoI<TSignature> signatureDao;
	@Autowired
	private BaseDaoI<TPayment> paymentDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String, Object>> supplierDataGrid(TSignature info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="select distinct t.batch_no,t.supplier_name,format(sum(t.pay_amt),2) as pay_amt from t_signature t "+supplierQuery(info)+" group by t.batch_no,t.supplier_name order by ";
		if("pay_amt".equals(ph.getSort()))
		{
			sql+=" sum(t.pay_amt) ";
		}
		else if("supplier_name".equals(ph.getSort()))
		{
			sql+="convert(t.supplier_name using gbk) ";
		}
		else
		{
			sql+=" t." + ph.getSort();
		}	
		sql+=" " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		List<Map<String, Object>> signList=jdbcTemplate.queryForList(sql);
		return signList;
	}

	@Override
	public Long supplierCount(TSignature info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String sql="select count(distinct t.batch_no,t.supplier_name) from t_signature t "+supplierQuery(info); 
		return signatureDao.countBySql(sql).longValue();
	}

	@Override
	public List<Map<String, Object>> accountDataGrid(TSignature info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="select t.id,t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.address_code,t.address_name,t.account_name,format((case when sum(t.pay_amt) is null then 0 else sum(t.pay_amt) end),2) as pay_amt,format((case when sum(t.invoice_amt) is null then 0 else sum(t.invoice_amt) end),2) as invoice_amt from t_signature t "+accountQuery(info)+" group by t.project_code,t.account_name order by ";
		if("pay_amt".equals(ph.getSort())||"invoice_amt".equals(ph.getSort()))
		{
			sql+=" sum(t."+ph.getSort()+") ";
		}
		else if("account_name".equals(ph.getSort()))
		{
			sql+="convert(t.account_name using gbk) ";
		}
		else
		{
			sql+=" t." + ph.getSort();
		}	
		sql+=" " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		List<Map<String, Object>> signList=jdbcTemplate.queryForList(sql);
		//String hql="from TSignature t "+accountQuery(info)+" order by t." + ph.getSort() + " " + ph.getOrder();
		return signList;
	}

	@Override
	public Long accountCount(TSignature info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(distinct t.project_code,t.account_name) from t_signature t "+accountQuery(info); 
		return signatureDao.countBySql(hql).longValue();
	}

	private String supplierQuery(TSignature info)
	{
		String hql=" where t.org_id = '"+info.getOrg_id()+"'";
		if(StringUtils.hasText(info.getSupplier_name()))
		{
			hql+=" and t.supplier_name like '%"+info.getSupplier_name()+"%'";
		}
		return hql;
	}

	private String accountQuery(TSignature info)
	{
		String hql=" where t.org_id = '"+info.getOrg_id()+"'";
		if(StringUtils.hasText(info.getSupplier_name()))
		{
			hql+=" and t.supplier_name = '"+info.getSupplier_name()+"'";
		}
		if(StringUtils.hasText(info.getBatch_no()))
		{
			hql+=" and t.batch_no = '"+info.getBatch_no()+"'";
		}
		return hql;
	}
	@Override
	public void save(SignInfo info) throws Exception {
		// TODO Auto-generated method stub
		/*List<Signature> signList=info.getSignList();
		for(Signature sign:signList)
		{
			TPayment payment=new TPayment();
			payment.setMonth(info.getMonth());
			payment.setVoucher_no(info.getVoucher_no());
			payment.setProject_code(sign.getProject_code());
			payment.setSupplier_code(sign.getSupplier_code());
			payment.setSupplier_name(sign.getSupplier_name());
			payment.setOrder_code(sign.getOrder_code());
			payment.setAccount_name(sign.getAccount_name());
			payment.setAddress_code(sign.getAddress_code());
			payment.setAddress_name(sign.getAddress_name());
			payment.setInvoice_amt(BigDecimal.valueOf(new DecimalFormat().parse(sign.getInvoice_amt()).doubleValue()));
			payment.setPay_amt(BigDecimal.valueOf(new DecimalFormat().parse(sign.getPay_amt()).doubleValue()));
			payment.setRemark(sign.getRemark());
			payment.setUpdate_by(info.getUser_id());
			payment.setTax_rate(sign.getTax_rate());
			payment.setUpdate_time(new Date());
			paymentDao.save(payment);
			TSignature tSign=new TSignature();
			tSign.setId(sign.getId());
			signatureDao.delete(tSign);
		}*/
		String str=StringUtils.hasText(info.getRemark())?info.getRemark():" ";
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql="insert into t_payment(month,project_code,supplier_code,supplier_name,order_code,address_code,address_name,account_name,invoice_amt,invoice_amt_e_tax,pay_amt,"
				+ "pay_amt_e_tax,tax_rate,remark,online_offline,voucher_no,update_by,org_id,update_time,uuid,flag) select '"+info.getMonth()+"',t.project_code,t.supplier_code,t.supplier_name,t.order_code,"
				+ "t.address_code,t.address_name,t.account_name,t.invoice_amt,t.invoice_amt_e_tax,t.pay_amt,t.pay_amt_e_tax,t.tax_rate,'"+str+"','线下','"+info.getVoucher_no()+"','"+info.getUser_id()+"',t.org_id,"
				+ "'"+sdf.format(new Date())+"',t.uuid,case when exists(select 1 from t_cost c where c.org_id = t.org_id and c.project_code = t.project_code and c.supplier_name = t.supplier_name and c.account_name = t.account_name and c.order_type like '退库红冲%') then 1 else 0 end from t_signature t where t.batch_no = '"+info.getBatch_no()+"' and t.supplier_name = '"+info.getSupplier_name()+"' and t.org_id = '"+info.getOrg_id()+"'";
		/*String sql="insert into t_payment(month,project_code,supplier_code,supplier_name,order_code,address_code,address_name,account_name,invoice_amt,invoice_amt_e_tax,pay_amt,"
				+ "pay_amt_e_tax,tax_rate,remark,online_offline,voucher_no,update_by,org_id,update_time,uuid) select '"+info.getMonth()+"',t.project_code,t.supplier_code,t.supplier_name,t.order_code,"
				+ "t.address_code,t.address_name,t.account_name,t.invoice_amt,t.invoice_amt_e_tax,t.pay_amt,t.pay_amt_e_tax,t.tax_rate,'"+str+"','线下','"+info.getVoucher_no()+"','"+info.getUser_id()+"',t.org_id,"
				+ "'"+sdf.format(new Date())+"',t.uuid from t_signature t where t.batch_no = '"+info.getBatch_no()+"' and t.supplier_name = '"+info.getSupplier_name()+"' and t.org_id = '"+info.getOrg_id()+"'";*/
		jdbcTemplate.execute(sql);
		System.out.println("######付款成功："+sdf.format(new Date()));
		String updateSql="update t_over_view v set v.table_name = 't_payment',v.t_pay_amt=v.s_pay_amt,v.s_pay_amt=0,v.month='"+info.getMonth()+"',v.flag=case when exists(select 1 from t_cost c where c.org_id = v.org_id and c.project_code = v.project_code and c.supplier_name = v.supplier_name and c.account_name = v.account_name and c.order_type like '退库红冲%') then 1 else 0 end  where v.org_id = '"+info.getOrg_id()+"' and v.table_name = 't_signature' and exists (select 1 from t_signature s where s.batch_no = '"+info.getBatch_no()+"' and s.supplier_name = '"+info.getSupplier_name()+"' and s.org_id = '"+info.getOrg_id()+"' and s.uuid = v.uuid)";
		//String updateSql="update t_over_view v set v.table_name = 't_payment',v.t_pay_amt=v.s_pay_amt,v.s_pay_amt=0 where v.org_id = '"+info.getOrg_id()+"' and v.table_name = 't_signature' and exists (select 1 from t_signature s where s.batch_no = '"+info.getBatch_no()+"' and s.supplier_name = '"+info.getSupplier_name()+"' and s.org_id = '"+info.getOrg_id()+"' and s.uuid = v.uuid)";
		System.out.println("######更新sql:"+updateSql);
		jdbcTemplate.execute(updateSql);
		System.out.println("######更新完成："+sdf.format(new Date()));
		String deleteSql="delete from t_signature where batch_no = '"+info.getBatch_no()+"' and supplier_name = '"+info.getSupplier_name()+"' and org_id = '"+info.getOrg_id()+"'";
		System.out.println("######删除sql:"+deleteSql);
		jdbcTemplate.execute(deleteSql);
		System.out.println("######删除完成："+sdf.format(new Date()));
	}

	@Override
	public void delete(String batchs,String org_id) throws Exception {
		// TODO Auto-generated method stub
		String deleteSql2="delete t from t_over_view t where t.table_name = 't_signature' and t.org_id = '"+org_id+"' and exists (select 1 from t_signature s where s.batch_no = '"+batchs+"' and s.org_id = '"+org_id+"' and s.uuid = t.uuid)";
		jdbcTemplate.execute(deleteSql2);
		String deleteSql1="delete from t_signature where batch_no = '"+batchs+"' and org_id = '"+org_id+"'";
		signatureDao.executeSql(deleteSql1);
	}
}
