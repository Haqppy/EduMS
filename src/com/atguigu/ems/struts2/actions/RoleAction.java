package com.atguigu.ems.struts2.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.atguigu.ems.entities.Role;
import com.atguigu.ems.service.AuthorityService;
import com.atguigu.ems.service.RoleService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * SELECT count(e.employeeId) 
 * FROM Employee e
 * INNER OUTER JOIN e.roles r
 * WHERE r.roleId = ?
 * 
 *
 */

@Scope("prototype")
@Controller
public class RoleAction 
	extends ActionSupport
	implements RequestAware, ModelDriven<Role>, Preparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private RoleService roleService;
	
	private InputStream inputStream;
	
	public InputStream getInputStream() {
		return inputStream;
	}
	
	private Integer roleId;
	
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	public Integer getRoleId() {
		return roleId;
	}
	
	public String details(){
		return "details";
	}
	
	public void prepareDetails(){
		model = roleService.get(roleId);
	}
	
	public String delete(){
		try {
			roleService.delete(roleId);
			inputStream = new ByteArrayInputStream("1".getBytes());
		} catch (Exception e) {
			if(e.getCause() instanceof ConstraintViolationException){
				inputStream = new ByteArrayInputStream("0".getBytes());				
			}else{
				inputStream = new ByteArrayInputStream("-1".getBytes());								
			}
		}
		
		return "ajax-success";
	}
	
	public String list(){
		request.put("roles", roleService.getAllWithDisplayName());
		return "list";
	}
	
	public String save(){
		this.roleService.save(model);
		return "save-success";
	}
	
	public void prepareSave(){
		model = new Role();
	}
	
	public String input(){
		request.put("parentAuthorities", authorityService.getParentAuthorities());
		return "input";
	}
	
	public void prepareInput(){
		if(roleId != null){
			model = roleService.get(roleId);
		}
	}

	private Map<String, Object> request;
	
	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.request = arg0;
	}

	@Override
	public void prepare() throws Exception {}

	private Role model;
	
	@Override
	public Role getModel() {
		return model;
	}
	
}
