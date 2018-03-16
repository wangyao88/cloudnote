<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests">
    <script src="<%=basePath %>js/boot.js" type="text/javascript"></script>
    
        
    <style type="text/css">
    html,body
    {
        padding:0;
        margin:0;
        border:0;     
        width:100%;
        height:100%;
        overflow:hidden;   
    }
    </style>
</head>
<body>
    <div class="mini-fit">
        <ul id="backupDB">
        	<li>
        	   <span id="fileName"></span>
        	   <a id="fileDownload">
        	       <span id="fileDownloadP" style="flow:right">下载</span>
        	   </a>
        	</li>
        </ul>
    </div>                
</body>
</html>
<script type="text/javascript">
    mini.parse();

    function SetData(filePath,hrefPath) {
        $("#fileDownload").attr("href",hrefPath);
        $("#fileName").text(filePath);
         $("#fileDownload").text("下载")
    }

    function CloseWindow(action) {
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();
    }
    function onOk() {
        CloseWindow("ok");
    }
    function onCancel() {
        CloseWindow("cancel");
    }
</script>