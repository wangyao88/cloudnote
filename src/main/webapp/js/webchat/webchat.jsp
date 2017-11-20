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
<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
<link href="<%=basePath%>js/webchat/webchat.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var from='<%=uid%>';
var fromName='<%=name%>';
</script>
<script src="<%=basePath%>js/webchat/webchat.js" type="text/javascript"></script>
</head>
<body>
	<div id="webchatLayout" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;" >
		 <div title="好友列表" showProxyText="true" region="west" width="200px" expanded="true" showSplitIcon="false" showCollapseButton="false">
			<ul id="userTree" class="mini-tree" url="<%=basePath%>main/getTree" ajaxType="post" style="width:100%;height:100%;" 
                    showTreeIcon="true" textField="text" idField="id" resultAsTree="false"   contextMenu="#treeMenu"
                    onNodeClick="onNodeClick1" >        
            </ul>
		</div>
		<div title="笔记详情" region="center" id="globalKeyEvent" >
		    <div id="webchatLayoutinner" class="mini-layout" style="width:775px;height:100%;">
				<div title="笔记详情" region="center" height="382px" style="overflow-x:hidden">
				    <div id="content"></div>
				</div>
				<div title="输入窗口" region="south" showSplit="false" showHeader="true" height="270px" showCollapseButton="false"
				     showSplitIcon="false" >
				    <textarea id="msg" name="msg" 
							style="width:100%;height:205px;">
		    		</textarea>
		        	<div class="mini-toolbar">
					    <a class="mini-button" iconCls="icon-goto" onclick="sendMsg">发送</a>
					    <span class="separator"></span>
					    <a class="mini-button" iconCls="icon-goto" onclick="clearAll">清空</a>
					    <span class="separator"></span>
				    </div>
		    	</div>
		  </div>
		    
		</div>
  </div>
</body>
</html>
