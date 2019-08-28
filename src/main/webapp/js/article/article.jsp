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

<link rel="stylesheet" href="<%=basePath%>js/ueditor-1.4.3.3/third-party/SyntaxHighlighter/shCoreDefault.css" />

<script src="<%=basePath%>js/boot.js" type="text/javascript"></script>
<script src="<%=basePath%>js/ueditor-1.4.3.3/ueditor.config.js" type="text/javascript"></script>
<script src="<%=basePath%>js/ueditor-1.4.3.3/ueditor.all.min.js" type="text/javascript"></script>
<script src="<%=basePath%>js/ueditor-1.4.3.3/third-party/SyntaxHighlighter/shCore.js" type="text/javascript"></script>
<script type="text/javascript">
	SyntaxHighlighter.all();
</script>
<style type="text/css">
body, html {
	height: 100%;
	width: 100%;
	margin: 0;
	padding: 0;
}
</style>
</head>
<body>
    <div title="工具栏" region="north" showHeader="false" height="28px">
		<div class="mini-toolbar">
		    <a class="mini-button" iconCls="icon-addnew" style="align:center"onclick="onOk">确定</a>
		    <span class="separator"></span>
		    <a class="mini-button" iconCls="icon-edit" onclick="onCancel">取消</a>
	    </div>
	</div>
	<table id="form1" border="0" style="width:100%;table-layout:fixed;">
		<tr>
			<td style="width:90%" colspan="3">
				<input name="title" id="title" onvaluechanged="checkTitle"
					class="mini-textbox" style="width:100%;" required="true"
					emptyText="请输入标题..." nullItemText="请输入标题..." />
			</td>
		</tr>
		<tr>
			<td style="width:30%">
				<input id="note" name="note"
					class="mini-combobox" style="width:70%;" textField="name"
					valueField="id" emptyText="请选择笔记本..."
					url="<%=basePath%>note/getNoteDataFromCombo" ajaxType="get"
					required="true" allowInput="true" showNullItem="true"
					nullItemText="请选择笔记本..." />
		    </td>
			<td style="width:70%">
			    <input id="flags" name="flags"
					class="mini-buttonedit" style="width:70%;" required="true"
					emptyText="请选择标签..." nullItemText="请选择标签..."
					allowInput=“false” onbuttonclick="onButtonEdit" />
			</td>
			<td style="width:30%">
			    <div name="isShared" id="isShared" class="mini-checkbox" readOnly="false" 
			    	 text="分享到博客">
			    </div>
			</td>
		</tr>
		<tr>
			<td colspan="3">
			     <textarea id="editor_id" name="content"
					style="width:100%;height:455px;">
			     </textarea>
			</td>
		</tr>
	</table>
	<div title="工具栏" region="north" showHeader="false" height="28px">
		<div class="mini-toolbar">
			<a class="mini-button" iconCls="icon-addnew" style="align:center"onclick="onOk">确定</a>
			<span class="separator"></span>
			<a class="mini-button" iconCls="icon-edit" onclick="onCancel">取消</a>
		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	mini.parse();
    var editor = UE.getEditor('editor_id',{
        'enterTag' : 'br'
    });
	var form = new mini.Form("form1");
	var isEdit = false;
	var articleId;
	var oldTitle;
	
	function SetDefaultFlagData(node){
		if(node){
			var nodeId = node.id;
		    var nodeText = node.text;
		    if(nodeId.indexOf("note") == 0){
		    	var note = mini.get("note");
				note.setValue(nodeId);
				note.setText(nodeText);
		    }
		    if(nodeId.indexOf("flag") == 0){
		    	var flag = mini.get("flags");
				flag.setValue(nodeId);
				flag.setText(nodeText);
		    }
		}
	}

	function SetData(data) {
		data = mini.clone(data);
		form.setData(data);
		if (data) {
			isEdit = true;
			oldTitle = $("input[name='title']").val();
			var articleIdTemp = data.id
			articleId = articleIdTemp;
			$.ajax({
				url : basePATH + "/article/getArticleForEdit",
				type : "post",
				data : {
					articleId : articleIdTemp
				},
				dataType : 'json',
				success : function(result) {
					var note = mini.get("note");
					note.setValue(result.note.id);
					note.setText(result.note.name);
					
					var flag = mini.get("flags");
					flag.setValue(result.flag.id);
					flag.setText(result.flag.name);
					
					var isShared = mini.get("isShared");
					isShared.setChecked(result.isShared);
					
					editor.setContent(result.content);
				}
			});
		} else {
			isEdit = false;
		}
	}

	function getFormData() {
		var options = form.getData();
		if (isEdit) {
			options.articleId = articleId;
		}
		options.isEdit = isEdit;
		options.content = editor.getContent();
		var isShared = mini.get("isShared");
		options.isShared = isShared.getChecked();
		return options;
	}

	function CloseWindow(action) {
		if (window.CloseOwnerWindow) {
			var options = form.getData();
			if (isEdit) {
				options.articleId = articleId;
			}
			options.isEdit = isEdit;
			options.content = editor.getContent();
			//window.Owner.addArticleAction(options);
			return window.CloseOwnerWindow(action);
		}
		window.close();
	}

	function onOk() {
	    var options = form.getData();
	    if(!options.title){
	         mini.alert("标题不允许为空！");
	         return false;
	    }
	    if(!options.flags){
	         mini.alert("标签不允许为空！");
	         return false;
	    }
		CloseWindow("ok");
	}
	function onCancel() {
		CloseWindow("cancel");
	}

	function onButtonEdit() {
		var btnEdit = this;
		mini.open({
			url : basePATH + "/js/flag/MultiSelectTreeWindow.jsp",
			title : "请选择标签",
			width : 550,
			height : 450,
			onload: function () {       //弹出页面加载完成
		        var iframe = this.getIFrameEl(); 
		        var nodes = btnEdit.getValue();       
		        //调用弹出页面方法进行初始化
		        iframe.contentWindow.SetData(nodes); 
		                        
		    },
			ondestroy : function(action) {
				if (action == "ok") {
					var iframe = this.getIFrameEl();
					var data = iframe.contentWindow.GetData();
					data = mini.clone(data);

					btnEdit.setValue(data.id);
					btnEdit.setText(data.text);
				}
			}
		});
	}
	
	function checkTitle(){
		var title = $("input[name='title']").val();
		if(oldTitle == title){
			return;
		}
		$.ajax({
			url : basePATH + "/article/checkTitle",
			type : "post",
			data : {
				title : title
			},
			dataType : 'json',
			success : function(result) {
				if(!result.data){
					mini.alert("标题已存在，请重新填写！");
				}
			}
		});
	}
</script>