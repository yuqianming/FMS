package com.mvc.utils;

import java.util.concurrent.locks.ReentrantLock;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * CacheUtils
 * @dependencies ehcache-core-2.5.0.jar
 * 
 */
public class CacheUtils {

	private static Cache paymentTest = null;
	private static ReentrantLock lock = new ReentrantLock();

	private static void init() {
		if (paymentTest == null) {
			lock.lock();
			if (paymentTest == null) {
				CacheManager manager = CacheManager.create(CacheUtils.class.getResourceAsStream("/ehcache.xml"));
				// InputStream is =
				// CacheUtils.class.getResourceAsStream("/ehcache.xml");
				// CacheManager manager = new CacheManager(is);
				paymentTest = manager.getCache("paymentTest");
				if (paymentTest == null) {
					paymentTest = new Cache("paymentTest", 10000, true, false, 86600000, 86500000);
				}
			}
			lock.unlock();
		}
	}

	public static void cacheMe(String key, Object obj) {
		if (paymentTest == null) {
			init();
		}
		lock.lock();
		paymentTest.put(new Element(key, obj));
		lock.unlock();
	}

	public static void removeMe(String key) {
		if (paymentTest == null) {
			return;
		}
		lock.lock();
		paymentTest.remove(key);
		lock.unlock();
	}

	public static Object getCache(String key) {
		if (paymentTest == null) {
			return null;
		}
		Element ele = paymentTest.get(key);
		if (ele == null) {
			return null;
		}
		return ele.getValue();
	}
}
