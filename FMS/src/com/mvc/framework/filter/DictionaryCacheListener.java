package com.mvc.framework.filter;

import java.util.List;

import javax.servlet.ServletContextEvent;

import org.springframework.beans.factory.annotation.Autowired;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TCancelProj;
import com.mvc.model.sys.TCrm;
import com.mvc.model.sys.TOldProj;
import com.mvc.model.sys.TPms;
import com.mvc.utils.CacheUtils;

public class DictionaryCacheListener implements javax.servlet.ServletContextListener{
	@Autowired
	private BaseDaoI<TPms> pmsDao;
	@Autowired
	private BaseDaoI<TCrm> crmDao;
	@Autowired
	private BaseDaoI<TCancelProj> cancelDao;
	@Autowired
	private BaseDaoI<TOldProj> oldDao;
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		List<TPms> pmsList=pmsDao.find("from TPms t");
		CacheUtils.cacheMe("pmsList", pmsList);
		List<TCrm> crmList=crmDao.find("from TCrm t");
		CacheUtils.cacheMe("crmList", crmList);
		List<TCancelProj> cancelList=cancelDao.find("from TCancelProj t");
		CacheUtils.cacheMe("cancelList", cancelList);
		List<TOldProj> oldList=oldDao.find("from TOldProj t");
		CacheUtils.cacheMe("oldList", oldList);
	}

}
