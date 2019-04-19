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
  <title>曼妙云端笔记-搜索</title>

  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">

<style>
* {
	margin: 0;
	padding: 0;
}
body {
	overflow: hidden;
}
</style>
<link href="<%=basePath%>css/baidusearch/font.css" rel="stylesheet">
<link rel="stylesheet" href="<%=basePath%>css/baidusearch/style.css">
<link rel="stylesheet" href="<%=basePath%>css/baidusearch/style-search.css" media="screen" type="text/css" />

<script src="<%=basePath%>js/baidusearch/jquery-1.9.1.min.js"></script>
<script>
	function search(){
        words = $("#input").val();
        if(words == "") {
            location.reload();
        }else {
            window.open("<%=basePath%>search/result?words="+words+"&page=0&size=10");
            location.reload();
        }
	}

    $(document).ready(function(){
        $('#input').keydown(function(event) {
            if (event.keyCode == 13) {
                search();
            }
        });
    });
</script>
</head>
  <body>
  <canvas id="canvas" width="1280" height="1024"> 您的浏览器不支持canvas标签，请您更换浏览器 </canvas>
  <script src="<%=basePath%>js/baidusearch/word.js"></script>
  <p id="offscreen-text" class="offscreen-text"></p>
  <p id="text" class="text"></p>
  <!--<svg id="svg" > </svg>-->

  <div id="d" class="webdesigntuts-workshop">
    <span>
      <input class="input" id="input" type="search" placeholder="请输入您要搜索的内容"/>
      <button onclick="search();">搜一下</button>
    </span>
  </div>



  <script  src='<%=basePath%>js/baidusearch/index.js'></script>
  </body>
</html>
