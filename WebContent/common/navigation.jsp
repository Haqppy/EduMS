<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" media="screen"
	href="${pageContext.request.contextPath }/css/global.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/content.css">

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/script/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/script/themes/icon.css">


<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/script/jquery.easyui.min.js"></script>

<script type="text/javascript">
	
	$(function(){
		$("#navigate").tree({
		    onClick: function(node){
		    	if(node.url != null){
		    		alert(node.url);
		    		if(node.url != "/EduMS/security-logout"){
				    	window.parent.document.getElementById("content").src = node.url;
		    		}else{
				    	window.parent.parent.document.location = node.url;		    			
		    		}
		    	}
		    }
		});
	})
	
</script>
	
</head>
<body>
	
	<br>
	<table cellpadding=0 cellspacing=0>
      <tr>
      	<td>
      		&nbsp;&nbsp;&nbsp;
      	</td>
        <td>
			<ul id="navigate" class="easyui-tree" data-options="url:'tree_data1.json?a=5',method:'get',animate:true,dnd:true"></ul>
        </td>
      </tr>
    </table>
	
</body>
</html>