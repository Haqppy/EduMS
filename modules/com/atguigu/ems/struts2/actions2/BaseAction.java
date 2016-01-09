package com.atguigu.ems.struts2.actions2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.ems.orm2.Page;
import com.atguigu.ems.orm2.PropertyFilter;
import com.atguigu.ems.service2.BaseService;
import com.atguigu.ems.utils.ReflectionUtils;
import com.atguigu.ems.utils.web.HibernateWebUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class BaseAction<T, PK extends Serializable> 
	extends ActionSupport 
	implements ModelDriven<T>, Preparable, RequestAware{

	protected Map<String, Object> request;
	
	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

	@Override
	public void prepare() throws Exception {}

	protected T entity;
	
	@Override
	public T getModel() {
		return entity;
	}

	public BaseAction() {
		try {
			entity = (T) ReflectionUtils.getSuperGenericType(getClass()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected PK id;
	
	public void setId(PK id) {
		this.id = id;
	}
	
	public PK getId() {
		return id;
	}
	
	@Autowired
	protected BaseService<T, PK> baseService;

	protected Page<T> page;
	
	public void setPage(Page<T> page) {
		this.page = page;
	}
	
	public Page<T> getPage() {
		return page;
	}
	
	public void buildPage(){
		HttpServletRequest req = ServletActionContext.getRequest();
		List<PropertyFilter> filters = HibernateWebUtils.buildPropertyFilters(req);
		page = baseService.getPage(page, filters);
	}
	
}
