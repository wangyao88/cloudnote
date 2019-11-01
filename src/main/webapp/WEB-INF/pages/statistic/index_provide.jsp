<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>曼妙云端云笔记统计平台</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" href="<%=basePath%>css/statistic/base.css">

    <script type="text/javascript">
        var basePath = "<%=basePath%>";
    </script>

</head>
<body>
<!--顶部-->
<header class="header left">
    <div class="header_center">
        <h2><strong>曼妙云端云笔记统计平台</strong></h2>
    </div>
</header>
<!--内容部分-->
<div class="con left">
    <!--数据总概-->
    <div class="con_div">
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="<%=basePath%>images/statistic/info_1.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>笔记本数量</p>
                    <p id="noteNum"></p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="<%=basePath%>images/statistic/info_2.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>标签数量</p>
                    <p id="flagNum"></p>
                </div>
            </div>
        </div>
        <div class="con_div_text left">
            <div class="con_div_text01 left">
                <img src="<%=basePath%>images/statistic/info_4.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>笔记总数量</p>
                    <p class="sky" id="articleNum"></p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="<%=basePath%>images/statistic/info_5.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>博客总数量</p>
                    <p class="sky" id="blogNum"></p>
                </div>
            </div>
        </div>
        <div class="con_div_text left">

            <div class="con_div_text01 left">
                <img src="<%=basePath%>images/statistic/info_6.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>今天记录笔记数量</p>
                    <p class="org" id="todayArticleNum"></p>
                </div>
            </div>
            <div class="con_div_text01 right">
                <img src="<%=basePath%>images/statistic/info_7.png" class="left text01_img"/>
                <div class="left text01_div">
                    <p>操作日志数量</p>
                    <p class="org" id="logNum"></p>
                </div>
            </div>
        </div>
    </div>
    <!--统计分析图-->
    <div class="div_any">
        <div class="left div_any01">
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_1.png">笔记本关联笔记统计 </div>
                <p id="char1" class="p_chart"></p>
            </div>
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_2.png">季度日志状态统计 </div>
                <p id="char2" class="p_chart"></p>
            </div>
        </div>
        <div class="div_any02 left ">
            <div class="div_any_child div_height">
                <div class="div_any_title any_title_width"><img src="<%=basePath%>images/statistic/title_3.png">实时日志监控 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>编号</th>
                            <th>级别</th>
                            <th>操作</th>
                            <th>耗时</th>
                            <th>IP</th>
                            <th>日期</th>
                        </tr>
                        </thead>
                        <tbody id="logDataTable">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="right div_any01">
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_4.png">todo月度统计 </div>
                <p id="char3" class="p_chart"></p>
            </div>
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_5.png">笔记数量月度统计 </div>
                <p id="char4" class="p_chart"></p>
            </div>
        </div>
    </div>
    <!--分析表格-->
    <div class="div_table">
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_4.png">笔记浏览量排名前5位 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>编号</th>
                            <th>标题</th>
                            <th>浏览量</th>
                        </tr>
                        </thead>
                        <tbody id="hitDataTable">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_4.png">最新添加笔记前5位 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>编号</th>
                            <th>标题</th>
                            <th>添加日期</th>
                        </tr>
                        </thead>
                        <tbody id="recentDataTable">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_4.png">flag拥有文章数前5位 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>排名</th>
                            <th>标签名称</th>
                            <th>数量</th>
                        </tr>
                        </thead>
                        <tbody id="flagDataTable">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="left div_table_box">
            <div class="div_any_child">
                <div class="div_any_title"><img src="<%=basePath%>images/statistic/title_4.png">搜索关键字前5位 </div>
                <div class="table_p">
                    <table>
                        <thead><tr>
                            <th>编号</th>
                            <th>关键字</th>
                            <th>数量</th>
                        </tr>
                        </thead>
                        <tbody id="searchDataTable">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="<%=basePath%>js/jquery.min.js"></script>
<script src="<%=basePath%>js/statistic/echarts-all.js"></script>
<script src="<%=basePath%>js/statistic/base.js"></script>
<script src="<%=basePath%>js/statistic/index.js"></script>
</body>
</html>
