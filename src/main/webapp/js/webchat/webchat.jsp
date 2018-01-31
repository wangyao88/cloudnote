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
    
    String wsPath = "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href="<%=basePath%>js/webchat/webchat.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>css/scroller.css" rel="stylesheet" type="text/css" />

<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
<script src="<%=basePath%>js/ueditor-1.4.3.3/ueditor.webchat.config.js" type="text/javascript"></script>
<script src="<%=basePath%>js/ueditor-1.4.3.3/ueditor.all.min.js" type="text/javascript"></script>

<script type="text/javascript">
var from='<%=uid%>';
var fromName='<%=name%>';
var wsPath = '<%=wsPath%>';
</script>

</head>
<body>
	<div id="webchatLayout" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;" >
		<div title="消息记录" region="center">
		    <div id="content" style="overflow:auto"></div>
		    <div><a id="msg_end" name="1" href="#1">&nbsp</a></div>
		</div>
		<div region="south" showSplit="false" showHeader="false" height="210px" showCollapseButton="false"
		     showSplitIcon="false" style="overflow:hidden;">
		    <textarea id="msg" name="msg" style="width:100%;height:144px;overflow:hidden;">
		    </textarea>
        	<div class="mini-toolbar">
        	    <input id="userTo" name="userTo"
					class="mini-combobox" style="width:30%;" textField="name"
					valueField="id" emptyText="请选择好友..."
					url="<%=basePath%>user/getAllFriendsFromCombo" ajaxType="get"
					required="true" allowInput="true" showNullItem="true"
					nullItemText="请选择好友..." onitemclick="selectFriend"/>
			    <a class="mini-button" iconCls="icon-upload" onclick="sendMsg" style="float:right;margin-right:10px;">发送</a>
			    <span class="separator" style="float:right;"></span>
			    <a class="mini-button" iconCls="icon-no" onclick="clearAll" style="float:right;">清空</a>
			    <span class="separator" style="float:right;"></span>
		    </div>
    	</div>
  </div>
</body>
</html>
<script type="text/javascript">
	var webchatInput = UE.getEditor('msg');
	webchatInput.addListener("keypress", function (type, event) {
         if(event.keyCode == 13){
        	 event.preventDefault(); 
        	 event.stopPropagation();
        	 sendMsg();
         }
    });
</script>
<script src="<%=basePath%>js/webchat/webchat.js" type="text/javascript"></script>