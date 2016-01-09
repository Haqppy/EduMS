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

<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery.validate.js"></script>

<script type="text/javascript" src="script/messages_cn.js" ></script>
<script type="text/javascript" src="script/jquery.blockUI.js" ></script>

<script type="text/javascript">
	$(function(){
		
		//表单的 jQuery validate 验证
		//问题: 权限没有被 validate 验证
		$("#employeeForm").validate();
		
		//选取对应的父权限, 子权限给予显示
		$("select[name='parentAuthroities']").change(function(){
			//使所有的子权限都隐藏
			$("p[class^='authority_']").hide();
			
			//使当前权限的子权限被显示
			var id = $(this).val();
			if(id != ""){
				$(".authority_" + id).show();
			}
		});
		
		//级联选择的问题
		$(":checkbox[name='authorities2']").click(function(){
			var flag = $(this).attr("checked");
			
			if(flag){
				//定位到关联的 checkbox: 获取当前 checkbox 后面的隐藏域的值, 并把字符串解析为字符串数组, 使 value 值
				//等于字符串元素的那些 checkbox 被选中.
				var relaredStr = $(this).attr("class");
				if(relaredStr != ""){
					var relaredStrs = relaredStr.split(",");
					
					for(var i = 0; i < relaredStrs.length; i++){
						var rs = relaredStrs[i];
						if(rs != ""){
							$(":checkbox[value='" + rs + "']").attr("checked", true);
						}
					}
				}
			}else{
				//定位到哪些 checkbox 关联到当前取消选择的 checkbox: 判断哪些隐藏域中的 value 值包含当前的 value 值
				//注意使用,value,
				var val = $(this).val();
				var $checkbox = $(":checkbox[class*='," + val + ",']");
				
				$checkbox.each(function(){
					$(this).attr("checked", false);
				});
			}
		});
	})
</script>

</head>
<body>
	<br>
	<s:if test="id != null ">
		<s:url var="url" value="role-update"></s:url>
	</s:if>
	<s:else>
		<s:url var="url" value="role-save"></s:url>
	</s:else>
	
	<s:form action="%{#attr.url}" method="post" id="employeeForm" cssClass="employeeForm">
		<s:if test="roleId != null">
			<s:hidden name="roleId"></s:hidden>
		</s:if>
	
		<fieldset>
			<p>
				<label for="name">角色名(必填*)</label>
				<s:textfield name="roleName" id="name" cssClass="required"></s:textfield>
			</p>
			
			<p>
				<label for="authority">授予权限(必选)</label>
				<input type="checkbox" name="authorities2" class="required" style="display:none"/>
			</p>
			
			<p>
				<label>权限名称(必填)</label>
				<!-- 父权限 -->
				<s:select list="#request.parentAuthorities"
					headerKey="" headerValue="请选择..."
					listKey="id" listValue="displayName"
					name="parentAuthroities"></s:select>
			</p>
				
			<!-- 子权限以隐藏的 checkbox 的形式显示出来 -->
			<!--  
				1.使用 s:checkboxlist 标签, 修改了模版文件. 
				2.使用 s:checkbox 标签, 使用 JS 进行回显
				3.其他办法. 
			-->
			<s:iterator value="#request.parentAuthorities">
			
				<s:checkboxlist list="subAuthorities" listKey="id" listValue="displayName"
					name="authorities2" templateDir="ems/template" cssStyle="border:none"
					listCssClass="relatedAuthorites"></s:checkboxlist>
								
			</s:iterator>
			
			<p>
				<input class="submit" type="submit" value="Submit"/>
			</p>
			
		</fieldset>
		
	</s:form>

</body>
</html>