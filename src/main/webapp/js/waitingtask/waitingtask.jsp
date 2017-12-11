<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>待办任务</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css"
	rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet"
	type="text/css" />
<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/waitingtask/waitingtask.js" type="text/javascript"></script>
<script type="text/javascript">
   var basePath = "<%=basePath%>";
</script>
</head>
<body id="globalKeyEvent">
	<div id="waitingtaskLayout" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
		 <div title="任务列表" region="west" width="800px" expanded="true">
				<div id="waitingtaskGrid" class="mini-datagrid" style="width:795px;height:100%;" 
			        url="<%=basePath%>waitingtask/findPage" idField="id" allowResize="false" 
			        pageSize="10" sizeList="[20,50,100]" ajaxType="post" multiSelect="false">
	            	<div property="columns">
			            <div type="indexcolumn">序号</div>
			            <div field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
			            <div field="name" headerAlign="center" allowSort="true" width="250px" >任务标题</div>
			            <div field="createDate" width="60px" allowSort="true" dateFormat="yyyy-MM-dd">创建日期</div>
			            <div field="process" width="110px" allowSort="true" >任务进度(%)</div>
			            <div field="expire" width="60px" allowSort="true" dateFormat="yyyy-MM-dd">结束日期</div>
			            <div field="taskType" width="60px" allowSort="true" renderer="onTaskTypeRenderer" align="center">任务类型</div>   
			        </div>       
	            </div>
        </div>
        
		<div title="任务统计" region="center">
		    <div class="mini-toolbar" height="33px" width="340px">
				    <a class="mini-button" iconCls="icon-addnew" onclick="addWaitingTask">增加</a>
				    <span class="separator"></span>
				    <a class="mini-button" iconCls="icon-remove" onclick="deleteWaitingTask">删除</a>
				    <span class="separator"></span>
				    <a class="mini-button" iconCls="icon-edit" onclick="insertWaitingTask">保存</a>
				    <span class="separator"></span>
			    </div>
		        <fieldset style="width:340px;border:solid 1px #aaa;margin-top:8px;position:relative;">
			        <legend>任务详细信息</legend>
			        <div id="waitingtaskEditForm" style="padding:5px;">
			            <input class="mini-hidden" name="id"/>
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:80px;">任务标题：</td>
			                    <td style="width:200px;"><input id="name" name="name" class="mini-textbox" /></td>
			                </tr>
			                <tr>
			                    <td>任务进度：</td>
			                    <td><input id="process" name="process" class="mini-textbox"/></td>
			                </tr>
			                <tr>
			                    <td>任务类型：</td>
			                    <td><input id="taskType" name="taskType" class="mini-combobox" data="TaskTypes"/></td>
			                </tr>
			                <tr>
			                    <td>创建日期：</td>
			                    <td><input id="createDate" name="createDate" class="mini-datepicker"/></td>
			                    
			                </tr>
			                <tr>
			                    <td>结束日期：</td>
			                    <td><input id="expire" name="expire" class="mini-datepicker"/></td>
			                </tr>
			                <tr>
			                    <td>任务内容：</td>
			                    <td><input id="content" name="content" class="mini-textarea"/></td>
			                </tr>
			            </table>
			        </div>
			    </fieldset>
		</div>
	</div>
</body>
</html>