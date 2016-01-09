<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" type="text/css" href="css/content.css">
	<link rel="stylesheet" type="text/css" href="css/list.css">
	
	<script type="text/javascript" src="script/jquery-1.3.1.js"></script>
	<script type="text/javascript">
		$(function(){
			//删除角色的 JS 代码, 若某一个角色被一个 Employee 引用, 则提示该角色不能被删除
			$(".role-delete").click(function(){
				
				var url = $(this).attr("href");
				var args = {"time":new Date()};
				
				var $tr = $(this).parent("td").parent("tr");
				
				$.post(url, args, function(data){
					if(data == "1"){
						$tr.remove();
						alert("删除成功!");
					}else if(data == "0"){
						alert("角色被引用, 无法删除！");
					}else{
						alert("删除失败！");						
					}
				})
				
				return false;
			});
			
		});
	</script>
</head>
<body>
	<br><br>
	<center>
		<table>
			<thead>
				<tr>
					<th>角色名称</th>
					<th>授予的权限</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="#request.roles">
					<tr>
						<td>
							<a href="role-details?roleId=${roleId }">${roleName }</a>						
						</td>
						<td>${displayName }</td>
						
						<td>
						<a href="role-delete?roleId=${roleId }" id="${roleId }" name="${roleName }" class="role-delete">删除</a>
						<a href="role-input?roleId=${roleId }">修改</a>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</center>
</body>
</html>