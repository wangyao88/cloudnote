function addArticle(){
	var tree = mini.get("menuTree");
    var node = tree.getSelectedNode();
    
    mini.open({
    	url: bootPATH+ "article/article.jsp",
            title: "笔记详情", width: 1000, height: 700,
            onload: function () {
//                var iframe = this.getIFrameEl();
//                iframe.contentWindow.SetData(node);
            }
        })
}

function addArticleAction(data){
	$.ajax({
			url : basePATH+"/article/addArticle",
			type : "post",
			data : {
				title : data.title,
				content : data.content,
				note : data.note,
				flags : data.flags,
				articleId : data.articleId,
				isEdit : data.isEdit
			},
			success : function(){
				var tree = mini.get("menuTree")
			    var node = tree.getSelectedNode();
				var data = {
						first : false
				};
				if(isNoteTreeNode(node)){
					data.noteId = node.id;
				}
				if(isFlagTreeNode(node)){
					data.flagId = node.id;
				}
				loadArticles(data);
	        },
	        error : function(){
	        	mini.alert("笔记添加失败，请稍候重试！");
	        }
		});
	
}

function loadArticles(data){
	 mini.parse();
     var grid = mini.get("articleGrid");
 	 grid.loading();
     grid.load(data);
     grid.hideColumn("idColumn");
}

function gridRowClick(){
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	getArticleById(record.id);
}

function gridRowdblclick(){
	editArticle();
}

function getArticleById(articleId){
	mini.mask({
        el: document.body,
        cls: 'mini-mask-loading',
        html: '加载中...'
    });
	$.ajax({
		url : basePATH+"/article/getArticle",
		type : "post",
		data : {
			id : articleId
		},
		dataType : 'json',
		success : function(result){
			document.getElementById("articleContainer").innerHTML = result.data;
			mini.unmask(document.body);
        },
        error : function(){
        	mini.unmask(document.body);
        	mini.alert("获取笔记详情失败，请稍候重试！");
        }
	});
}

function removeArticle(){
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	if(!record){
		mini.alert("请选择需要删除的笔记！");
		return false;
	}
	mini.confirm("确定删除笔记["+record.title+"]吗？", "确定？",
    		function(action) {
    			if (action == "ok") {
    				$.ajax({
    					url : basePATH+"/article/deleteArticle",
    					type : "post",
    					data : {
    						id : record.id
    					},
    					success : function(){
    						var tree = mini.get("menuTree")
    					    var node = tree.getSelectedNode();
    						var data = {
    								first : false
    						};
    						if(isNoteTreeNode(node)){
    							data.noteId = node.id;
    						}
    						if(isFlagTreeNode(node)){
    							data.flagId = node.id;
    						}
    						loadArticles(data);
    			        },
    			        error : function(){
    			        	mini.alert("笔记删除失败，请稍候重试！");
    			        }
    				});
    			}
    		}
    	);
}

function editArticle(){
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	if(!record){
		mini.alert("请选择需要编辑的笔记！");
		return false;
	}
	 mini.open({
	    	url: bootPATH+ "article/article.jsp",
	            title: "笔记详情", width: 1000, height: 700,
	            onload: function () {
	                var iframe = this.getIFrameEl();
	                iframe.contentWindow.SetData(record);
	            },
	            ondestroy: function (action) {
	                if (action == "ok") {
	                    var iframe = this.getIFrameEl();
	                    var data = iframe.contentWindow.getFormData();
	                    data = mini.clone(data);
	                    addArticleAction(data);
	                }
	            }
	        })
}

function searchArticle() {
    var removeArticle = document.getElementById("searchArticleBtn").value;
    var grid = mini.get("articleGrid");
    grid.load({ key: removeArticle });
}


$(document).ready(function(){
	var data = {
			first : true
	};
	loadArticles(data);
	
	$("#key").bind("keydown", function (e) {
	    if (e.keyCode == 13) {
	    	searchArticle();
	    }
	});
	
	mini.parse();
    var grid = mini.get("articleGrid");
	grid.on("load",function(){
		 console.log('------------------------------');
		 	grid.select(0);
		 	var record = grid.getSelected();
		 	getArticleById(record.id);
//		 	grid.updateRow(record,{hitNum : record.hitNum+1});
		 	grid.unmask();
    });
});