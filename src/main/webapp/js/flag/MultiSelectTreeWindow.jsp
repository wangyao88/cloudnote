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
        <ul id="tree1" class="mini-tree" style="width:100%;height:100%;" 
            showTreeIcon="true" textField="text" idField="id" parentField="pid" resultAsTree="false"  
            showCheckBox="true" checkRecursive="true" ajaxType="get"
            expandOnLoad="true" allowSelect="false" enableHotTrack="false" 
            >
        </ul>
    
    </div>                
    <div class="mini-toolbar" style="text-align:center;padding-top:8px;padding-bottom:8px;" 
        borderStyle="border-left:0;border-bottom:0;border-right:0;">
        <a class="mini-button" style="width:60px;" onclick="onOk()">确定</a>
        <span style="display:inline-block;width:25px;"></span>
        <a class="mini-button" style="width:60px;" onclick="onCancel()">取消</a>
    </div>

</body>
</html>
<script type="text/javascript">
    mini.parse();

    var tree = mini.get("tree1");

    tree.load("<%=basePath%>flag/getCheckFlagTree");

    function GetCheckedNodes() {
        var nodes = tree.getCheckedNodes();
        return nodes;
    }
    
    function GetData() {
        var nodes = tree.getCheckedNodes();
        var ids = [], texts = [];
        for (var i = 0, l = nodes.length; i < l; i++) {
            var node = nodes[i];
            ids.push(node.id);
            texts.push(node.text);
        }
        var data = {};
        data.id = ids.join(",");
        data.text = texts.join(",");
        return data;
    }
    
    function SetData(nodeIdsStr){
	    if(nodeIdsStr){
	    	var nodeIds = nodeIdsStr.split(",");
	        for(var i = 0; i < nodeIds.length; i++){
	           var node = tree.getNode(nodeIds[i]);
	           tree.checkNode(node);
	        }
	        var node = tree.getNode(nodeIds[0]);
			if(node){
				tree.scrollIntoView(node);
			}
	    }
    }

    function CloseWindow(action) {
        if (window.CloseOwnerWindow) return window.CloseOwnerWindow(action);
        else window.close();
    }
    function onOk() {
        var node = tree.getSelectedNode();
        if (node && tree.isLeaf(node) == false) {
            alert("不能选中父节点");
            return;
        }
        
        CloseWindow("ok");
    }
    function onCancel() {
        CloseWindow("cancel");
    }

    
</script>