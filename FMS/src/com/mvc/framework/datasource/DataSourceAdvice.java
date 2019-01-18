package com.mvc.framework.datasource;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

//AOP切面类 主数据库增删改 从数据库查
public class DataSourceAdvice implements MethodBeforeAdvice,
		AfterReturningAdvice, ThrowsAdvice {
	// service方法执行之前被调用
	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println("切入点: " + target.getClass().getName() + "类中" + method.getName() + "方法");
		if (method.getName().startsWith("get")
				|| method.getName().startsWith("find")
				|| method.getName().startsWith("load")
				|| method.getName().startsWith("search")
				|| method.getName().startsWith("dataGrid")
				|| method.getName().startsWith("count")
				|| method.getName().startsWith("tree")
				|| method.getName().startsWith("list")) {
			System.out.println("切换到: slave");
			DataSourceSwitcher.setSlave();
			
		} else if (method.getName().startsWith("oraLoad")) {
			System.out.println("切换到: bizOra");
			DataSourceSwitcher.setBizOra();
		} else if (method.getName().startsWith("bvvnLoad")) {
			System.out.println("切换到: bvvnOra");
			DataSourceSwitcher.setBvvnOra();
		} else if (method.getName().startsWith("bpvnLoad")) {
			System.out.println("切换到: bpvnOra");
			DataSourceSwitcher.setBpvnOra();
		} else {
			System.out.println("切换到: master");
			DataSourceSwitcher.setMaster();
		}
	}

	// service方法执行完之后被调用
	public void afterReturning(Object arg0, Method method, Object[] args,
			Object target) throws Throwable {
	}

	// 抛出Exception之后被调用
	public void afterThrowing(Method method, Object[] args, Object target,
			Exception ex) throws Throwable {
		if("slave".equals(DataSourceSwitcher.getDataSource())){
			DataSourceSwitcher.setMaster();
			System.out.println("出现异常,切换到: master");
		}
	}
}
