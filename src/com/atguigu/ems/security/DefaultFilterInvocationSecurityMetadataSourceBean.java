package com.atguigu.ems.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Service;

@Service("securityMetadataSource")
public class DefaultFilterInvocationSecurityMetadataSourceBean implements FactoryBean<DefaultFilterInvocationSecurityMetadataSource>{

	@Autowired
	private SecurityMetadataSourceSrcMapBuilder srcMapBuilder;
	
	@Override
	public DefaultFilterInvocationSecurityMetadataSource getObject()
			throws Exception {
		
		//该 Map 中放入的是资源, 和访问该资源的权限的名字的 Map
		//例如: /emp-list:[ROLE_EMP_LIST]; /emp-input:[ROLE_EMP_SAVE,ROLE_EMP_UPDATE]
		Map<String, List<String>> srcMap = srcMapBuilder.buildSrcMap();
		System.out.println("srcMap: " + srcMap);
		
		DefaultFilterInvocationSecurityMetadataSource metadataSource = null;
		
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = 
				new LinkedHashMap<>();
		
		for(Map.Entry<String, List<String>> entry: srcMap.entrySet()){
			String url = entry.getKey();
			List<String> roles = entry.getValue();
			
			RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
			List<ConfigAttribute> attrs = new ArrayList<>();
			for(String role: roles){
				attrs.add(new SecurityConfig(role));
			}
			
			requestMap.put(requestMatcher, attrs);
		}
				
		metadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestMap);
		
		return metadataSource;
	}

	@Override
	public Class<?> getObjectType() {
		return DefaultFilterInvocationSecurityMetadataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
