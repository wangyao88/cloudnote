<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>爱记账-账本管理</title>
    
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
    <div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
		<div title="工具栏" region="north" showHeader="false" height="34px">
			<div class="mini-toolbar" height="33px">
			    <a class="mini-button" iconCls="icon-addnew" onclick="addAccountBookPage">增加</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-save" onclick="saveAccountBook">保存</a>
			    <span class="separator"></span>
			    <a class="mini-button" iconCls="icon-remove" onclick="removeAccountBook">删除</a>
			    <span class="separator"></span>
			    <input class="mini-textbox" id="searchAccountBookByNameText" emptyText="账本名称"/>   
			    <a class="mini-button" id="searchArticleByTitleBtn" iconCls="icon-search" onclick="searchAccountBookByName">查询</a>
		    </div>
		 </div>
		<div title="" region="center">
		    <div id="accountbookGrid" class="mini-datagrid" style="width:100%;height:100%;" 
		        url="<%=basePath%>accountsystem/accountbook/getAccountBookList" idField="id" allowResize="false" 
		        pageSize="2" sizeList="[20,50,100]" ajaxType="post" multiSelect="true" autoLoad=true
		        allowCellEdit="true" allowCellSelect="true" >
            	<div property="columns">
	            <div type="indexcolumn">序号</div>
	            <div name="idColumn" field="id"  headerAlign="center" allowSort="false" width="0px" >ID</div>
	            <div name="name"  field="name" headerAlign="center" allowSort="false">账本名称
	                <input property="editor" class="mini-textbox" style="width:100%;"/>
	            </div>
	            <div name="createDate" field="createDate" allowSort="false" dateFormat="yyyy-MM-dd HH:mm:ss">创建日期
	                <input class="mini-datepicker" style="width:100%;"/>
	            </div> 
	            <div name="income" field="income" allowSort="false" >收入</div>
	            <div name="outcome" field="outcome" allowSort="false" >支出</div>
	            <div name="remainingSum" field="remainingSum" allowSort="false" >结余</div>
	            <div name="mark"  field="mark" headerAlign="center" allowSort="false">备注
	                <input property="editor" class="mini-textarea" style="width:100%;"/>
	            </div>
            </div>
		</div>
	</div>
  </body>
</html>

