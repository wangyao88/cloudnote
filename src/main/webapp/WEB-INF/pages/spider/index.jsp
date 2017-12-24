<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>曼妙云端笔记</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/spider/spider.js" type="text/javascript"></script>
<script type="text/javascript">
   var basePath = "<%=basePath%>";
</script>
</head>
<body>
	<div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
	     <div title="工具栏" region="north" showHeader="false" height="34px">
			<div class="mini-toolbar" height="33px">
			    <a class="mini-button" iconCls="icon-search" onclick="spider">抓取</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-reload" onclick="reload">刷新</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-addnew" onclick="addArticle">入库</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-remove" onclick="removeArticle">删除</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-remove" onclick="removeAll">清空</a>
		    </div>
		 </div>
		<div title="文章列表" region="west" width="400px" expanded="true">
				<div id="articleGrid" class="mini-datagrid" style="width:395px;height:100%;" 
			        url="<%=basePath%>spider/getAll" idField="id" allowResize="false" showPager="false"
			        ajaxType="post" multiSelect="false" onRowClick="gridRowClick">
	            	<div property="columns">
		            <div type="indexcolumn">序号</div>
		            <div field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
		            <div field="title" headerAlign="center" allowSort="true" width="300px" >标题</div>
	            </div>
	            </div>
        </div>
        <div title="文章内容" region="center">
		    <div id="articleContainer" style="margin-left:10px"></div>
		</div>
	</div>
</body>
</html>