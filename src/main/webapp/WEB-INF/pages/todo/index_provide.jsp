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
    <title>云端笔记-待办</title>
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
            <a class="mini-button" iconCls="icon-addnew" onclick="openEditWindowByAdd">增加</a>
            <span class="separator"></span>
            <a class="mini-button" iconCls="icon-edit" onclick="openEditWindowByUpdate">修改</a>
            <span class="separator"></span>
            <a class="mini-button" iconCls="icon-date" onclick="removeTodo">删除</a>
        </div>
    </div>

    <div title="日期树" showProxyText="true" region="west" width="160px" expanded="true" showSplitIcon="true">
        <ul id="dateTree" class="mini-tree" url="<%=basePath%>todo/getDateTree" ajaxType="post"
            style="width:100%;height:94%"
            showTreeIcon="true" textField="text" idField="id" parentField="parent" enableHotTrack="true"
            resultAsTree="false" onNodeClick="dateTreeClick" style="overflow:hidden;">
        </ul>
    </div>

    <div title="待办列表" region="center" expanded="true">
        <div id="todoTreeGrid" class="mini-treegrid" style="width:100%;height:100%;"
             url="<%=basePath%>todo/findAll" ajaxType="post" showTreeIcon="true"
             idField="id" parentField="parent.id" resultAsTree="false" treeColumn="content"
             emptyText="无数据" autoLoad="false" expandOnLoad="true">
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

<div id="editTodoWindow" class="mini-window" title="编辑待办事项" style="width:650px;"
     showModal="true" allowResize="false" allowDrag="true">
    <div id="editTodoForm" class="form" >
        <input class="mini-hidden" name="id"/>
        <input class="mini-hidden" name="parent.id" id="parentId"/>
        <table style="width:100%;">
            <tr>
                <td style="width:80px;">
                    父待办：
                </td>
                <td style="width:150px;">
                    <input id="parentContent" name="parent.content" class="mini-textbox" readonly/>
                </td>
                <td style="width:80px;">
                    状态：
                </td>
                <td style="width:150px;">
                    <input name="status" class="mini-combobox" url="<%=basePath%>todo/getStatus"/>
                </td>
            </tr>
            <tr>
                <td style="width:80px;">
                    开始日期：
                </td>
                <td style="width:150px;">
                    <input name="beginDateTime" class="mini-datepicker"/>
                </td>
                <td style="width:80px;">
                    结束日期：
                </td>
                <td style="width:150px;">
                    <input name="endDateTime" class="mini-datepicker"/>
                </td>
            </tr>
            <tr>
                <td>待办内容：</td>
                <td colspan="3" style="width:380px;">
                    <input name="content" class="mini-textbox" style="width:480px;"/>
                </td>
            </tr>
            <tr>
                <td style="text-align:center;padding-top:5px;padding-right:20px;" colspan="4">
                    <a class="mini-button" iconCls="icon-addnew" style="align:center" onclick="saveTodo">确定</a>
                    <span class="separator"></span>
                    <a class="mini-button" iconCls="icon-cancel" onclick="closeEditTodoWindow">取消</a>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>
