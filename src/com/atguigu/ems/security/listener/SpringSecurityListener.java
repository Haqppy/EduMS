package com.atguigu.ems.security.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringSecurityListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//1. 获取 IOC 容器的引用
		ApplicationContext ctx = 
				WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext());
		
		//2. 获取 FilterSecurityInterceptor 对象
		FilterSecurityInterceptor interceptor = 
				ctx.getBean(FilterSecurityInterceptor.class);
		
		//3. 从 IOC 容器中获取 FilterInvocationSecurityMetadataSource 对象
		FilterInvocationSecurityMetadataSource securityMetadataSource = 
				(FilterInvocationSecurityMetadataSource) ctx.getBean("securityMetadataSource");
		
		//4. 调用 FilterSecurityInterceptor 的 setSecurityMetadataSource 方法为 
		//securityMetadataSource 赋值
		interceptor.setSecurityMetadataSource(securityMetadataSource);
	}

}
