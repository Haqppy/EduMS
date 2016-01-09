package com.atguigu.ems.dao2;

import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Authority;
import com.atguigu.ems.hibernate.HibernateDao;

@Repository("authorityDao2")
public class AuthorityDao extends HibernateDao<Authority, Integer>{
	
}
