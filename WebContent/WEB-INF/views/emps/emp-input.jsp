<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/content.css">
<link rel="stylesheet" type="text/css" href="css/input.css">
<link rel="stylesheet" type="text/css" href="css/weebox.css">
 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/script/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/script/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/default.css">

<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery.validate.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/script/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath }/script/messages_cn.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/script/bgiframe.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/script/weebox.js"></script>
<script type="text/javascript">

	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	
	function myparser(s){
		if (!s) return new Date();
		var ss = (s.split('-'));
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	}

	$(function(){
		
		//jQuery 的 validate 验证
		$("#employeeForm").validate();
		
		//为了使 jQuery validate 验证适用于 radio, 并正常显示错误消息: 当选择一个 radio 时, 时其后面的隐藏域value赋值.
		$(":radio[name='enabled']").click(function(){
			//使隐藏域有值
			$(":hidden[name='enabled2']").val("enabled2");
			//使 label 自动消失. 
			$(":hidden[name='enabled2'] + label").remove();
		});
		
		//ajax 检验 loginname 是否可用. 
		$("#loginName").change(function(){
			var val = $(this).val();
			val = $.trim(val);
			
			if(val == "" || val.length < 6){
				return;
			}
			
			var oldLoginName = $(":hidden[name='oldLoginName']").val();
			if(val == oldLoginName){
				return;
			}
			
			var url = "emp-ajaxValidateLoginName";
			var args = {"loginName":val, "time":new Date()};
			
			
			
			$.post(url, args, function(data){
				if(data == "1"){
					alert("LoginName 可用.");
				}else{
					alert("LoginName 不可用.");
				}
			})
		});		
		
		//用于处理角色
		//weebox 的显示原理为: 把要先的内容剪切到指定的位置. 做完操作后再粘贴到原位置. 但所做的操作不会记录. 
		//希望所做的操作能够被记录. 
		//解决方案: 在页面上设置了 2 组的对应的 checkbox, 一组用于显示, 一组用于提交. 
		//当显示的被关闭时, 把选中的状态复制到提交的那一组, 当显示的被打开, 则把提交的状态复制到显示组
		$("#role_a_id").click(function(){
			$.weeboxs.open('#rolebox', {
				title:'选择角色',
				onopen:function() {
					//被隐藏的被选择的
					$(":checkbox[name='roles2']:checked").each(function(){
						var val = $(this).val();
						$(".rolebox_[value='" + val + "']").attr("checked", "checked");
					});
					
				},
				onok:function(box){
					//显示那一组已经被选择的状态复制到隐藏的, 被提交的里面
					$(".rolebox_").each(function(){
						var flag = $(this).is(":checked");
						var val = $(this).val();
						$(":checkbox[name='roles2'][value='" + val + "']").attr("checked", flag);
					});
					
					box.close();//增加事件方法后需手动关闭弹窗
				}
			});
			
			return false;
		});
	
	});
	
</script>
</head>

<body>
	
	<br>
	<s:form action="emp-save" method="post" cssClass="employeeForm" id="employeeForm">
		
		<fieldset>
		
			<p>
				<label for="message">
					<s:if test="id != null">
						<font color="red">编辑员工信息</font>
						<s:hidden name="id" value="%{employeeId}"></s:hidden>
						<s:hidden name="oldLoginName" value="%{loginName}"></s:hidden>
					</s:if>
					<s:else>
						<font color="red">添加员工信息</font>
					</s:else>
				</label> 
			</p>
			
			<p>
				<label for="loginName">登录名(必填)</label>
				<s:textfield name="loginName" id="loginName" cssClass="required" minLength="6"></s:textfield>
				
				<label id="loginlabel" class="error" for="loginname" generated="true">
					<!-- 显示服务器端简单验证的信息 -->
					<s:fielderror fieldName="loginName" />
				</label>
			</p>
			
			<p>
				<label for="employeeName">姓名 (必填)</label>
				<s:textfield name="employeeName" id="employeeName" cssClass="required"></s:textfield>
			</p>
			
			<p>
				<label for="logingallow">登录许可 (必填)</label>
				<s:radio list="#{'1':'允许','0':'禁止' }" name="enabled" cssStyle="border:none"></s:radio>
				<!-- 
				用于进行 jQuery 的 validate 验证. 若不使用 jQuery 会把验证消息放到第一个 radio 的后面, 导致页面不美观. 
				需注意在提交表单时, 该隐藏域可能包含的干扰. 
				-->
				<input type="radio" name="enabled" class="required" style="display:none"/>
			</p>

			<p>
				<label for="gender">性别 (必填)</label>
				<s:radio list="#{'1':'男','0':'女' }" name="gender" cssStyle="border:none"></s:radio>
			</p>
			
			<p>
				<label for="dept">部门 (必填)</label>
				<s:select list="#request.departments" name="department.departmentId"
					listKey="departmentId" listValue="departmentName" cssClass="required"></s:select>
				<label class="error" for="dept" generated="true">
					<font color="red">
					<!-- 显示服务器端简单验证的信息 -->
					</font>
				</label>
			</p>
			
			<p>
				<label for="birth">生日 (必填)</label>
				<s:textfield name="birth" cssClass="easyui-datebox" data-options="formatter:myformatter,parser:myparser"></s:textfield>
			</p>
			
			<p>
				<label for="email">Email (必填)</label>
				<s:textfield name="email" id="email" cssClass="required email"></s:textfield>
				<label class="error" for="email" generated="true">
					<!-- 显示服务器端简单验证的信息 -->
					<s:fielderror fieldName="email"/>
				</label>
			</p>
			
			<p>
				<label for="mobilePhone">电话 (必填)</label>
				<s:textfield name="mobilePhone"></s:textfield>
			</p>

			<p>
				<label for="role"><a id="role_a_id" href="">分配角色(必选)</a></label>
			</p>
			
			<div style="display:none">
				<!-- 用来提交的. 有 name 属性 -->
				<s:checkboxlist list="#request.roles" 
					name="roles2" id="roles" listKey="roleId" listValue="roleName" cssStyle="border:none" requiredLabel="false"></s:checkboxlist>
			</div>
			
			<div style="display:none" id="rolebox"> 
				<c:forEach items="${roles }" var="role">
					<!-- 没有 name 属性, 不会被提交, 仅用于显示 -->
					<input class="rolebox_" type="checkbox" value="${role.roleId }" style="border:none"/>${role.roleName }
					<br>
				</c:forEach>
			</div>
			
			<p>
				<label for="mobilePhone">创建人</label>
				
				<s:if test="id == null">
					${sessionScope.employee.loginName }
					<input type="hidden" value="${sessionScope.employee.employeeId }" name="editor.employeeId"/>
				</s:if>
				<s:else>
					${editor.loginName }
				</s:else>
			</p>

			<p>
				<input class="submit" type="submit" value="提交"/>
			</p>
				
		</fieldset>
			
	</s:form>

</body>
</html>