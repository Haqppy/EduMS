package com.atguigu.ems.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Role;

@Repository
public class RoleDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public Role getWithAuthorites(Integer id){
		String hql = "FROM Role r LEFT OUTER JOIN FETCH r.authorities aus "
				+ "LEFT OUTER JOIN FETCH aus.parentAuthority WHERE r.roleId = ?";
		Query query = getSession().createQuery(hql).setInteger(0, id);
		return (Role) query.uniqueResult();
	}
	
	public void delete(Integer id){
		Role role = (Role) getSession().get(Role.class, id);
		if(role != null){
			getSession().delete(role);
		}
	}
	
	public void save(Role role){
		getSession().saveOrUpdate(role);
	}
	
	public List<Role> getAll(){
		String hql = "FROM Role";
		Query query = getSession().createQuery(hql);
		return query.list();
	}
	
	public List<Role> getRoleByNames(String [] roleNames){
		Criteria criteria = getSession().createCriteria(Role.class);
		criteria.add(Restrictions.in("roleName", roleNames));
		return criteria.list();
	}
}
