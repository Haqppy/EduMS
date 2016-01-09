<%@page import="java.util.Enumeration"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>欢迎使用培训中心管理系统</title>

<link rel="stylesheet" type="text/css" href="css/login.css">
<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript">

	/*
	* 前端基于 JS 的验证.
	* 
	* ①: loginName 和 password 字段除去前后空格不能为空
	* ②: loginName 和 password 字段除去前后空格, 不能少于 6 个字符
	* ③: loginName 中不能包含特殊字符, 即以字母开头, 后边还可以包含数字和_ 
	*
	* 其中前两个验证都通过, 再验证 ③. 
	*/
	/*
	$(function(){
		
		//1. 在点击提交按钮时进行验证, 所以为 .submit 添加 click 响应函数.
		$(".submit").click(function(){
			
			//3. 获取 loginName 和 password. 同时完成 ①. ② 验证
			//获取不是提交按钮的所有的 input. 
			//alert($("input:not(:submit)").length);
			
			var flag = true;
			
			$("input:not(:submit)").each(function(){
				//获取当前的 value 值
				var val = $(this).val();
				//去除前后空格
				val = $.trim(val);
				//使文本框中的字符串去除前后空格
				$(this).val(val);
				
				//获取 label
				var label = $(this).prev("b").text();
				
				if(val == ""){
					alert(label + "不能为空!");
					flag = false;
				}else{
					if(val.length < 6){
						alert(label + "不能少于6个字符");
						flag = false;
					}
				}
			});
			
			if(flag){
				//获取 loginName 的文本值
				var loginName = $("input[name='loginName']").val();
				loginName = $.trim(loginName);
				
				//alert(loginName);
				
				//loginName 中不能包含特殊字符, 即以字母开头, 后边还可以包含数字和_ 
				var reg = /^[a-zA-Z]+\w+$/gi;
				if(!reg.test(loginName)){
					alert("输入的用户名不合法:用户名只能以字母开头, 后边可以包含数字和_");
				}
			}
			
			//2. 取消默认行为
			return false;
		});
		
	});
	*/
	
</script>
</head>

<body> 
	<div align="center">
		<s:form action="emp-login" method="post">
			<div class="login_div" align="center">
			
				<font color="red">
					<s:text name="%{exception.class.name}"></s:text>
				</font>
				
				<b>用户名</b>
				<s:textfield name="loginName"></s:textfield>
				<font color="red">
					<s:fielderror fieldName="loginName"></s:fielderror>
				</font>

				<b>密码</b>
				<s:password name="password"></s:password>
				<font color="red">
					<s:fielderror fieldName="password"></s:fielderror>
				</font>
				
				<input type="submit" class="submit" value="登录" />
				
			</div>		
		</s:form>
	</div>	
	
</body>
</html>