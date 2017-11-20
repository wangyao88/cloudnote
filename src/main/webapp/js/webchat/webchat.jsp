<%@ page language="java" 
import="java.util.*"
import="com.sxkl.cloudnote.user.entity.User" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
    User user = (User)session.getAttribute("user");
    String uid = user.getId();
    String name = user.getName();
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
<link href="<%=basePath%>js/webchat/webchat.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var from='<%=uid%>';
var fromName='<%=name%>';
</script>
<script src="<%=basePath%>js/webchat/webchat.js" type="text/javascript"></script>
</head>
<body>
	<div id="content"></div>
	<input type="text" placeholder="请输入要发送的信息" id="msg" class="msg" onkeydown="send(event)">
	<input type="button" value="发送" class="send" onclick="sendMsg()" >
	<input type="button" value="清空" class="clear" onclick="clearAll()">
</body>
</html>
