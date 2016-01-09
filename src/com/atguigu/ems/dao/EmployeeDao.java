package com.atguigu.ems.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.orm.Page;

@Repository
public class EmployeeDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public List<Employee> getAll(){
		String hql = "FROM Employee e LEFT OUTER JOIN FETCH e.department LEFT OUTER JOIN FETCH e.roles";
		Query query = getSession().createQuery(hql);
		
		return query.list();
	}
	
	//获取 Employee, 其中包含引用字段
	public Employee getByRef(Integer id) {
		String hql = "FROM Employee e "
				+ "LEFT OUTER JOIN FETCH e.department "
				+ "LEFT OUTER JOIN FETCH e.roles "
				+ "LEFT OUTER JOIN FETCH e.editor "
				+ "WHERE e.employeeId = ?";
		Query query = getSession().createQuery(hql).setInteger(0, id);
		return (Employee) query.uniqueResult();
	}
	
	//获取 Employee, 其中不包含任何引用字段
	public Employee get(Integer id){
		Employee employee = (Employee) getSession().get(Employee.class, id);
		return employee;
	}
	
	public Employee getByLoginName(String loginName){
		String hql = "FROM Employee e WHERE e.loginName = ?";
		Query query = getSession().createQuery(hql).setString(0, loginName);
		return (Employee) query.uniqueResult();
	}
	
	public void save(Employee employee){
		getSession().saveOrUpdate(employee);
	}
	
	public Long getTotalItemNumbers(){
		String hql = "SELECT count(employeeId) FROM Employee";
		Query query = getSession().createQuery(hql);
		return (Long) query.uniqueResult();
	}
	
	public List<Employee> getPageList(int firstResult, int maxResults){
		String hql = "FROM Employee e LEFT OUTER JOIN FETCH e.department LEFT OUTER JOIN FETCH e.roles";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(firstResult).setMaxResults(maxResults);
		
		return query.list();
	}
	
	public void getPage(Page<Employee> page){
		page.setTotalItemNumbers(getTotalItemNumbers());
		page.setContent(getPageList(page.getFirstResult(), page.getMaxResults()));
	}

	public void save(List<Employee> emps) {
		
		for(int i = 0; i < emps.size(); i++){
			Employee emp = emps.get(i);
			getSession().save(emp);
			
			if((i + 1) % 50 == 0){
				getSession().flush();
				getSession().clear();
			}
		}
		
		/*
		getSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				String sql = "INSERT INTO oa_employee(department_id, email, employee_name, "
						+ "enabled, gender, login_name, password, visited_times) VALUES("
						+ "?,?,?,?,?,?)";
			}
		});
		*/
	}

	
}
