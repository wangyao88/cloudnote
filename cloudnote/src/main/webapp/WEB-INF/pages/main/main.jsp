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
<title>曼妙云端笔记首页</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">

<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet"
	type="text/css" />
<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/main/main.js" type="text/javascript"></script> 
<script type="text/javascript">
   var basePath = "<%=basePath%>";
</script>
</head>
<body>
	<div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
		<div title="笔记和标签" showProxyText="true" region="west" width="200px" expanded="true" showSplitIcon="true">
			<ul id="menuTree" class="mini-tree" url="<%=basePath%>main/getTree" ajaxType="post" style="width:100%;height:100%;" 
                    showTreeIcon="true" textField="text" idField="id" resultAsTree="false"   contextMenu="#treeMenu"
                    onNodeClick="onNodeClick1" >        
            </ul>
            <ul id="treeMenu" class="mini-contextmenu"  onbeforeopen="onBeforeOpen">        
			    <li class="separator"/>
				<li name="add" iconCls="icon-add" onclick="onAddNode">新增节点</li>
				<li class="separator"/>
				<li name="edit" iconCls="icon-edit" onclick="onEditNode">编辑节点</li>
				<li class="separator"/>
				<li name="remove" iconCls="icon-remove" onclick="onRemoveNode">删除节点</li>
				<li class="separator"/>     
			</ul>
		</div>
		<div title="笔记详情" region="center">
			
		</div>
		<div title="笔记列表" region="east" width="400px" expanded="true">
			
		</div>
	</div>