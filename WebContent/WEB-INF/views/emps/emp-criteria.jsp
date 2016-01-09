<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
#employeeForm { width: 300px; margin-left: 25px }
</style>
<script type="text/javascript"></script>

</head>
<body>
	<s:form action="emp-list" cssClass="employeeForm"
		id="employeeForm">
		
		<input type="hidden" value="0" name="pageNo"/>
		<br>
		查询条件
		<table border="0" cellpadding="3" cellspacing="3" style="margin:0 auto;" >
		  <tr>
		    <td><label>登录名</label>: </td>
		    <td>
		    	<s:textfield name="filter_LIKE_S_loginname" id="filter_LIKES_loginname"></s:textfield>
		    </td>
		  </tr>
		  <tr>
		    <td><label>姓名</label>: </td>
		    <td>
		    	<s:textfield name="filter_LIKE_S_employeename" id="employeename"></s:textfield>
		    </td>
		  </tr>
		  <tr>
		    <td><label>性别</label>: </td>
		    <td>
		    	<s:select list="#{'1':'男','0':'女' }" name="filter_EQ_S_gender"
		    		id="gender" headerKey="" headerValue="不限"></s:select>
		    </td>
		  </tr>
		  <tr>
		    <td><label>登录许可</label>: </td>
		    <td>
		    	<s:select list="#{'1':'是','0':'否' }" name="filter_EQ_S_logingallow"
		    		id="logingallow" headerKey="" headerValue="不限"></s:select>
		    </td>
		  </tr> 
		  <tr>
		    <td><label>部门</label>: </td>
		    <td>
		    	<s:select list="#request.departments" listKey="departmentId"
		    		listValue="departmentName" name="filter_EQ_I_deptid"
		    		cssClass="required" id="dept" headerKey="" headerValue="请选择"></s:select>
		    </td>
		  </tr>
		  <tr>
		    <td><label>Email</label>: </td>
		    <td><s:textfield name="filter_LIKE_S_email" id="email"></s:textfield></td>
		  </tr>
		  
		  <tr align="right">
		    <td colspan="2">
		    	<input class="submit" type="submit" value="提交"/>
		    </td>
		  </tr>
		  
		</table>
		
	</s:form>
	
</body>
</html>