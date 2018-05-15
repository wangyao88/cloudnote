<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>爱记账-首页</title>
    
    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css" />
	<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css" />
	
	<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
	<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
	<script src="<%=basePath%>js/accountsystem/main/main.js" type="text/javascript"></script>
	<script type="text/javascript">
	   var basePath = "<%=basePath%>";
	</script>
  </head>
  <body>
    <div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
		 <div title="菜单" showProxyText="true" region="west" width="200px" expanded="true" showSplitIcon="true">
			<ul id="menuTree" class="mini-tree" style="width:100%;height:94%" showTreeIcon="true" textField="text" 
				idField="id" resultAsTree="false"expandOnLoad="true" onNodeClick="menuClick">
            </ul>
		</div>
		<div title="" region="center">
		    <div id="tabs" class="mini-tabs" activeIndex="0" style="width:100%;height:100%;"
			    arrowPosition="side" showNavMenu="true">
			    <div title="首页">
			    </div>
			</div>
		</div>
	</div>
  </body>
</html>
