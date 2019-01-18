package com.mvc.framework.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;  

//数据源动态切换类
public class DynamicDataSource extends AbstractRoutingDataSource {  
	@Override  
    protected Object determineCurrentLookupKey() {  
        return DataSourceSwitcher.getDataSource();  
    } 
}
