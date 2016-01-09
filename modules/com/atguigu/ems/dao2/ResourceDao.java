package com.atguigu.ems.dao2;

import org.springframework.stereotype.Repository;

import com.atguigu.ems.entities.Resource;
import com.atguigu.ems.hibernate.HibernateDao;

@Repository("resourceDao2")
public class ResourceDao extends HibernateDao<Resource, Integer>{
	
}
