<%@ page language="java"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>曼妙云端笔记-QuickText</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

    <link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>css/a.css"/>
    <link rel="stylesheet" href="<%=basePath%>css/scroller.css"/>
    <link rel="stylesheet" href="<%=basePath%>js/ueditor-1.4.3.3/third-party/SyntaxHighlighter/shCoreDefault.css"/>
    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>
    <script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/ueditor-1.4.3.3/ueditor.config.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/ueditor-1.4.3.3/ueditor.all.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/quickText/quickText.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/ueditor-1.4.3.3/third-party/SyntaxHighlighter/shCore.js" type="text/javascript"></script>
    <script type="text/javascript">
        SyntaxHighlighter.all();
    </script>
</head>
<body>
<div id="layout1" class="mini-layout" style="width:100%;height:100%;" borderStyle="border:solid 1px #aaa;">
    <div title="工具栏" region="north" showHeader="false" height="34px">
        <div class="mini-toolbar" height="33px">
            <a class="mini-button" iconCls="icon-addnew" onclick="newQuickText">增加</a>
            <span class="separator"></span>
            <a class="mini-button" iconCls="icon-remove" onclick="removeQuickText">删除</a>
        </div>
    </div>

    <div title="QuickText列表" showProxyText="true" region="west" width="395px" expanded="true" showSplitIcon="true">
        <div id="quickTextGrid" class="mini-datagrid" style="width:395px;height:100%;"
             url="<%=basePath%>quickText/findAll" idField="id" allowResize="false"
             showPager="false" ajaxType="post" multiSelect="false" onRowClick="gridRowClick"
             emptyText="无数据" autoLoad="false">
            <div property="columns">
                <div type="indexcolumn">序号</div>
                <div name="idColumn" field="id" headerAlign="center" allowSort="true" width="0px">ID</div>
                <div name="title" field="title" headerAlign="center" allowSort="true" width="300px">标题</div>
            </div>
        </div>
    </div>

    <div title="笔记详情" region="center">
        <div id="articleContainerSimple"
             name="articleContainerSimple"
             style="width:97%;margin:5px 0px 0px 10px;overflow-x:hidden;display:none;"></div>
        <textarea id="quickTextContainer"
                  name="quickTextContainer"
                  style="width:100%;height:90%;overflow-x:hidden;overflow-y:hidden;"></textarea>
    </div>

</div>
</body>
</html>
<script type="text/javascript">
    mini.parse();
    var editor = UE.getEditor('quickTextContainer', {
        'enterTag': 'br'
    });
</script>