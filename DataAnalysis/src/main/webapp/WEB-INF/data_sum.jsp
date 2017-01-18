<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'data_sum.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<style type="text/css">
		.boder-right{
			border-right-color: red;
		}
		.bg-color{
			background-color: green;
		}
	</style>
  </head>
  
  <body>
   	<table cellspacing="0" cellpadding="3" border="1">
   		<tr>
   			<td rowspan="2">期数</td>
   			<c:forEach items="${numbers}" var="num">
   				<td width="10px">${num}</td>
   			</c:forEach>
   			<td rowspan="2">总和</td>
   		</tr>
   		<tr id="area">
   			<c:forEach items="${numbers}" var="num" varStatus="status">
   				<c:if test="${status.index % 7 == 0}">
	   				<td colspan="7" align="center" class="boder-right">${status.index / 7 + 1}区</td>
   				</c:if>
   			</c:forEach>
   		</tr>
   		
   		<!-- 循环期数数据  -->
   		<c:forEach items="${data}" var="jdd">
   			<tr>
	   			<td>${jdd.id}</td>	
	   			<c:forEach items="${numbers}" var="num" varStatus="status">
	   				<td class="${(status.index+1) % 7 == 0 ? 'boder-right' : ''}"
	   					flag="${num == jdd.one || num == jdd.two || num == jdd.three || num == jdd.four || num == jdd.five || num == jdd.six? 'num' : ''}"
	   				    lang="${num == jdd.one || num == jdd.two || num == jdd.three || num == jdd.four || num == jdd.five || num == jdd.six? num : ''}">
	   					${num == jdd.one || num == jdd.two || num == jdd.three || num == jdd.four || num == jdd.five || num == jdd.six? num : ''}
	   				</td>
	   			</c:forEach>
	   			<td>${jdd.one + jdd.two + jdd.three + jdd.four + jdd.five + jdd.six}</td>
   			</tr>
   		</c:forEach>
   	</table>
   	
   	<script type="text/javascript">
   		$(function(){
   			var tds = $("td[flag='num']");
   			$.each(tds,function(index,td){
	   			var val = $(td).attr("lang");
	   			var val1 = val/7;
	   			var val2 = val%7;
	   			if(val1 < 1 || (val1==1 && val2 == 0)) {
	   				val = 1;
	   			}else if(val1 < 2 || (val1==2 && val2 == 0)){
	   				val = 2 ;
	   			}else if(val1 < 3 || (val1==3 && val2 == 0)){
	   				val =3;
	   			}else if(val1 <4 || (val1==4 && val2 == 0)){
	   				val =4;
	   			}else if(val1 < 5 || (val1==5 && val2 == 0)){
	   				val =5
	   			}else if(val1 < 6 || (val1==6 && val2 == 0)){
	   				val = 6;
	   			}else{
	   				val =7;
	   			}
	   			var childs = $(td).parent().children();
	   			val = val * 7;
	   			for(var i = val - 6;i<=val ;i++){
	   				$(childs[i]).addClass("bg-color");
	   			}
   			});
   		});
   	</script>
  </body>
</html>
