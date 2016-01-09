package com.atguigu.ems.dao2;

import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Department;
import com.atguigu.ems.hibernate.HibernateDao;

@Repository("departmentDao2")
public class DepartmentDao extends HibernateDao<Department, Integer>{
	
}
