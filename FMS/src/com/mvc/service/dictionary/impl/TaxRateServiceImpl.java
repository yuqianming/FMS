package com.mvc.service.dictionary.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TLog;
import com.mvc.model.sys.TSupplier;
import com.mvc.model.sys.TTaxRate;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.dictionary.TaxRateServiceI;
import com.mvc.utils.ExcelReaderUtil;

@Service
public class TaxRateServiceImpl implements TaxRateServiceI{
	@Autowired
	private BaseDaoI<TTaxRate> taxRateDao;
	@Autowired
	private BaseDaoI<TLog> logDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Map<String,Object>> dataGrid(TTaxRate info, PageFilter ph)
			throws Exception {
		// TODO Auto-generated method stub
		String sql="select id,concat(floor(t.tax_rate*100),'%') as rate_txt,t.scope from t_tax_rate t "+whereHql(info)+" order by " + ph.getSort() + " " + ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(TTaxRate info, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String hql="select count(*) from TTaxRate t"+whereHql(info);
		return taxRateDao.count(hql);
	}
	
	private String whereHql(TTaxRate info)
	{
		String hql="";
		//String hql=" where t.org_id = '"+info.getOrg_id()+"' ";
//		if(StringUtils.hasText(info.get))
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
	public void add(TTaxRate info) throws Exception {
		// TODO Auto-generated method stub
		taxRateDao.save(info);
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		taxRateDao.executeSql("delete from t_tax_rate where id in ("+ids+")");
	}

	@Override
	public List<Map<String,Object>> getRateList() throws Exception {
		// TODO Auto-generated method stub
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select tax_rate,concat(floor(tax_rate*100),'%') as rate_txt from t_tax_rate  order by tax_rate asc");
		return mapList;
	}
	
	@Override
	public void edit(TTaxRate info) throws Exception {
		// TODO Auto-generated method stub
		taxRateDao.update(info);
	}

	@Override
	public void upload(UploadInfo info) throws Exception {
		// TODO Auto-generated method stub
		taxRateDao.executeSql("delete from t_tax_rate ");
		//List<String[]> allList=ReadFileUtil_2.readFile(session, info.getUserId(), info.getUpFile());
		List<String[]> allList=ExcelReaderUtil.readExcel(info.getUpFile());
		if(allList!=null&&allList.size()>0)
		{
			for(String[] temp:allList)
			{
				TTaxRate tr=new TTaxRate();
				tr.setScope(temp[0]);
				tr.setTax_rate(new BigDecimal(temp[1]));
				tr.setUpdate_by(info.getUserId());
				tr.setUpdate_time(new Date());
				taxRateDao.save(tr);
			}
			TLog log=new TLog();
			log.setTable_name("t_tax_rate");
			log.setUser_id(info.getUserId());
			log.setLog_type("1");
			log.setLog_time(new Date());
			log.setFile_name(info.getUpFile().getOriginalFilename());
			logDao.save(log);
		}
		else
		{
			throw new Exception("请上传非空文件！");
		}
	}
}
