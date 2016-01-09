<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/content.css">
<link rel="stylesheet" type="text/css" href="css/input.css">
</head>
<body>
	<br>
	
	<form action="" class="employeeForm" id="employeeForm">
	
		<fieldset>
			<p>
				<label for="message"><font color="red">角色的详细信息</font></label> 
			</p>
			<p>
				<label for="loginname">角色名</label>
				${roleName }
			</p>
			
			<% 
				Set parentAuthorities = new HashSet();
				request.setAttribute("parentAuthorities", parentAuthorities);
			%>
			
			<s:iterator value="authorities">
				<% 
					parentAuthorities.add(request.getAttribute("parentAuthority"));
				%>
			</s:iterator>
			
			<s:iterator value="#attr.parentAuthorities" var="pa">
				<p>
					<label for="message">${displayName }</label> 
				</p>
				
				<s:iterator value="authorities">
					<s:if test="parentAuthority==#pa">
						<p>
							<label for="message">&nbsp;</label>
							${displayName } 
						</p>
					</s:if>
				</s:iterator>
				
			</s:iterator>
		
		</fieldset>
		
	</form>
</body>
</html>