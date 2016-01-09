package com.atguigu.ems.test;

import java.util.ArrayList;
import java.util.HashSet;

import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.entities.Role;
import com.opensymphony.xwork2.ActionContext;

public class RoleListAction {
	
	public String test(){
		Employee employee = new Employee();
		ArrayList roles = new ArrayList();
		
		Role role = new Role();
		role.setRoleId(1);
		roles.add(role);
		
		role = new Role();
		role.setRoleId(2);
		roles.add(role);
		
		employee.setRoles(new HashSet(roles));
		
		ActionContext.getContext().getValueStack().push(employee);
		return "success";
	}
	
}
