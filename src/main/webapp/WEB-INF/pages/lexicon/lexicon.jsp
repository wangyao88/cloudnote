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
<link href="<%=basePath%>js/miniui/themes/bootstrap/skin.css" rel="stylesheet" type="text/css" />
<link href="<%=basePath%>js/miniui/res/demo.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=basePath%>css/a.css" />
<link rel="stylesheet" href="<%=basePath%>css/scroller.css" />

<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/jquery.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/lexicon/lexicon.js" type="text/javascript"></script>
<script type="text/javascript">
   var basePath = "<%=basePath%>";
</script>
</head>
<body>
    <div id="layout1" class="mini-layout" style="width:100%;height:100%;"borderStyle="border:solid 1px #aaa;">
		<div showHeader="false" region="west" width="400px;" expanded="true">
			<div class="mini-tabs" activeIndex="0"  style="width:400px;height:100%">
			    <div title="停用词" iconCls="icon-cut">
                      <div title="停用词列表" region="north" width="100%" expanded="true">
	                      <div class="mini-toolbar" height="33px">
						     <a class="mini-button" iconCls="icon-addnew" onclick="addStopLexicon">增加</a>
						     <span class="separator"></span>
						     <a class="mini-button" iconCls="icon-remove" onclick="removeStopLexicon">删除</a>
					      </div>
						  <div id="stopLexiconGrid" class="mini-datagrid" style="width:100%;height:95%;" 
					          url="<%=basePath%>lexicon/findAllStop" idField="id" allowResize="false" autoLoad="true"
					          pageSize="25" sizeList="[25,50,100]" ajaxType="post" multiSelect="false">
			            	  <div property="columns">
					             <div type="indexcolumn">序号</div>
					             <div field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
					             <div field="name" headerAlign="center" allowSort="true" width="300px" >名称</div>
			                  </div>
			              </div>
		               </div>		
			    </div>
			    <div title="扩展词" iconCls="icon-goto">
			        <div title="扩展词列表" region="north" width="100%" expanded="true">
			            <div class="mini-toolbar" height="33px">
						     <a class="mini-button" iconCls="icon-addnew" onclick="addExtLexicon">增加</a>
						     <span class="separator"></span>
						     <a class="mini-button" iconCls="icon-remove" onclick="removeExtLexicon">删除</a>
					    </div>
						<div id="extLexiconGrid" class="mini-datagrid" style="width:100%;height:95%;" 
					        url="<%=basePath%>lexicon/findAllExt" idField="id" allowResize="false" autoLoad="true"
					        pageSize="25" sizeList="[25,50,100]" ajaxType="post" multiSelect="false">
			            	<div property="columns">
					            <div type="indexcolumn">序号</div>
					            <div field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
					            <div field="name" headerAlign="center" allowSort="true" width="300px" >名称</div>
			                </div>
			            </div>
		             </div>
			    </div>
			    <div title="关键词" iconCls="icon-filter">
			        <div title="关键词列表" region="north" width="100%" expanded="true">
			            <div class="mini-toolbar" height="33px">
						     <a class="mini-button" iconCls="icon-addnew" onclick="addKeyLexicon">增加</a>
						     <span class="separator"></span>
						     <a class="mini-button" iconCls="icon-remove" onclick="removeKeyLexicon">删除</a>
						     <span class="separator"></span>
						     <a class="mini-button" iconCls="icon-remove" onclick="changeToStop">停用</a>
						     <span class="separator"></span>
						     <a class="mini-button" iconCls="icon-filter" onclick="initKey">初始化</a>
						     <span class="separator"></span>
						     <a class="mini-button" iconCls="icon-filter" onclick="createIndexs">索引</a>
					    </div>
						<div id="keyLexiconGrid" class="mini-datagrid" style="width:100%;height:95%;" 
					        url="<%=basePath%>lexicon/findAllKey" idField="id" allowResize="false" autoLoad="true"
					        pageSize="25" sizeList="[25,50,100]" ajaxType="post" multiSelect="false">
			            	<div property="columns">
					            <div type="indexcolumn">序号</div>
					            <div field="id"  headerAlign="center" allowSort="true" width="0px" >ID</div>
					            <div field="name" headerAlign="center" allowSort="true" width="300px" >名称</div>
			                </div>
			            </div>
		             </div>
			    </div>
			</div>
        </div>
        <div title="文章内容" region="center">
		   
		</div>
	</div>
</body>
</html>