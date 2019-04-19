<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<html>
<head>
    <base href="<%=basePath%>">
    <title>曼妙云端笔记-搜索明细</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

    <link href="<%=basePath%>css/baidusearch/searchresult/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/baidusearch/searchresult/common.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/baidusearch/searchresult/article_detail.css"/>

    <script src="<%=basePath%>js/baidusearch/searchresult/jquery.min.js"></script>
    <script src="<%=basePath%>js/baidusearch/searchresult/bootstrap.min.js"></script>
    <script src="<%=basePath%>js/baidusearch/searchresult/common.js"></script>

<body>
<div class="w_container">
    <div class="container">
        <div class="row w_main_row">
            <ol class="breadcrumb w_breadcrumb">
                <span class="w_navbar_tip">我们长路漫漫，只因学无止境。</span>
            </ol>

            <div class="col-lg-9 col-md-9 w_main_left_detail">
                <div class="panel panel-default">
                    <div class="panel-body" style="margin: 0 auto">
                        <h2 class="c_titile">${article.title}</h2>
                        <p class="box_c">
                            <span class="d_time">发布时间：${article.createTime}</span>
                            <span>编辑：${article.user.name}</span>
                            <span>阅读（${article.hitNum}）</span>
                        </p>
                        <ul class="infos">${article.content}</ul>
                        <!--<div class="nextinfo">
                            <p class="last">上一篇：<a href="#">免费收录网站搜索引擎登录口大全</a></p>
                            <p class="next">下一篇：<a href="#">javascript显示年月日时间代码</a></p>
                        </div>-->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="w_foot">
    <div class="w_foot_copyright">
        Copyright &copy; 2017-2020, www.travelmonk123.com. All Rights Reserved.
    </div>
</div>
<!--toTop-->
<div id="shape">
    <div class="shapeColor">
        <div class="shapeFly">
        </div>
    </div>
</div>
</body>
</html>
