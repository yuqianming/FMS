package com.mvc.service.home.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TPayment;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.home.HomeServiceI;

@Service
public class HomeServiceImpl implements HomeServiceI{

	@Autowired
	private BaseDaoI<TPayment> paymentDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	@Override
	public List<TPayment> getPayOffList(){
		String hql="select distinct t from TPayment t order by t.pay_amt desc";
		List<TPayment> paymentList=paymentDao.find(hql,null,1,10);
		return paymentList;
	}
	
	@Override
	public List<Map<String,Object>> payOffDataGrid(String orgId,PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		
		String sql=" SELECT "
				+ " Format(SUM(t.pay_amt),2) AS format_pay_amt,"
				+ " SUM(t.pay_amt) AS pay_amt,"
				+ "t.supplier_name "
				+ " FROM "
				+ " t_payment t"
				
				+ " where "
				+ " org_id ='"+orgId+"'"
				
				+ " GROUP BY"
				+ " t.supplier_name"
				
				+ " ORDER BY"
				+ " sum(t.pay_amt) DESC"
				
				+ " LIMIT 0,10";
		
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		System.out.println("###### pay datagrid end time : "+sdf.format(new Date()));
		return mapList;
	}
	
	@Override
	public List<Map<String, Object>> getDataImport(String orgId, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String sql="select distinct tlog.*,tuser.user_name from t_log tlog left join user_info tuser on tlog.user_id = tuser.user_id where tlog.log_type = '1' and tuser.org_id = '"+orgId+"'";
		if(ph==null)
		{
			sql+=" order by tlog.log_time desc limit 0,10";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String, Object>> listMap =jdbcTemplate.queryForList(sql);
		return listMap;
	}
	
	@Override
	public Long count(String orgId) throws Exception {
		// TODO Auto-generated method stub
		String sql="select count(*) from t_log tlog where tlog.org_id = '"+orgId+"' ";
//		return jdbcTemplate.queryForInt(sql);
		return jdbcTemplate.queryForObject(sql, Long.class);
	}
	
	/*@Override
	public List<Map<String, Object>> getDataImportGrid(String orgId, PageFilter ph) {
		// TODO Auto-generated method stub
		String sql="select distinct tlog.*,tuser.user_name from t_log tlog left join user_info tuser on tlog.user_id = tuser.user_id where tlog.log_type = '1' and tuser.org_id = '"+orgId+"' order by tlog.log_time desc";
		if(ph==null)
		{
			sql+=" order by t.id asc";
		}
		else
		{
			sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}*/

	@Override
	public List<Map<String, Object>> initCustomTreeList(String userId) throws Exception {
		// TODO Auto-generated method stub
		String sql="select t.custom_id from user_custom t where t.user_id = '"+userId+"'";
		List<Map<String, Object>> listTree =jdbcTemplate.queryForList(sql);
		return listTree;
	}

	@Override
	public int[] updateCustom(String userId,String customIds) throws Exception {
		// TODO Auto-generated method stub
		String sql="delete t.* from user_custom t where t.user_id = '"+userId+"'";
		jdbcTemplate.execute(sql);
		
		char [] strArr = customIds.toCharArray();
		
		String[] sqls = new String[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			String sqlTemp="insert into user_custom (user_id,custom_id) values('"+userId+"','"+strArr[i]+"')";
			sqls[i] = sqlTemp;
		}
		
		/*List<String> strs = new ArrayList<>();
		for (int i = 0; i < customList.size(); i++) {
			String sqlTemp="insert into (user_id,custom_id) user_custom t values('"+userId+"','"+customList.get(i)+"')";
			strs.add(sqlTemp);
		}*/
		
//		jdbcTemplate.batchUpdate((String[])strs.toArray());
		return jdbcTemplate.batchUpdate(sqls);
	}

	@Override
	public Map<String, Object> getNoticeDept(String orgId) throws Exception {
		// TODO Auto-generated method stub
		String sql="select t.org_id,t.org_name from department_info t where t.org_id = '"+orgId+"'";
		Map<String, Object> mapOrg =jdbcTemplate.queryForMap(sql);
		return mapOrg;
	}

	@Override
	public Map<String, Object> getNoticeUser(String userId) throws Exception {
		// TODO Auto-generated method stub
		String sql="select t.user_id,t.user_name from user_info t where t.user_id = '"+userId+"'";
		Map<String, Object> mapUser =jdbcTemplate.queryForMap(sql);
		return mapUser;
	}
	
}
