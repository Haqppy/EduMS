package com.atguigu.ems.dao2;

import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Role;
import com.atguigu.ems.hibernate.HibernateDao;

@Repository("roleDao2")
public class RoleDao extends HibernateDao<Role, Integer>{
	
}
