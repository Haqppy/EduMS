<%@page import="com.atguigu.ems.entities.Employee"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="java.util.*"%>
<%@page import="com.atguigu.ems.entities.Role"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<% 
		//准备要显示的 Role
		List roles = new ArrayList();
		Role role = null;
	
		role = new Role();
		role.setRoleId(1);
		role.setRoleName("AA");
		roles.add(role);
		
		role = new Role();
		role.setRoleId(2);
		role.setRoleName("BB");
		roles.add(role);
		
		role = new Role();
		role.setRoleId(3);
		role.setRoleName("CC");
		roles.add(role);
		
		request.setAttribute("roles", roles);
		
		//准备回显的!
		
	%>
	
	<s:debug></s:debug>
	
	<s:form action="test-checkboxlist" method="post">
		<s:checkboxlist 
			list="#attr.roles" 
			listKey="roleId" 
			listValue="roleName" 
			name="roles2"
			templateDir="ems/template">
		</s:checkboxlist>
		<s:submit></s:submit>
	</s:form>
	
</body>
</html>