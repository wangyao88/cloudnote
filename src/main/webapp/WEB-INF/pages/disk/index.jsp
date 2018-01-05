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
<title>曼妙云端笔记|云盘</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css" />
<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/disk/disk.js" type="text/javascript"></script>
<script type="text/javascript">
   var basePath = "<%=basePath%>";
</script>
</head>
<body>
	<div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
	     <div title="工具栏" region="north" showHeader="false" height="34px">
			<div class="mini-toolbar" height="33px">
			    <a class="mini-button" iconCls="icon-undo" onclick="spider">上一级</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-reload" onclick="reload">刷新</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-redo" onclick="addArticle">下一级</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-addnew" onclick="removeAll">新建文件夹</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-remove" onclick="removeArticle">删除</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-edit" onclick="removeAll">重命名</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-edit" onclick="removeAll">上传</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-edit" onclick="removeAll">下载</a>
		    </div>
		 </div>
		<div title="菜单" region="west" width="200px" expanded="true">
			<ul id="menuTree" class="mini-tree" url="<%=basePath%>disk/getTree" ajaxType="post" style="width:100%;height:100%;" 
                   showTreeIcon="true" textField="text" idField="id" resultAsTree="false" expandOnLoad="true" onNodeClick="onNodeClick1" >
            </ul>
        </div>
        <div title="文件列表" region="center">
		    <div id="fileContainer" style="margin-left:10px"></div>
		</div>
	</div>
</body>
</html>