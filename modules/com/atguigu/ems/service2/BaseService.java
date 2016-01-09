package com.atguigu.ems.service2;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.atguigu.ems.hibernate.HibernateDao;
import com.atguigu.ems.orm2.Page;
import com.atguigu.ems.orm2.PropertyFilter;

public class BaseService<T, PK extends Serializable> {

	@Autowired
	private HibernateDao<T, PK> hibernateDao;
	
	public Page<T> getPage(Page<T> page, List<PropertyFilter> filters){
		return hibernateDao.findPage(page, filters);
	}
	
}
