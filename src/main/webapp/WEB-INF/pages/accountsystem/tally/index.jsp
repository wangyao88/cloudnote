<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>爱记账-记账管理</title>
    
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
	<script src="<%=basePath%>js/accountsystem/tally/tally.js" type="text/javascript"></script>

  </head>
  
  <body>
    <div class="mini-clearfix ">
         <div class="mini-col-12">
             <div class="mini-panel" title="查询条件" width="auto" height="8%" showCollapseButton="false" showCloseButton="false">
                 <div class="mini-col-1">
                 	<input id="searchAccountBookCombobox" class="mini-combobox" style="width:100%;" emptyText="账本"
			                	   url="<%=basePath%>accountsystem/accountbook/getAll" ajaxType="post" 
			                	   valueField="id" textField="name"
			                	   onvaluechanged="onSearchAccountBookChanged"/>
                 </div>
                 <div class="mini-col-1" style="margin-left:20px;">
                 	<input id="searchTypeCombobox" class="mini-combobox" emptyText="收支类型"
                 		   style="width:100%;" data="Type" 
                 		   onvaluechanged="onSearchTypeChanged"/>
                 </div>
                 <div class="mini-col-1" style="margin-left:20px;">
                 	<input id="searchCategoryCombobox" class="mini-combobox" style="width:100%;" emptyText="类别"
                 		   url="<%=basePath%>accountsystem/category/getCategory" ajaxType="get" 
                 		   valueField="id" textField="name"/>  
                 </div>
                 <div class="mini-col-1" style="margin-left:20px;">
                 	<input id="searchMark" class="mini-textbox" style="width:100%;" emptyText="备注"/>
                 </div>
                 <div class="mini-col-1" style="margin-left:20px;">
			            <input id="searchBeginDate" class="mini-datepicker" style="width:100%;" dateFormat="yyyy-MM-dd" emptyText="开始日期"/>
                 </div>
                 <div class="mini-col-1">
                 	<input id="searchEndDate" class="mini-datepicker" style="width:100%;" dateFormat="yyyy-MM-dd" emptyText="结束日期"/>
                 </div>
                 <div class="mini-col-1" style="margin-left:20px;">
                 	<a class="mini-button mini-button-success" onclick="searchTally()">查询</a>
                 </div>
             </div>
         </div>
     </div>
     <div class="mini-clearfix ">
         <div class="mini-col-12">
             <div class="mini-panel" title="账目列表" width="auto" height="90%" showCollapseButton="false" showCloseButton="false">
                 <div style="width:100%;">
			        <div class="mini-toolbar" style="border-bottom:0;padding:0px;">
			            <table style="width:100%;">
			                <tr>
			                    <td style="width:100%;">
			                        <a class="mini-button" iconCls="icon-add" onclick="addRow()" plain="true" tooltip="增加...">增加</a>
			                        <span class="separator"></span>
			                        <a class="mini-button" iconCls="icon-remove" onclick="removeRow()" plain="true">删除</a>
			                        <span class="separator"></span>
			                        <a class="mini-button" iconCls="icon-save" onclick="saveData()" plain="true">保存</a>            
			                    </td>
			                </tr>
			            </table>           
			        </div>
			    </div>
			    <div id="tallyGrid" class="mini-datagrid" style="width:100%;height:95%;" 
			          url="<%=basePath%>accountsystem/tally/getTallyList" ajaxType="post" idField="id" 
			          allowResize="false" pageSize="20" sizeList="[20,50,100]" autoLoad=true
			          allowCellEdit="true" allowCellSelect="true" multiSelect="true" 
			          editNextOnEnterKey="true"  editNextRowCell="true" >
			        <div property="columns">
			            <div type="indexcolumn"></div>
			            <div name="idColumn" field="id"  headerAlign="center" allowSort="false" width="0px" >ID</div>
			            <div type="comboboxcolumn" name="accountBook" field="accountBook" headerAlign="center" >账本
			                <input property="editor" class="mini-combobox" style="width:100%;" 
			                	   url="<%=basePath%>accountsystem/accountbook/getAll" ajaxType="post" 
			                	   valueField="id" textField="name"
			                	   onvaluechanged="onAccountBookChanged"/>                
			            </div> 
			            <div type="comboboxcolumn" autoShowPopup="true" name="type" field="type" align="center" headerAlign="center">收支类型
			                <input property="editor" class="mini-combobox" style="width:100%;" data="Type" onvaluechanged="onTypeChanged"/>       
			            </div>
			            <div type="comboboxcolumn" name="category" field="category" headerAlign="center" >类别
			                <input property="editor" class="mini-combobox" style="width:100%;" url="<%=basePath%>accountsystem/category/getCategory" ajaxType="get"  valueField="id" textField="name"/>                
			            </div> 
			            <div name="money" field="money" width="100" allowSort="true" >金额
			                <input property="editor" class="mini-spinner"  minValue="0" maxValue="2000000000" style="width:100%;"/>
			            </div>   
			            <div name="createDate" field="createDate" allowSort="true" dateFormat="yyyy-MM-dd HH:mm:ss">记账日期
			                <input property="editor" class="mini-datepicker" style="width:100%;"/>
			            </div>    
			            <div name="mark" field="mark" headerAlign="center" allowSort="true">备注
			                <input property="editor" class="mini-textarea" style="width:200px;" minWidth="200" minHeight="50"/>
			            </div>
			        </div>
			    </div>
             </div>
         </div>
     </div>
  </body>
</html>
