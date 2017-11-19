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

<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet"
	type="text/css" />
<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/note/note.js" type="text/javascript"></script>
<script src="<%=basePath%>js/flag/flag.js" type="text/javascript"></script>
<script src="<%=basePath%>js/article/article.js" type="text/javascript"></script> 
<script src="<%=basePath%>js/main/main.js" type="text/javascript"></script>
<script type="text/javascript">
   var basePath = "<%=basePath%>";
</script>
</head>
<body id="globalKeyEvent">
	<div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
	     <div title="工具栏" region="north" showHeader="false" height="28px">
			<div class="mini-toolbar">
			    <a class="mini-button" iconCls="icon-addnew" onclick="addArticle">增加</a>
			    <a class="mini-button" iconCls="icon-edit" onclick="editArticle">修改</a>
			    <a class="mini-button" iconCls="icon-remove" onclick="removeArticle">删除</a>
			    <span class="separator"></span>
			    <input class="mini-textbox" id="searchArticleText" emptyText="搜索笔记标题"/>   
			    <a class="mini-button" plain="true" id="searchArticleBtn" onclick="searchArticle">查询</a>
		    </div>
		 </div>
		 <div title="笔记和标签" showProxyText="true" region="west" width="200px" expanded="true" showSplitIcon="true">
			<ul id="menuTree" class="mini-tree" url="<%=basePath%>main/getTree" ajaxType="post" style="width:100%;height:100%;" 
                    showTreeIcon="true" textField="text" idField="id" resultAsTree="false"   contextMenu="#treeMenu"
                    onNodeClick="onNodeClick1" >        
            </ul>
            <ul id="treeMenu" class="mini-contextmenu"  onbeforeopen="onBeforeOpen">        
			    <li class="separator"/>
				<li name="add" iconCls="icon-add" onclick="onAddNode">新增子节点</li>
				<li class="separator"/>
				<li name="edit" iconCls="icon-edit" onclick="onEditNode">编辑节点</li>
				<li class="separator"/>
				<li name="remove" iconCls="icon-remove" onclick="onRemoveNode">删除节点</li>
				<li class="separator"/>     
			</ul>
		</div>
		<div title="笔记详情" region="center" id="globalKeyEvent">
		    <div id="articleContainer"></div>
		</div>
		<div title="笔记列表" region="east" width="400px" expanded="true">
				<div id="articleGrid" class="mini-datagrid" style="width:395px;height:100%;" 
			        url="<%=basePath%>article/getAllArticles" idField="id" allowResize="false" 
			        pageSize="23" sizeList="[23,50,100]" ajaxType="post" multiSelect="false" onRowClick="gridRowClick"
			        onrowdblclick="gridRowdblclick">
	            	<div property="columns">
		            <div type="indexcolumn">序号</div>
		            <div name="idColumn"  field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
		            <div name="title"  field="title" headerAlign="center" allowSort="true" width="310px" >标题</div>
		            <div field="hitNum" width="53px" allowSort="true" >阅读次数</div>            
	            </div>
        </div>
		</div>
	</div>
	</body>
</html>