package com.atguigu.ems.struts2.actions2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.atguigu.ems.entities.Employee;
import com.atguigu.ems.service.DepartmentService;
import com.atguigu.ems.utils.EmployeeCrieriaFormBean;
import com.opensymphony.xwork2.ActionContext;

@Scope("prototype")
@Controller("employeeAction2")
public class EmployeeAction extends BaseAction<Employee, Integer>{

	@Autowired
	private DepartmentService departmentService;
	
	public String list(){
		buildPage();
		return "success";
	}
	
	//显示查询条件的页面
	public String criteriaInput(){
		System.out.println(ActionContext.getContext().getValueStack().peek());
		request.put("departments", departmentService.getAll());
		return "success";
	}
	          //prepareCriteriaInput
	public void prepareCriteriaInput(){
		ActionContext.getContext().getValueStack().push(new EmployeeCrieriaFormBean());
	}
		
}
