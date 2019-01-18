package com.mvc.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TCancelProj;
import com.mvc.model.sys.TCrm;
import com.mvc.model.sys.TOldProj;
import com.mvc.model.sys.TPms;

public class CacheDictionary {
	@Autowired
	private BaseDaoI<TPms> pmsDao;
	@Autowired
	private BaseDaoI<TCrm> crmDao;
	@Autowired
	private BaseDaoI<TCancelProj> cancelDao;
	@Autowired
	private BaseDaoI<TOldProj> oldDao;
	public void init()
	{
		System.out.println("###### init ......");
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
