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
<div class="w_header">
    <div class="container">
        <div class="w_header_top">
            <span class="w_header_nav">
					<ul>
                        <%--<li><a href="" class="active">首页</a></li>--%>
                    </ul>
				</span>
            <div class="w_search">
                <div class="w_searchbox">
                    <input type="text" placeholder="请输入您要搜索的内容" />
                    <button>搜索</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="w_container">
    <div class="container">
        <div class="row w_main_row">
            <div class="col-lg-9 col-md-9 w_main_left">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">${words} -> 搜索结果</h3>
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

            <div class="col-lg-3 col-md-3 w_main_right">
                <div class="panel panel-default sitetip">
                    <a href="javascript:void(0);">
                        <strong>搜索统计信息</strong>
                        <h3 class="title">搜索成功</h3>
                        <p id="total" class="overView">搜索数量：0</p>
                        <p id="count" class="overView">命中数量：0</p>
                        <p id="rate" class="overView">命&nbsp;&nbsp;中&nbsp;&nbsp;率：0</p>
                        <p id="pageNum" class="overView">本页数量：0</p>
                        <p id="cost" class="overView">搜索耗时：0ms</p>
                    </a>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">热门关键字</h3>
                    </div>
                    <div class="panel-body">
                        <div id="labelList" class="labelList">

                        </div>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">文章推荐</h3>
                    </div>
                    <div class="panel-body">
                        <ul id="recommendArticleList" class="list-unstyled sidebar">

                        </ul>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">今日新闻</h3>
                    </div>
                    <div class="panel-body">
                        <ul id="newsList" class="list-unstyled sidebar">

                        </ul>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">友情链接</h3>
                    </div>
                    <div class="panel-body">
                        <div class="newContent">
                            <ul class="list-unstyled sidebar shiplink">
                                <li>
                                    <a href="https://www.baidu.com/" target="_blank">百度</a>
                                </li>
                                <li>
                                    <a href="https://www.oschina.net/" target="_blank">开源中国</a>
                                </li>
                                <li>
                                    <a href="https://www.csdn.net/" target="_blank">csdn</a>
                                </li>
                                <li>
                                    <a href="https://github.com/" target="_blank">github</a>
                                </li>
                            </ul>
                        </div>
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
        var pageAmount = 10;
        var dataTotal = ${count};
        var num = Math.floor(dataTotal/pageAmount);
        var pageTotal = dataTotal % pageAmount == 0 ? num : num+1;
        new Page({
            id: 'pagination',
            pageTotal: pageTotal, //必填,总页数
            pageAmount: pageAmount,  //每页多少条
            dataTotal: dataTotal, //总共多少条数据
            curPage: 1, //初始页码,不填默认为1
            pageSize: 5, //分页个数,不填默认为5
            showPageTotalFlag: true, //是否显示数据统计,不填默认不显示
            showSkipInputFlag: true, //是否支持跳转,不填默认不显示
            getPage: function (page) {
                getPageData(0, page, pageAmount, ${count});
            }
        })
    }

    function getPageData(first, page, size, count) {
        $.ajax({
            url : basePATH+"search/page",
            type : "get",
            data : {
                words : "${words}",
                first : first,
                page : page-1,
                size : size,
                count : count
            },
            success : function(data){
                if(first == 1) {
                    $("#total").html("搜索数量："+data.total);
                    $("#count").html("命中数量："+data.count);
                    $("#rate").html("命&nbsp;&nbsp;中&nbsp;&nbsp;率："+data.rate);
                }
                $("#pageNum").html("本页数量："+data.pageNum);
                $("#cost").html("搜索耗时："+data.cost+"ms");
                var articles = data.articles;
                if(articles.length == 0) {
                    $("#contentList").html("未找到搜索数据");
                    return;
                }
                var content = "";
                $(articles).each(function(index, article) {
                    content += "<div class='panel panel-default'>"+
                        "<div class='panel-body'>"+
                        "<h4><a target='_blank' class='title' href='<%=basePath%>article/detail?id="+article.id+"'>"+article.title+"</a></h4>"+
                        "<p class='overView'>"+article.content+"</p>"+
                        "</div>"+
                        "</div>";
                });
                $("#contentList").html(content);
                $("html, body").animate({ scrollTop: 0 }, 120);
            },
            error : function(data) {
                $("#contentList").html("暂无数据");
                $("html, body").animate({ scrollTop: 0 }, 120);
            }
        });
    }

    function getHotLabels() {
        $.ajax({
            url : basePATH+"search/hotLabel",
            type : "get",
            success : function(labels){
                if(labels.length == 0) {
                    $("#labelList").html("暂无数据");
                    return;
                }
                var content = "";
                $(labels).each(function(index, label) {
                    content += "<a class='label label-default'>"+label+"</a>";
                });
                $("#labelList").html(content);
            },
            error : function(data) {
                $("#labelList").html("暂无数据");
            }
        });
    }

    function getRecommendArticles() {
        $.ajax({
            url : basePATH+"search/recommendArticles",
            type : "get",
            success : function(articles){
                if(articles.length == 0) {
                    $("#recommendArticleList").html("暂无数据");
                    return;
                }
                var content = "";
                $(articles).each(function(index, article) {
                    content += "<li><a target='_blank' href='"+article.url+"'>"+article.title+"</a></li>";
                });
                $("#recommendArticleList").html(content);
            },
            error : function(data) {
                $("#recommendArticleList").html("暂无数据");
            }
        });
    }

    function getTodayNews() {
        $.ajax({
            url : basePATH+"search/todayNews",
            type : "get",
            success : function(newss){
                if(newss.length == 0) {
                    $("#newsList").html("暂无数据");
                    return;
                }
                var content = "";
                $(newss).each(function(index, news) {
                    content += "<li><a target='_blank' href='"+news.url+"'>"+news.title+"</a></li>";
                });
                $("#newsList").html(content);
            },
            error : function(data) {
                $("#newsList").html("暂无数据");
            }
        });
    }

    $(document).ready(function(){
        getPageData(1, 1, 10, ${count});
        getHotLabels();
        getRecommendArticles();
        getTodayNews();
    });
</script>
</html>
