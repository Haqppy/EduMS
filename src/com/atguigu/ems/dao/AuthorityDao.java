package com.atguigu.ems.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Authority;

@Repository
public class AuthorityDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public List<Authority> getParentAuthorities(){
		String hql = "SELECT DISTINCT a FROM Authority a INNER JOIN FETCH a.subAuthorities";
		Query query = getSession().createQuery(hql);
		return query.list();
	}
	
}
