package com.atguigu.ems.security.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.atguigu.ems.dao.EmployeeDao;
import com.atguigu.ems.entities.Authority;
import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.entities.Role;
import com.atguigu.ems.security.SecurityUser;

@Service("userDetailsService")
public class SecurityUserDetailsService implements UserDetailsService{

	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Employee employee = employeeDao.getByLoginName(username);
		
		if(employee == null){
			throw new UsernameNotFoundException("用户不存在!");
		}
		
		String password = employee.getPassword();
		boolean enabled = (employee.getEnabled() == 1);
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
		Collection<GrantedAuthorityImpl> authorities = new HashSet<>();
		for(Role role: employee.getRoles()){
			for(Authority authority: role.getAuthorities()){
				authorities.add(new GrantedAuthorityImpl(authority.getName()));
			}
		}
		
		SecurityUser user = new SecurityUser(username, password, enabled, 
				accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		user.setEmployee(employee);
		
		return user;
	}

}
