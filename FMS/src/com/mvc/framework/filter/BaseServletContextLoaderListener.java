package com.mvc.framework.filter;


import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

//import com.wits.hc.util.CacheManager;


/**启动监听
 * @author liujun
 *
 */
public class BaseServletContextLoaderListener extends ContextLoaderListener{

	private static final Logger logger = Logger.getLogger(BaseServletContextLoaderListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {		
		logger.info("[INFOR : BaseServletContextLoaderListener.contextInitialized 启动监听。。。。。。]");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("[INFOR : BaseServletContextLoaderListener.contextDestroyed 销毁。。。。。。]");
	}
	
	/*public void setConfTask(){
		ConfDao confDao = ServiceFactory.getInstance().createConfDao();
		List<ConfDetail> conDetailist = confDao.getLanchConfs();
		HashMap<String, ArrayList<Integer>> conMap = new HashMap<String, ArrayList<Integer>>();
		HashMap<String, Long> contimeMap = new HashMap<String, Long>();
		
		if(conDetailist!=null && conDetailist.size()>0){
			for (int i = 0; i < conDetailist.size(); i++) {
				ConfDetail detail = conDetailist.get(i);
				if(conMap.containsKey(detail.getConfid())){
					conMap.get(detail.getConfid()).add(detail.getMemberID());
				} else {
					ArrayList<Integer> idList = new ArrayList<Integer>();
					idList.add(detail.getMemberID());
					conMap.put(detail.getConfid(), idList);
					long diff;
					diff = detail.getConftime().getTime() - new Date().getTime();
					if(diff>30){
						contimeMap.put(detail.getConfid(), diff);
					}
				}
			}
			 for (String key : conMap.keySet()) {
				 	String confid = key;
					if(contimeMap.containsKey(confid)){
						Timer tm = new Timer();
						ConTimerServiceTask conftask = new ConTimerServiceTask(confid);
						CacheManager.putCache(confid, conftask);
						tm.schedule(conftask, contimeMap.get(confid));
					}
			}
		}
	}*/
	
	
}
