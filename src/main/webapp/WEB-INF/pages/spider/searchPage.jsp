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
	
	<link rel="stylesheet" href="<%=basePath%>css/search/bt3.css" />
	<link rel="stylesheet" href="<%=basePath%>css/search/app.css" />
	<link rel="stylesheet" href="<%=basePath%>css/search/page-fast-news-60c1b0fb04288316f8b14bbbcc409bff.css" />
	<link rel="stylesheet" href="<%=basePath%>css/search/application-febb10a4ab803df13138ef8cf968c276.css" />
	<link rel="stylesheet" href="<%=basePath%>css/search/style.css" />
	<link rel="stylesheet" href="<%=basePath%>css/buttons.css" />
	<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/spider/search.js"></script>
  </head>

<body style="background:#fff;">
	<center>
		<div class="container paperCut" style="margin-top: 25px;">
				<div class="row">
					<div class="col-md-9">
						<div class="box" style="width: 100%;border: 0px;height: 50px;">
							<div style="width: 80%;float: left;height: 33px;">
								<input id="searchKey"
									style=" width:100%;border: 2px #008DF2  solid;padding: 10px;"
									placeholder=" 搜索你感兴趣的文章" />
							</div>
							<a class="button button-glow button-rounded button-raised button-primary" hreaf="javascript:void(0);" onclick="search()">搜索</a>
						</div>
					</div>
				</div>
		</div>
	</center>
</body>
</html>
