package com.mvc.service.dictionary.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TLog;
import com.mvc.model.sys.TSupplierAdj;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.dictionary.SupplierAdjServiceI;
import com.mvc.utils.ExcelReaderUtil;

@Service
public class SupplierAdjServiceImpl implements SupplierAdjServiceI{
	@Autowired
	private BaseDaoI<TSupplierAdj> supplierAdjDao;
	@Autowired
	private BaseDaoI<TLog> logDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String,Object>> dataGrid(TSupplierAdj info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="select t.id,t.s_supplier_code,t.s_supplier_name,t.t_supplier_code,t.t_supplier_name from t_supplier_adj t "+whereHql(info);
		if(ph==null)
		{
			sql+=" order by s_supplier_code asc";
		}
		else
		{
		    sql+=" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		}
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(TSupplierAdj info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(*) from TSupplierAdj t"+whereHql(info);
		return supplierAdjDao.count(hql);
	}
	
	private String whereHql(TSupplierAdj info)
	{
		String hql=" where t.org_id = '"+info.getOrg_id()+"' ";
//		if(StringUtils.hasText(info.getProject_code()))
//		{
//			hql=" and t.project_code like '%"+info.getProject_code()+"%'";
//		}
//		if(StringUtils.hasText(info.getProject_name()))
//		{
//			hql=" and t.project_name like '%"+info.getProject_name()+"%'";
//		}
		return hql;
	}

	@Override
	public void add(TSupplierAdj info) throws Exception {
		// TODO Auto-generated method stub
		supplierAdjDao.save(info);
		String sql="update t_supplier set unique_name = (select a.unique_name from (select unique_name from t_supplier  where org_id = '"+info.getOrg_id()+"' and supplier_name='"+info.getT_supplier_name()+"') a) where org_id = '"+info.getOrg_id()+"' and supplier_name='"+info.getS_supplier_name()+"'";
		supplierAdjDao.executeSql(sql);
		supplierAdjDao.executeSql("update t_over_view set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
		supplierAdjDao.executeSql("update t_cost set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
		supplierAdjDao.executeSql("update t_signature set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
		supplierAdjDao.executeSql("update t_payment set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		supplierAdjDao.executeSql("delete from t_supplier_adj where id in ("+ids+")");
	}
	
	@Override
	public void edit(TSupplierAdj info) throws Exception {
		// TODO Auto-generated method stub
		supplierAdjDao.update(info);
		String sql="update t_supplier set unique_name = (select a.unique_name from (select unique_name from t_supplier  where supplier_name='"+info.getT_supplier_name()+"' and org_id = '"+info.getOrg_id()+"') a) where supplier_name='"+info.getS_supplier_name()+"' and org_id = '"+info.getOrg_id()+"'";
		supplierAdjDao.executeSql(sql);
		supplierAdjDao.executeSql("update t_over_view set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
		supplierAdjDao.executeSql("update t_cost set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
		supplierAdjDao.executeSql("update t_signature set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
		supplierAdjDao.executeSql("update t_payment set supplier_name = '"+info.getT_supplier_name()+"' where org_id = '"+info.getOrg_id()+"' and supplier_name = '"+info.getS_supplier_name()+"'");
	}

	@Override
	public void upload(UploadInfo info, HttpSession session) throws Exception {
		// TODO Auto-generated method stub
		supplierAdjDao.executeSql("delete from t_supplier_adj where org_id = '"+info.getOrgId()+"'");
		//List<String[]> allList=ReadFileUtil_2.readFile(session, info.getUserId(), info.getUpFile());
		List<String[]> allList=ExcelReaderUtil.readExcel(info.getUpFile());
		if(allList!=null&&allList.size()>0)
		{
			for(String[] temp:allList)
			{
				TSupplierAdj tsa=new TSupplierAdj();
				tsa.setS_supplier_code(temp[0]);
				tsa.setS_supplier_name(temp[1]);
				tsa.setT_supplier_code(temp[2]);
				tsa.setT_supplier_name(temp[3]);
				tsa.setUpdate_by(info.getUserId());
				tsa.setOrg_id(info.getOrgId());
				tsa.setUpdate_time(new Date());
				supplierAdjDao.save(tsa);
				String sql="update t_supplier set unique_name = (select a.unique_name from (select unique_name from t_supplier  where supplier_name='"+temp[3]+"' and org_id = '"+info.getOrgId()+"') a) where supplier_name='"+temp[1]+"' and org_id = '"+info.getOrgId()+"'";
				supplierAdjDao.executeSql(sql);
				supplierAdjDao.executeSql("update t_over_view set supplier_name = '"+temp[3]+"' where org_id = '"+info.getOrgId()+"' and supplier_name = '"+temp[1]+"'");
				supplierAdjDao.executeSql("update t_cost set supplier_name = '"+temp[3]+"' where org_id = '"+info.getOrgId()+"' and supplier_name = '"+temp[1]+"'");
				supplierAdjDao.executeSql("update t_signature set supplier_name = '"+temp[3]+"' where org_id = '"+info.getOrgId()+"' and supplier_name = '"+temp[1]+"'");
				supplierAdjDao.executeSql("update t_payment set supplier_name = '"+temp[3]+"' where org_id = '"+info.getOrgId()+"' and supplier_name = '"+temp[1]+"'");
			}
			TLog log=new TLog();
			log.setTable_name("t_supplier_adj");
			log.setUser_id(info.getUserId());
			log.setLog_type("1");
			log.setLog_time(new Date());
			log.setFile_name(info.getUpFile().getOriginalFilename());
			log.setOrg_id(info.getOrgId());
			logDao.save(log);
		}
		else
		{
			throw new Exception("请上传非空文件！");
		}
	}
}
