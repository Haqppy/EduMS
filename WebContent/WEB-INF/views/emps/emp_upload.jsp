<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
	    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet"  type="text/css" href="css/content.css">
	<link rel="stylesheet" type="text/css" href="css/list.css">
</head>
<body>
	<br><br>
	<center>
		<br><br>
		<a href="emp-uploadTemplateDownload.action">下载模板</a><br><br>
		
		<font color="red">
			<!-- 显示上传时的错误消息: 例如部门是否存在. 输入的性别是否合法 -->
			<s:actionerror/>
		</font>
		<br><br>
		
		<form action="emp-upload" enctype="multipart/form-data" method="post">
			<input type="file" name="file" />
			<input type="submit" value="上传"/>
		</form>
	</center>
</body>
</html>