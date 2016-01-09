package com.atguigu.ems.security.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.ems.dao.ResourceDao;
import com.atguigu.ems.entities.Resource;
import com.atguigu.ems.security.SecurityMetadataSourceSrcMapBuilder;
import com.atguigu.ems.utils.ReflectionUtils;

@Service
public class SecurityMetadataSourceSrcMapBuilderImpl implements
		SecurityMetadataSourceSrcMapBuilder {

	@Autowired
	private ResourceDao resourceDao;
	
	@Override
	public Map<String, List<String>> buildSrcMap() {
		Map<String, List<String>> srcMap = new HashMap<String, List<String>>();
		
		List<Resource> resources = resourceDao.getAll();
		for(Resource resource: resources){
			String url = resource.getUrl();
			List<String> roles = ReflectionUtils.fetchElementPropertyToList(resource.getAuthorities(), "name");
		
			srcMap.put(url, roles);
		}
		
		return srcMap;
	}

}
