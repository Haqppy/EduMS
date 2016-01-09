package com.atguigu.ems.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Department;
import com.atguigu.ems.entities.Employee;

@Repository
public class DepartmentDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public boolean isManager(Integer empId){
		String hql = "FROM Department d WHERE d.manager = ?";
		Employee mgr = new Employee();
		mgr.setEmployeeId(empId);
		
		Query query = getSession().createQuery(hql).setEntity(0, mgr);
		return query.uniqueResult() != null;
	}
	
	public List<Department> getAll(){
		String hql = "FROM Department";
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	public Department getDepartmentByName(String deptName) {
		String hql = "FROM Department d WHERE d.departmentName = ?";
		Query query = getSession().createQuery(hql).setString(0, deptName);
		return (Department) query.uniqueResult();
	}
	
}
