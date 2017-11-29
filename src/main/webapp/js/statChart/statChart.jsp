<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>报表</title>
<link href="<%=basePath%>js/miniui/themes/default/large-mode.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css"
	rel="stylesheet" type="text/css" />
<script src="<%=basePath%>js/jquery.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/echarts/echarts-3.8.4.js"
	type="text/javascript"></script>
<script src="<%=basePath%>js/statChart/statChart.js"
	type="text/javascript"></script>

<style type="text/css">
body, html {
	height: 100%;
	width: 100%;
	margin: 0;
	padding: 0;
}

.container {
	padding: 30px;
}

.container .mini-panel {
	margin-right: 10px;
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="mini-clearfix ">
			<div class="mini-col-6">

				<div class="mini-panel" title="月笔记记录量走势" width="auto"
					showCollapseButton="false" showCloseButton="false">
					 <div id="monthArticleColumn" style="width: 400px;height:300px;"></div>
				</div>
			</div>
			
			
			<div class="mini-col-6">

				<div class="mini-panel mini-panel-primary" title="面板标题" width="auto"
					showCollapseButton="true" showCloseButton="true">
					面板内容</div>

			</div>
		</div>
		<div class="mini-clearfix ">
			<div class="mini-col-6">

				<div class="mini-panel mini-panel-success" title="面板标题" width="auto"
					showCollapseButton="true" showCloseButton="true">
					面板内容</div>

			</div>
			<div class="mini-col-6">

				<div class="mini-panel mini-panel-info" title="面板标题" width="auto"
					showCollapseButton="true" showCloseButton="true">
					面板内容</div>

			</div>
		</div>
		<div class="mini-clearfix ">
			<div class="mini-col-6">

				<div class="mini-panel mini-panel-warning" title="面板标题" width="auto"
					showCollapseButton="true" showCloseButton="true">
					面板内容</div>

			</div>
			<div class="mini-col-6">

				<div class="mini-panel mini-panel-danger" title="面板标题" width="auto"
					showCollapseButton="true" showCloseButton="true">
					面板内容</div>

			</div>
		</div>
	</div>

</body>
</html>