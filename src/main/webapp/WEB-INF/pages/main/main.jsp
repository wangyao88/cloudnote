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
<script src="<%=basePath%>js/user/user.js" type="text/javascript"></script>
<script src="<%=basePath%>js/webchat/webchat.js" type="text/javascript"></script>
<script src="<%=basePath%>js/statChart/statChart.js" type="text/javascript"></script>
<script src="<%=basePath%>js/main/main.js" type="text/javascript"></script>
<script src="<%=basePath%>js/backup/backup.js" type="text/javascript"></script>
<script src="<%=basePath%>js/waitingtask/waitingtask.js" type="text/javascript"></script>
<script type="text/javascript">
   var basePath = "<%=basePath%>";
</script>
</head>
<body id="globalKeyEvent">
	<div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
	     <div title="工具栏" region="north" showHeader="false" height="34px">
			<div class="mini-toolbar" height="33px">
			    <a class="mini-button" iconCls="icon-addnew" onclick="addArticle">增加</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-edit" onclick="editArticle">修改</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-remove" onclick="removeArticle">删除</a>
			    <span class="separator"></span>
			    <input class="mini-textbox" id="searchArticleByTitleText" emptyText="笔记标题"/>   
			    <a class="mini-button" plain="true" id="searchArticleByTitleBtn" onclick="searchArticleByTitle">查询</a>
			    <span class="separator"></span>
			    <input class="mini-textbox" width="400px" id="searchArticleByTitleOrContentText" emptyText="笔记标题或内容，以逗号分隔"/>   
			    <a class="mini-button" plain="true" id="searchArticleByTitleOrContentBtn" onclick="searchArticleByTitleOrContentText">搜索</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-help" href="<%=basePath%>spider/searchPage" target="_blank">在线搜索</a>
		    </div>
		 </div>
		 <div title="south" region="south" showSplit="false" showHeader="false" height="32px" showSplitIcon="false" >
        		<div class="mini-toolbar">
			    <a class="mini-button" iconCls="icon-user" onclick="openWebchatPage">在线聊天</a>
			    <!-- <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-date" onclick="openStatChart">报表</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-edit" onclick="">账号管理</a> -->
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-date" href="<%=basePath%>disk/index" target="_blank">云盘</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-undo" href="<%=basePath%>spider/index" target="_blank">订阅文章</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-search" onclick="backupDB()">备份数据</a>
			    <!-- <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-goto" onclick="">发送邮件</a> -->
			    <!-- <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-goto" onclick="onLineNum">在线用户数</a> -->
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-goto" onclick="openwaitingtask">待办任务</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-goto" onclick="logout">退出</a>
			    <span class="separator"></span>
		    </div>
    	</div>
		 <div title="笔记和标签" showProxyText="true" region="west" width="300px" expanded="true" showSplitIcon="true">
			<ul id="menuTree" class="mini-tree" url="<%=basePath%>main/getTree" ajaxType="post" style="width:100%;height:100%;" 
                    showTreeIcon="true" textField="text" idField="id" resultAsTree="false"   contextMenu="#treeMenu"
				    expandOnLoad="true" onNodeClick="onNodeClick1" >
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
		<div title="笔记详情" region="center">
		    <div id="articleContainer" style="margin-left:10px"></div>
		</div>
		<div title="笔记列表" region="east" width="400px" expanded="true">
				<div id="articleGrid" class="mini-datagrid" style="width:395px;height:100%;" 
			        url="<%=basePath%>article/getAllArticles" idField="id" allowResize="false" 
			        pageSize="20" sizeList="[20,50,100]" ajaxType="post" multiSelect="false" onRowClick="gridRowClick"
			        onrowdblclick="gridRowdblclick">
	            	<div property="columns">
		            <div type="indexcolumn">序号</div>
		            <div name="idColumn"  field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
		            <div name="title"  field="title" headerAlign="center" allowSort="true" width="300px" >标题</div>
		            <div field="hitNum" width="60px" allowSort="true" >阅读次数</div>            
	            </div>
        </div>
	</div>
</body>
</html>