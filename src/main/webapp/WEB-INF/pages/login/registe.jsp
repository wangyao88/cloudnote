<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>曼妙云端笔记|注册</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="description" content="This is my page">
	<link href="<%=basePath%>css/registe/style.css" rel='stylesheet' type='text/css' />
	<script type="text/javascript">
	    var basePATH = "<%=basePath%>";
	</script>
    <script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/login/registe.js" type="text/javascript"></script>
    <script src="<%=basePath%>js/layer/layer.js" type="text/javascript"></script>
  </head>
  <body>
    <h1>曼妙云端笔记</h1>
<div class="login-form">
	<div class="head-info">
		<label class="lbl-1"> </label>
		<label class="lbl-2"> </label>
		<label class="lbl-3"> </label>
	</div>
	<div class="clear"> </div>
		<input type="text" class="text" id="name" name="name" value="昵称" onFocus="onNameFocus()" onBlur="onNameBlur()" >
		<input type="text" class="text" id="password" name="password" value="密码" onFocus="onPasswordFocus()" onBlur="onPasswordBlur()" >
		<input type="text" class="text" id="repassword" name="repassword" value="确认密码" onFocus="onRepasswordFocus()" onBlur="onRepasswordBlur()" >		
	<div class="signin"><input type="submit" value="注册" onclick="submit()"></div>
</div>
  </body>
</html>
