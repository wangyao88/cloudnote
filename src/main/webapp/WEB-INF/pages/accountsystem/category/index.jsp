<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>爱记账-收支类别管理</title>
    
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
	<script src="<%=basePath%>js/accountsystem/category/category.js" type="text/javascript"></script>

  </head>
  
  <body>
    <div class="mini-clearfix ">
         <div class="mini-col-6">
            <div class="mini-panel" title="收入类别" width="auto" height="98%" showCollapseButton="false" showCloseButton="false">
                <input id="incomeAccountBook" name="incomeAccountBook"
					class="mini-combobox" style="width:30%;" textField="name"
					valueField="id" emptyText="请选择账本..."
					url="<%=basePath%>accountsystem/accountbook/getAll" ajaxType="post"
					allowInput="true" showNullItem="true" nullItemText="请选择账本..." onvaluechanged="onIncomeChange"/>
                <ul id="incomeCategoryTree" class="mini-tree" url="<%=basePath%>accountsystem/category/getIncomeCategoryTree" ajaxType="post" style="width:100%;height:94%" 
                    showTreeIcon="true" textField="text" idField="id" resultAsTree="false" contextMenu="#incomeCategoryTreeMenu"
				    expandOnLoad="true" style="overflow:hidden;">
	            </ul>
	            <ul id="incomeCategoryTreeMenu" class="mini-contextmenu"  onbeforeopen="onBeforeOpenIncomeCategoryTreeMenu">        
				    <li class="separator"/>
					<li name="add" iconCls="icon-add" onclick="onAddIncomeNode">新增子节点</li>
					<li class="separator"/>
					<li name="edit" iconCls="icon-edit" onclick="onEditIncomeNode">编辑节点</li>
					<li class="separator"/>
					<li name="remove" iconCls="icon-remove" onclick="onRemoveIncomeNode">删除节点</li>
					<li class="separator"/>     
				</ul>
             </div>
         </div>
         <div class="mini-col-6">
             <div class="mini-panel" title="支出类别" width="auto" height="98%" showCollapseButton="false" showCloseButton="false">
                  <input id="outcomeAccountBook" name="outcomeAccountBook"
					class="mini-combobox" style="width:30%;" textField="name"
					valueField="id" emptyText="请选择账本..."
					url="<%=basePath%>accountsystem/accountbook/getAll" ajaxType="post"
					allowInput="true" showNullItem="true" nullItemText="请选择账本..." onvaluechanged="onOutcomeChange"/>
                <ul id="outcomeCategoryTree" class="mini-tree" url="<%=basePath%>accountsystem/category/getOutcomeCategoryTree" ajaxType="post" style="width:100%;height:94%" 
                    showTreeIcon="true" textField="text" idField="id" resultAsTree="false" contextMenu="#outcomeCategoryTreeMenu"
				    expandOnLoad="true" style="overflow:hidden;">
	            </ul>
	            <ul id="outcomeCategoryTreeMenu" class="mini-contextmenu"  onbeforeopen="onBeforeOpenOutcomeCategoryTreeMenu">        
				    <li class="separator"/>
					<li name="add" iconCls="icon-add" onclick="onAddOutcomeNode">新增子节点</li>
					<li class="separator"/>
					<li name="edit" iconCls="icon-edit" onclick="onEditOutcomeNode">编辑节点</li>
					<li class="separator"/>
					<li name="remove" iconCls="icon-remove" onclick="onRemoveOutcomeNode">删除节点</li>
					<li class="separator"/>     
				</ul>
             </div>
         </div>
     </div>
  </body>
</html>
