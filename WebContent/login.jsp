<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎使用培训中心管理系统</title>

<link rel="stylesheet" type="text/css" href="css/login.css">
<script type="text/javascript" src="script/jquery-1.3.1.js"></script>
<script type="text/javascript">

	/*
	* ①: loginname 和 loginpassword 字段除去前后空格不能为空
	* ②: loginname 和 loginpassword 字段除去前后空格, 不能少于 6 个字符
	* ③: loginname 中不能包含特殊字符, 即以字母开头, 后边还可以包含数字和_   
	*/
	$(function(){
		$(":submit").click(function(){
			
			var flag = true;
			
			//获取 loginname 和 loginpasswords
			$("input:not(:submit)").each(function(){
				var val = $(this).val();
				val = $.trim(val);
				$(this).val(val);
				
				var label = $(this).prev("b").text();
				if(val == ""){
					alert(label + "不能为空");
					flag = false;
				}else{
					if(val.length < 6){
						alert(label + "不能少于6个字符");
						flag = false;
					}else{
						var name = $(this).attr("name");
						if(name == "loginname"){
							var reg = /^[a-zA-Z]\w*\w$/gi;
							if(!reg.test(val)){
								alert(label + "不能包含特殊字符.");
								flag = false;
							}
						}
					}
				}
				
			});
			
			return flag;
		});
	})

</script>
</head>

<body> 
	<div align="center">
		<form action="security-login" method="post">
			<div class="login_div" align="center">
				<font color="red">
					<!-- 显示错误消息 -->
					<c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.class.simpleName == 'DisabledException' }">
						您被禁止登录, 请联系管理员
					</c:if>
					<c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.class.simpleName == 'BadCredentialsException' }">
						用户名不存在或用户名和密码不匹配
					</c:if>
				</font>
				<b>用户名</b>
				<input type="text" name="username" value="${sessionScope.SPRING_SECURITY_LAST_USERNAME }"/>
				<font color="red"></font>
				<b>密码</b>
				<input type="password" name="password"/>
				<font color="red"></font>
				<input type="submit" class="submit" value="登录" />
			</div>		
		</form>
	</div>
</body>
</html>

<%

	Enumeration names = session.getAttributeNames();
	while(names.hasMoreElements()){
		Object name = names.nextElement();
		System.out.println(name + ": " + session.getAttribute((String)name));
	}

%>