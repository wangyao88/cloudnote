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
    <title>曼妙云端笔记-搜索结果</title>

    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

    <link href="<%=basePath%>css/baidusearch/searchresult/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/baidusearch/searchresult/common.css" />
    <link rel="stylesheet" href="<%=basePath%>plugins/myPagination/myPagination.css">

    <script src="<%=basePath%>js/baidusearch/searchresult/jquery.min.js"></script>
    <script src="<%=basePath%>js/baidusearch/searchresult/bootstrap.min.js"></script>
    <script src="<%=basePath%>plugins/myPagination/myPagination.js"></script>
<body>
<div class="w_container">
    <div class="container">
        <div class="row w_main_row">
            <div class="col-lg-9 col-md-9 w_main_left">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">搜索结果</h3>
                    </div>
                    <div class="panel-body">
                        <!--文章列表开始-->
                        <div id="contentList" class="contentList">
                            <c:forEach items="${articles}" var="article" varStatus="id">
                                <div class="panel panel-default">
                                    <div class="panel-body">

                                        <h4><a target="_blank" class="title" href="<%=basePath%>article/detail?id=${article.id}">${article.title}</a></h4>
                                        <%--<p>
                                            <a class="label label-default">UUID</a>
                                            <a class="label label-default">java</a>
                                        </p>--%>
                                        <p class="overView">${article.content}</p>
                                        <%--<p>
                                            <span class="count">
                                                <i class="glyphicon glyphicon-user"></i>
                                                <a href="#">admin</a>
                                            </span>
                                        <span class="count">
                                                <i class="glyphicon glyphicon-eye-open"></i>阅读:1003
                                            </span>
                                        <span class="count">
                                                <i class="glyphicon glyphicon-comment"></i>评论:2
                                            </span>
                                        <span class="count">
                                                <i class="glyphicon glyphicon-time"></i>2017-01-16
                                            </span>
                                        </p>--%>
                                    </div>
                                </div>
                                <%--<div class="panel panel-default">
                                    <div class="panel-body">
                                        <div class="contentleft">
                                            <h4><a class="title" href="article_detail.html">${article.title}</a></h4>
                                                <p>
                                                    <a class="label label-default">Nginx</a>
                                                    <a class="label label-default">tomcat负载均衡</a>
                                                </p>
                                            <p class="overView">${article.content}</p>
                                                <p>
                                                    <span class="count">
                                                        <i class="glyphicon glyphicon-user"></i>
                                                        <a href="#">admin</a>
                                                    </span>
                                                    <span class="count">
                                                        <i class="glyphicon glyphicon-eye-open"></i>阅读:1003
                                                    </span>
                                                    <span class="count">
                                                        <i class="glyphicon glyphicon-comment"></i>评论:2
                                                    </span>
                                                    <span class="count">
                                                        <i class="glyphicon glyphicon-time"></i>2017-01-16
                                                    </span>
                                                </p>
                                        </div>
                                            <div class="contentImage">
                                                <div class="row">
                                                    <a href="#" class="thumbnail w_thumbnail">
                                                        <img src="img/slider/Aj6bieY.jpg" alt="...">
                                                    </a>
                                                </div>
                                            </div>
                                    </div>
                                </div>--%>
                            </c:forEach>
                        </div>
                        <!--文章列表结束-->
                        <div id="pagination" class="pagination"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="w_foot">
    <div class="w_foot_copyright">Copyright &copy; 2017-2020, www.travelmonk123.com. All Rights Reserved. <span>|</span>
        <a target="_blank" href="http://www.miitbeian.gov.cn/" rel="nofollow">皖ICP备17002922号</a>
    </div>
</div>
</body>
<script type="text/javascript">
    var basePATH = "<%=basePath%>";

    var $backToTopEle = $('<a href="javascript:void(0)" class="Hui-iconfont toTop" title="返回顶部" alt="返回顶部" style="display:none">^</a>').appendTo($("body")).click(function() {
        $("html, body").animate({ scrollTop: 0 }, 120);
    });
    var backToTopFun = function() {
        var st = $(document).scrollTop(),
            winh = $(window).height();
        (st > 0) ? $backToTopEle.show(): $backToTopEle.hide();
        /*IE6下的定位*/
        if(!window.XMLHttpRequest) {
            $backToTopEle.css("top", st + winh - 166);
        }
    };

    $(function() {
        $(window).on("scroll", backToTopFun);
        backToTopFun();
    });

    window.onload = function () {
        var pageAmount = 20;
        var dataTotal = ${count};
        var num = Math.floor(dataTotal/pageAmount);
        var pageTotal = dataTotal % pageAmount == 0 ? num : num+1;
        new Page({
            id: 'pagination',
            pageTotal: pageTotal, //必填,总页数
            pageAmount: 20,  //每页多少条
            dataTotal: dataTotal, //总共多少条数据
            curPage: 1, //初始页码,不填默认为1
            pageSize: 5, //分页个数,不填默认为5
            showPageTotalFlag: true, //是否显示数据统计,不填默认不显示
            showSkipInputFlag: true, //是否支持跳转,不填默认不显示
            getPage: function (page) {
                getPageData(page, pageAmount)

            }
        })
    }

    function getPageData(page, size) {
        $.ajax({
            url : basePATH+"search/page",
            type : "get",
            data : {
                words : "${words}",
                page : page-1,
                size : size
            },
            success : function(data){
                console.log(data);
                var content = "";
                $(data).each(function(index, article) {
                    content += "<div class='panel panel-default'>"+
                                    "<div class='panel-body'>"+
                                    "<h4><a target='_blank' class='title' href='<%=basePath%>article/detail?id="+article.id+"'>"+article.title+"</a></h4>"+
                                    "<p class='overView'>"+article.content+"</p>"+
                                    "</div>"+
                               "</div>";
                });
                $("#contentList").html(content);
            },
            error : function(data) {
                mini.alert("查询数据失败，请稍候重试！");
                $("#contentList").html("暂无数据");
            }
        });
    }
</script>
</html>
