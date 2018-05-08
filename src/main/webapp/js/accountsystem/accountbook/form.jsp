<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>爱记账-新增账本</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css" />
	
	<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
	<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
	<script type="text/javascript">
	   var basePath = "<%=basePath%>";
	</script>
	<script src="<%=basePath%>js/accountsystem/accountbook/accountbook.js" type="text/javascript"></script>
  </head>

<body>
	<table>
		<tr>
			<td><label for="textbox1$text">名称：</label></td>
			<td><input id="name" name="name" class="mini-textbox" required="true" /></td>
		</tr>
		<tr>
			<td><label for="textarea1$text">备注：</label></td>
			<td><input id="mark" name="mark" class="mini-textarea" required="false" /></td>
		</tr>
		<tr>
            <td></td>
            <td><input value="保存" type="button" onclick="addAccountBook()" /></td>
        </tr>
	</table>
</body>
</html>
