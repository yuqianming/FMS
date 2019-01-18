package com.mvc.service.dictionary.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TLog;
import com.mvc.model.sys.TSupplier;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.dictionary.SupplierServiceI;
import com.mvc.utils.ExcelReaderUtil;
import com.mvc.utils.ReadFileUtil_2;

@Service
public class SupplierServiceImpl implements SupplierServiceI{
	@Autowired
	private BaseDaoI<TSupplier> supplierDao;
	@Autowired
	private BaseDaoI<TLog> logDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<TSupplier> dataGrid(TSupplier info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String hql="from TSupplier t "+whereHql(info)+" order by t." + ph.getSort() + " " + ph.getOrder();;
		return supplierDao.find(hql,ph.getPage(),ph.getRows());
	}

	@Override
	public Long count(TSupplier info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(*) from TSupplier t"+whereHql(info);
		return supplierDao.count(hql);
	}
	
	private String whereHql(TSupplier info)
	{
		String hql=" where t.org_id = '"+info.getOrg_id()+"' ";
		if(StringUtils.hasText(info.getSupplier_code()))
		{
			hql=" and t.supplier_code like '%"+info.getSupplier_code()+"%'";
		}
		if(StringUtils.hasText(info.getSupplier_name()))
		{
			hql=" and t.supplier_name like '%"+info.getSupplier_name()+"%'";
		}
		return hql;
	}

	@Override
	public void add(TSupplier info) throws Exception {
		// TODO Auto-generated method stub
		List<TSupplier> supplierList=supplierDao.find("from TSupplier t where t.supplier_name = '"+info.getSupplier_name()+"' and org_id = '"+info.getOrg_id()+"'");
		if(supplierList!=null&&supplierList.size()>0)
		{
			throw new Exception("不能存在同名供应商！");
		}
		info.setUnique_name(info.getSupplier_name());
		supplierDao.save(info);
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		supplierDao.executeSql("delete from t_supplier where id in ("+ids+")");
	}
	
	@Override
	public void edit(TSupplier info) throws Exception {
		// TODO Auto-generated method stub
		List<TSupplier> supplierList=supplierDao.find("from TSupplier t where t.supplier_name = '"+info.getSupplier_name()+"' and t.org_id = '"+info.getOrg_id()+"'");
		if(supplierList!=null&&supplierList.size()>0)
		{
			TSupplier supplier=supplierList.get(0);
			if(supplier.getId()!=info.getId())
			{
				throw new Exception("该名称供应商已存在！");
			}
			else
			{
				supplier.setSupplier_code(info.getSupplier_code());
				supplier.setSupplier_name(info.getSupplier_name());
				supplier.setUpdate_by(info.getUpdate_by());
				supplier.setOrg_id(info.getOrg_id());
				supplier.setUpdate_time(info.getUpdate_time());
				supplierDao.update(supplier);
			}
		}
		else
		{
			supplierDao.update(info);
		}
	}

	@Override
	public List<Map<String,Object>> getSupplierList(String org_id) throws Exception {
		// TODO Auto-generated method stub
		//return supplierDao.find("select distinct t from TSupplier t where t.org_id = '"+org_id+"'");
		return jdbcTemplate.queryForList("select distinct unique_name as supplier_name from t_supplier where org_id = '"+org_id+"'");
	}

	@Override
	public void upload(UploadInfo info, HttpSession session) throws Exception {
		// TODO Auto-generated method stub
		supplierDao.executeSql("delete from t_supplier where org_id = '"+info.getOrgId()+"'");
		//List<String[]> allList=ReadFileUtil_2.readFile(session, info.getUserId(), info.getUpFile());
		List<String[]> allList=ExcelReaderUtil.readExcel(info.getUpFile());
		if(allList!=null&&allList.size()>0)
		{
			for(String[] temp:allList)
			{
				TSupplier ts=new TSupplier();
				ts.setSupplier_code(temp[0]);
				ts.setSupplier_name(temp[1]);
				ts.setUnique_name(temp[1]);
				ts.setUpdate_by(info.getUserId());
				ts.setOrg_id(info.getOrgId());
				ts.setUpdate_time(new Date());
				supplierDao.save(ts);
			}
			TLog log=new TLog();
			log.setTable_name("t_supplier");
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
