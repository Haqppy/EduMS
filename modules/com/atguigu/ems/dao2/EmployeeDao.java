package com.atguigu.ems.dao2;

import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.hibernate.HibernateDao;

@Repository("employeeDao2")
public class EmployeeDao extends HibernateDao<Employee, Integer>{
		
}
