package com.atguigu.ems.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.ems.dao.RoleDao;
import com.atguigu.ems.entities.Role;

@Service
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	public Role get(Integer roleId) {
		return roleDao.getWithAuthorites(roleId);
	}
	
	public void delete(Integer id){
		roleDao.delete(id);
	}
	
	public void save(Role role){
		roleDao.save(role);
	}
	
	public List<Role> getAllWithDisplayName(){
		List<Role> roles = roleDao.getAll();
		for(Role role: roles){
			String displayName = role.getDisplayName();
		}
		return roles;
	}
	
	public List<Role> getAll(){
		return roleDao.getAll();
	}

	
}
