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
<base href="<%=basePath%>">
<title>待办任务</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css" />

<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/echarts/echarts-3.8.4.js" type="text/javascript"></script>
<script type="text/javascript">
var uid='waitingtask-'+'<%=uid%>';
var basePath = "<%=basePath%>";
var wsPath = '<%=wsPath%>';
</script>
<script src="<%=basePath%>js/waitingtask/waitingtask.js" type="text/javascript"></script>
</head>
<body id="globalKeyEvent">
	<div id="waitingtaskLayout" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
		 <div title="任务列表" region="west" width="800px" expanded="true">
				<div id="waitingtaskGrid" class="mini-datagrid" style="width:795px;height:100%;" 
			        url="<%=basePath%>waitingtask/findPage" idField="id" allowResize="false" 
			        pageSize="15" sizeList="[15,30,60]" ajaxType="post" multiSelect="false"
			        ondrawcell="onDrawCell">
	            	<div property="columns">
			            <div type="indexcolumn">序号</div>
			            <div field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
			            <div field="name" headerAlign="center" allowSort="true" width="250px" >任务标题</div>
			            <div field="createDate" width="0px" allowSort="true" dateFormat="yyyy-MM-dd H:mm:ss">创建日期</div>
			            <div field="beginDate" width="60px" allowSort="true" dateFormat="yyyy-MM-dd">开始日期</div>
			            <div field="process" width="110px" allowSort="true" >任务进度(%)</div>
			            <div field="expireDate" width="60px" allowSort="true" dateFormat="yyyy-MM-dd">结束日期</div>
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
			            <input class="mini-hidden" name="createDate"/>
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:80px;">任务标题：</td>
			                    <td style="width:230px;"><input id="name" name="name" class="mini-textbox" style="width:228px;"/></td>
			                </tr>
			                <tr>
			                    <td>任务进度：</td>
			                    <td><input id="process" name="process" class="mini-textbox"/ style="width:228px;"></td>
			                </tr>
			                <tr>
			                    <td>任务类型：</td>
			                    <td><input id="taskType" name="taskType" class="mini-combobox" data="TaskTypes" style="width:228px;"/></td>
			                </tr>
			                <tr>
			                    <td>开始日期：</td>
			                    <td><input id="beginDate" name="beginDate" class="mini-datepicker" style="width:228px;"/></td>
			                    
			                </tr>
			                <tr>
			                    <td>结束日期：</td>
			                    <td><input id="expireDate" name="expireDate" class="mini-datepicker" style="width:228px;"/></td>
			                </tr>
			                <tr>
			                    <td>任务内容：</td>
			                    <td><input id="content" name="content" class="mini-textarea" style="width:228px;height:150px;"/></td>
			                </tr>
			            </table>
			        </div>
			    </fieldset>
			    
		</div>
	</div>
</body>
</html>