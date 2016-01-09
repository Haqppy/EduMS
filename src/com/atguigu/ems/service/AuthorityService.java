package com.atguigu.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.ems.dao.AuthorityDao;
import com.atguigu.ems.entities.Authority;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityDao authorityDao;
	
	public List<Authority> getParentAuthorities(){
		return authorityDao.getParentAuthorities();
	}
	
}
