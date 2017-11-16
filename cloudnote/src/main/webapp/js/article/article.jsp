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
    <script src="<%=basePath %>js/boot.js" type="text/javascript"></script>
    <style type="text/css">
    body,html{
        height:100%;width:100%;
        margin:0;padding:0;
    }
    </style>
    <script src="http://apps.bdimg.com/libs/ueditor/1.4.3.1/ueditor.config.js" type="text/javascript"></script>
    <script src="http://apps.bdimg.com/libs/ueditor/1.4.3.1/ueditor.all.min.js" type="text/javascript"></script>
</head>
<body>
    <table id="form1" border="0" style="width:100%;table-layout:fixed;">
	    <tr>
	        <td style="width:90%"  colspan="2">
	            <input name="title" class="mini-textbox" style="width:100%;" required="true" emptyText="请输入标题..." nullItemText="请输入标题..."/>
	        </td>
	    </tr>
	    <tr>
	        <td style="width:30%">
		        <input id="note" name="note" class="mini-combobox" style="width:30%;" textField="name" valueField="id" emptyText="请选择笔记本..."
	               url="<%=basePath %>note/getNoteDataFromCombo" ajaxType="get" required="true" allowInput="true" showNullItem="true" nullItemText="请选择笔记本..."/>
	        </td>
	        <td style="width:70%">
	            <input id="flags" name="flags" class="mini-buttonedit" style="width:70%;" required="true"  emptyText="请选择标签..." nullItemText="请选择标签..." onbuttonclick="onButtonEdit"/>    
	        </td>
	    </tr>
	    <tr>
	        <td colspan="2" >
	         <textarea id="container" name="content" style="width:100%;height:455px;">
       		 </textarea>
	        </td>        
	    </tr>
	</table>
    <input type="button" value="确定" onclick="CloseWindow()"/>
</body>
</html>
<script type="text/javascript">
var ue = UE.getEditor('container');

mini.parse();
var form = new mini.Form("form1");

var isEdit = false;
var articleId;

function SetData(data){
     data = mini.clone(data);
     form.setData(data);
     if(data){
    	 isEdit = true;
    	 var articleIdTemp = data.id
    	 articleId = articleIdTemp;
    	 $.ajax({
				url : basePATH+"/note/getNoteByArticleId",
				type : "post",
				data : {
					articleId : articleIdTemp
				},
				dataType : 'json',
				success : function(result){
					 var note = mini.get("note");
					 note.setValue(result.data.id);
					 note.setText(result.data.name);
		        }
			});
    	 $.ajax({
				url : basePATH+"/flag/getFlagByArticleId",
				type : "post",
				data : {
					articleId : articleIdTemp
				},
				dataType : 'json',
				success : function(result){
					 var flag = mini.get("flags");
					 flag.setValue(result.data.id);
					 flag.setText(result.data.name);
		        }
			});
    	 $.ajax({
    			url : basePATH+"/article/getArticle",
    			type : "post",
    			data : {
    				id : articleIdTemp
    			},
    			dataType : 'json',
    			success : function(result){
    				ue.setContent(result.data);
    	        },
    	        error : function(){
    	        	mini.alert("获取笔记详情失败，请稍候重试！");
    	        }
    		});
     }else{
    	 isEdit = false;
     }
    
}

function CloseWindow() {
    var options = form.getData();
    if(isEdit){
    	options.articleId = articleId;
    }
    options.isEdit = isEdit;
    //var nodeName = options.name;
   // if(nodeName == null || nodeName == ""){
	//	 mini.alert("笔记本名称不能为空！");
	//	 return false;
	//}
    options.content = ue.getContent();
    window.Owner.addArticleAction(options);
    window.CloseOwnerWindow();
}

function onButtonEdit(){
	var btnEdit = this;
	mini.open({
        url: basePATH + "/js/flag/MultiSelectTreeWindow.jsp",    
        title: "请选择标签",
        width: 550,
        height: 450,
        ondestroy: function (action) {
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
</script>