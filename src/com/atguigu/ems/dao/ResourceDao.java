package com.atguigu.ems.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Resource;

@Repository
public class ResourceDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public List<Resource> getAll(){
		return getSession().createCriteria(Resource.class).list();
	}
	
}
