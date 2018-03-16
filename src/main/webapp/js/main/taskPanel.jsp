<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>增加节点</title>
    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
    <script src="<%=basePath %>js/boot.js" type="text/javascript"></script>
    <style type="text/css">
    body,html{
        height:100%;width:100%;
        margin:0;padding:0;
    }
    </style>
</head>
<body>
    <div id="form1" >
        <table>
            <tr>
                <td>
                    <label>节点名称：</label>
                </td>
                <td>
                    <input id="name"  name="name" class="mini-textbox" required="true" />
                </td>
            </tr>
        </table>
    </div>
    <input type="button" value="确定" onclick="CloseWindow()"/>
</body>
</html>
<script type="text/javascript">
mini.parse();
var form = new mini.Form("form1");

function SetData(data){
     data = mini.clone(data);
     form.setData(data);
}

function CloseWindow() {
    var options = form.getData();
    var nodeName = options.name;
    if(nodeName == null || nodeName == ""){
		 mini.alert("笔记本名称不能为空！");
		 return false;
	}
    window.Owner.addNode(options);
    window.CloseOwnerWindow();
}
</script>