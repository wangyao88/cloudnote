<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>曼妙云端笔记|在线搜索文章</title>
    <meta charset="utf-8" />
    <meta content="user-scalable=no,width=device-width, initial-scale=1" name="viewport">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="在线搜索文章">
	
	<link rel="stylesheet" href="<%=basePath%>css/search/default.css" />
	<link rel="stylesheet" href="<%=basePath%>css/search/search-form.css" />
	<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery.autocomplete-min.js"></script>
	<script src="<%=basePath%>js/layer/layer.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath%>js/spider/search.js"></script>
	<script type="text/javascript">
	    var basePATH = "<%=basePath%>";
	</script>
  </head>

<body>
	<div class="search-wrapper" style="top:200px;">
		<div class="input-holder">
			<input id="searchText" type="text" class="search-input" placeholder="请输入关键词" />
			<button class="search-icon" onClick="searchToggle(this, event);"><span></span></button>
		</div>
		<span class="close" onClick="searchToggle(this, event);"></span>
	</div>
</body>
</html>
