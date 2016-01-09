package com.atguigu.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.ems.dao.DepartmentDao;
import com.atguigu.ems.entities.Department;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;
	
	public List<Department> getAll(){
		return departmentDao.getAll();
	}
}
