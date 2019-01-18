package com.mvc.service.dataImport.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TSignature;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.dataImport.SignatureServiceI;
@Service
public class SignatureServiceImpl implements SignatureServiceI{
	@Autowired
	private BaseDaoI<TSignature> signDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String,Object>> dataGrid(TSignature info, PageFilter ph) {
		// TODO Auto-generated method stub
		//String hql="from TGodownEntry t order by t." + ph.getSort() + " " + ph.getOrder();
		String sql="select t.id,t.batch_no,t.project_code,t.supplier_code,t.supplier_name,t.order_code,t.account_name,"
				+ "t.address_code,t.address_name,format(t.invoice_amt,2) as invoice_amt_txt,t.invoice_amt,format(t.invoice_amt_e_tax,2) "
				+ "as invoice_amt_e_tax_txt,t.invoice_amt_e_tax,format(t.pay_amt,2) as pay_amt_txt,t.pay_amt,format(t.pay_amt_e_tax,2) as pay_amt_e_tax_txt,t.pay_amt_e_tax,t.remark,"
				+ "floor(t.tax_rate*100) as rate_txt,t.tax_rate,u.user_name as update_by,t.update_time,t.uuid from t_signature t left join user_info u on t.update_by = u.user_id "+whereSql(info);
		if(ph==null)
		{
			sql+=" order by t.batch_no asc";
		}
		else
		{
			sql+=" order by t." + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(TSignature info, PageFilter ph) {
		// TODO Auto-generated method stub
		String hql="select count(*) from TSignature t"+whereSql(info);
		return signDao.count(hql);
	}
	
	private String whereSql(TSignature info)
	{
		String sql=" where t.org_id = '"+info.getOrg_id()+"' ";
		return sql;
	}

	@Override
	public void delete(String ids,String org_id) throws Exception {
		// TODO Auto-generated method stub
		jdbcTemplate.execute("delete v from t_over_view v where v.table_name = 't_signature' and v.org_id = '"+org_id+"' and exists (select 1 from t_signature t where t.id in ("+ids+") and t.uuid = v.uuid)");
		jdbcTemplate.execute("delete from t_signature where id in ("+ids+")");
	}

	@Override
	public void edit(TSignature info) throws Exception {
		// TODO Auto-generated method stub
		signDao.update(info);
		jdbcTemplate.execute("update t_over_view set project_code = '"+info.getProject_code()+"',supplier_name = '"+info.getSupplier_name()+"',account_name = '"+info.getAccount_name()+"',s_pay_amt = "+info.getPay_amt_e_tax()+",t_invoice_amt = "+info.getInvoice_amt()+",t_invoice_amt_e_tax = "+info.getInvoice_amt_e_tax()+"  where uuid = '"+info.getUuid()+"'");
	}

}
