<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/content.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/list.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/script/thickbox/thickbox.css">

<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/script/thickbox/thickbox.js"></script>

<script type="text/javascript">
	
	$(function(){
		
		//删除的需求: 
		//1. confirm: 确定是要删除 xxx 的信息吗 ?
		//2. ajax 检查要删除的员工是否为某一个 Department 的 manager. 若是, 则提示: xxx 是 Manager, 不能删除
		//3. 若不是 manager, 则发送 ajax 请求, 把对应的记录的 IS_DELETED 设置为 1, 页面上删除对应的超链接
		$(".emp-delete").click(function(){
			var $td = $(this).parent("td");
			var $a = $(this);
			
			var id = $(this).prev(":hidden").val();
			var loginName = $(this).next(":hidden").val();
			var flag = confirm("确定要删除" + loginName + "的信息吗?");
			
			if(flag){
				var url = "emp-ajaxValidateManager";
				var args = {"id":id,"time":new Date()};
				
				$.post(url, args, function(data){
					if(data == "1"){
						alert(loginName + "是 Manager, 不能删除");
					}else{
						$a.prev(":hidden").remove();
						$a.next(":hidden").remove();
						$a.remove();
						
						$td.html($td.html() + "删除");
						$("#deleted-" + id).text("删除");
						
						alert("删除成功");
					}
				});
			}
			
			return false;
		});
		
		//为站到多少页添加 js 代码
		$(".logintxt").change(function(){
			var val = $(this).val();
			val = $.trim(val);
			
			//校验输入的 val 是否为一个数值型的字符串
			var reg = /^\d*$/gi;
			if(!reg.test(val)){
				alert("输入的页码不合法");
				$(this).val("");
				return;
			}
			
			if(val < 1 || val > "${page.totalPages}"){
				alert("输入的页码不合法");
				$(this).val("");
				return;
			}
			
			window.location.href = "test-emp-list?page.pageNo=" + val;
		});
		
		//排序的 JS 代码
		$("#loginName,#email").click(function(){
			var propertyName = this.id;
			var oldPropertyName = $("#orderBy").val();

			var order = $("#order").val();
			if(order == "" || propertyName != oldPropertyName){
				order = "asc";
			}else{
				if(order == "asc"){
					order = "desc";
				}else{
					order = "asc";
				}
			}
			
			$("#orderBy").val(propertyName);
			$("#order").val(order);
			
			var url = "test-emp-list?page.pageNo=${page.prePage }&" + $(":hidden").serialize();
			alert(url);
			
			window.location.href = url;
			
			return false;
		});
		
		//点击分页连接携带排序字符串
		$(".pagelinks a").click(function(){
			var href = this.href;
			alert($(":hidden").serialize());
			
			href = href + "&" + $(":hidden").serialize(); 
			window.location.href = href;
			return false;
		});
		
		$("#criteria").attr("href", $("#criteria").attr("href") + "&" + $(":hidden").serialize());
	})

</script>

</head>
<body>
	<input type="hidden" id="order" name="page.order" value="${param['page.order']}"/>
	<input type="hidden" id="orderBy" name="page.orderBy" value="${param['page.orderBy'] }"/>
	
	<input type="hidden" name="filter_LIKES_loginName" value="${param.filter_LIKES_loginName }"/>
	<input type="hidden" name="filter_LIKES_employeeName" value="${param.filter_LIKES_employeeName }"/>
	
	<input type="hidden" name="filter_EQS_gender" value="${param.filter_EQS_gender }"/>
	<input type="hidden" name="filter_EQI_enabled" value="${param.filter_EQI_enabled }"/>
	
	<input type="hidden" name="filter_EQI_department.departmentId" value="${param['filter_EQI_department.departmentId'] }"/>
	<input type="hidden" name="filter_LIKES_email" value="${param.filter_LIKES_email }"/>
		
	<br><br>
	<center>
		<br><br>
		
		<a id="criteria" href="${pageContext.request.contextPath }/test-emp-criteriaInput?height=300&width=320&time=new Date()"  class="thickbox"> 
	   		增加(显示当前)查询条件	   		
		</a> 
		
		<a href="test-emp-list?page.pageNo=0" id="delete-query-condition">
		   	删除查询条件
		</a>
		
		<span class="pagebanner">
			共 ${page.totalCount } 条记录
			&nbsp;&nbsp;
			共 ${page.totalPages } 页
			&nbsp;&nbsp;
			当前第 ${page.pageNo } 页
		</span>
		
		<span class="pagelinks">
			<s:if test="page.pageNo != 1">
				[
				<a href="test-emp-list?page.pageNo=0">首页</a>
				/
				<a href="test-emp-list?page.pageNo=${page.prePage }">上一页</a>
				] 	
			</s:if>
			<span id="pagelist">
				转到 <input type="text" name="pageNo" size="1" height="1" class="logintxt"/> 页
			</span>
			<s:if test="page.pageNo != page.totalPages">
				[
				<a href="test-emp-list?page.pageNo=${page.nextPage }">下一页</a>
				/
				<a href="test-emp-list?page.pageNo=${page.totalPages }">末页</a>
				] 
			</s:if>
		</span>
		
		<table>
			<thead>
				<tr>
					<td><a id="loginName" href="">登录名</a></td> 
					<td>姓名</td>
					
					<td>登录许可</td>
					<td>部门</td>
					
					<td>生日</td>
					<td>性别</td>
					
					<td><a id="email" href="">E-Mail</a></td>
					<td>手机</td>
					
					<td>登录次数</td>
					<td>删除</td>
					<td>角色</td>
					<security:authorize ifAnyGranted="ROLE_EMP_DELETE,ROLE_EMP_UPDATE">
						<td>操作</td>
					</security:authorize>
				</tr>
			</thead>
			
			<tbody>
				<s:iterator value="page.result">
					<tr>
						<td><a id="loginname" href="">${loginName }</a></td> 
						<td>${employeeName }</td>
						
						<td>${enabled == 1 ? '允许' : '禁止' }</td>
						<td>${department.departmentName }</td>
						
						<td>
							<s:date name="birth" format="yyyy-MM-dd"/>
						</td>
						<td>${gender == 1 ? '男' : '女' }</td>
						
						<td><a id="email" href="">${email }</a></td>
						<td>${mobilePhone }</td>
						
						<td>${visitedTimes }</td>
						<td id="deleted-${employeeId }">${isDeleted == 1 ? '删除' : '正常' }</td>
						
						<td>${roleNames }</td>
						<td>
							<security:authorize ifAllGranted="ROLE_EMP_UPDATE">
								<a href="emp-input?id=${employeeId }">修改</a>
							</security:authorize>
							&nbsp;
							<security:authorize ifAllGranted="ROLE_EMP_DELETE">
								<s:if test="isDeleted == 1">
									删除
								</s:if>
								<s:else>
									<input type="hidden" value="${employeeId }"/>
										<a class="emp-delete" href="emp-delete?id=${employeeId }">删除</a>
										<input type="hidden" value="${loginName }"/>
								</s:else>
							</security:authorize>
						</td>
					</tr>
				</s:iterator>	
			</tbody>
		</table>
		
		<a href="${pageContext.request.contextPath }/emp-downToExcel.action?time=<%= new java.util.Date() %>">下载到 Excel 中</a>
		&nbsp;&nbsp;&nbsp;&nbsp;
		
	</center>
</body>
</html>