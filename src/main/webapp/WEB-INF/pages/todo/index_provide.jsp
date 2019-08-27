<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>曼妙云端笔记-待办</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

    <link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/todo/todo.js" type="text/javascript"></script>
</head>
<body id="globalKeyEvent">
<div id="layout1" class="mini-layout" style="width:100%;height:100%;" borderStyle="border:solid 1px #aaa;">
    <div title="工具栏" region="north" showHeader="false" height="34px">
        <div class="mini-toolbar" height="33px">
            <a class="mini-button" iconCls="icon-addnew" onclick="addTodo">增加</a>
            <span class="separator"></span>
            <a class="mini-button" iconCls="icon-edit" onclick="editTodo">修改</a>
            <span class="separator"></span>
            <a class="mini-button" iconCls="icon-date" onclick="removeTodo">删除</a>
            <span class="separator"></span>
            <input class="mini-textbox" width="200px" id="searchArticleByTitleOrContentText" emptyText="搜索待办"/>
            <a class="mini-button" id="searchTodo" iconCls="icon-search" onclick="searchTodo">查询</a>
        </div>
    </div>

    <div title="日期树" showProxyText="true" region="west" width="400px" expanded="true" showSplitIcon="true">
        <ul id="menuTree" class="mini-tree" url="<%=basePath%>todo/getDateTree" ajaxType="post"
            style="width:100%;height:94%"
            showTreeIcon="true" textField="text" idField="id" parentField="parent" enableHotTrack="true"
            resultAsTree="false" onNodeClick="onNodeClick1" style="overflow:hidden;">
        </ul>
    </div>

    <div title="待办列表" region="center" expanded="true">
        <div id="todoTreeGrid" class="mini-treegrid" style="width:100%;height:100%;"
             url="<%=basePath%>todo/findAll" showTreeIcon="true" treeColumn="todoColumns"
             idField="id" parentField="parent" resultAsTree="false"
             emptyText="无数据" autoLoad="false" expandOnLoad="false" onNodeClick="todoTreeGridClick">
            <div property="columns">
                <div type="indexcolumn">序号</div>
                <div name="idColumn" field="id" headerAlign="center" width="0px">ID</div>
                <div name="content" field="content" headerAlign="center" width="300px">待办</div>
                <div name="beginDateTime" field="beginDateTime" headerAlign="center">开始时间</div>
                <div name="endDateTime" field="endDateTime" headerAlign="center">结束时间</div>
                <div name="status" field="status" headerAlign="center">状态</div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
