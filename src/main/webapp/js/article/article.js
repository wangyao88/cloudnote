function addArticle() {
	var tree = mini.get("menuTree");
	var node = tree.getSelectedNode();
	cacheAddArticleTreeMenu();
	mini.open({
		url : bootPATH + "article/article.jsp",
		title : "笔记详情",
		width : 1000,
		height : 700,
		onload : function() {
            var iframe = this.getIFrameEl();
            iframe.contentWindow.SetDefaultFlagData(node);
		},
		ondestroy : function(action) {
			if (action == "ok") {
				var iframe = this.getIFrameEl();
				var data = iframe.contentWindow.getFormData();
				data = mini.clone(data);
				addArticleAction(data);
			}
		}
	})
}

function cacheAddArticleTreeMenu(){
	$.ajax({
		url : basePATH + "/flag/cacheAddArticleTreeMenu"
	});
}

function addArticleAction(data) {
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '笔记保存中...'
	});
	
	
	
	$.ajax({
		url : basePATH + "/article/addArticle",
		type : "post",
		data : {
			title : data.title,
			content : data.content,
			note : data.note,
			flags : data.flags,
			articleId : data.articleId,
			isEdit : data.isEdit
		},
		success : function() {
			var tree = mini.get("menuTree")
			var node = tree.getSelectedNode();
			var data = {
				first : false
			};
			if (isNoteTreeNode(node)) {
				data.noteId = node.id;
			}
			if (isFlagTreeNode(node)) {
				data.flagId = node.id;
			}
			mini.unmask(document.body);
			loadArticles(data);
		},
		error : function() {
			mini.alert("笔记添加失败，请稍候重试！");
			mini.unmask(document.body);
		}
	});

}

function loadArticles(data) {
	mini.parse();
	var grid = mini.get("articleGrid");
	grid.loading();
	grid.load(data);
	grid.hideColumn("idColumn");
}

function gridRowClick() {
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	getArticleById(record.id);
}

function gridRowdblclick() {
	editArticle();
}

function getArticleById(articleId) {
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '加载中...'
	});
	$.ajax({
		url : basePATH + "/article/getArticle",
		type : "post",
		data : {
			id : articleId
		},
		dataType : 'json',
		success : function(result) {
			document.getElementById("articleContainer").innerHTML = result.data;
			mini.unmask(document.body);
		},
		error : function() {
			mini.unmask(document.body);
			mini.alert("获取笔记详情失败，请稍候重试！");
		}
	});
}

function removeArticle() {
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	if (!record) {
		mini.alert("请选择需要删除的笔记！");
		return false;
	}
	mini.confirm("确定删除笔记[" + record.title + "]吗？", "确定？",
		function(action) {
			if (action == "ok") {
				$.ajax({
					url : basePATH + "/article/deleteArticle",
					type : "post",
					data : {
						id : record.id
					},
					success : function() {
						var tree = mini.get("menuTree")
						var node = tree.getSelectedNode();
						var data = {
							first : false
						};
						if (isNoteTreeNode(node)) {
							data.noteId = node.id;
						}
						if (isFlagTreeNode(node)) {
							data.flagId = node.id;
						}
						loadArticles(data);
					},
					error : function() {
						mini.alert("笔记删除失败，请稍候重试！");
					}
				});
			}
		}
	);
}

function editArticle() {
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	if (!record) {
		mini.alert("请选择需要编辑的笔记！");
		return false;
	}
	mini.open({
		url : bootPATH + "article/article.jsp",
		title : "笔记详情",
		width : 1000,
		height : 700,
		onload : function() {
			var iframe = this.getIFrameEl();
			iframe.contentWindow.SetData(record);
		},
		ondestroy : function(action) {
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
	var text = mini.get("searchArticleText");
	var grid = mini.get("articleGrid");
	grid.load({
		articleTitle : text.value
	});
}

function firstLoadArticles(){
	var data = {
			first : true
		};
		loadArticles(data);
}

function addListener(){
	mini.parse();
	var grid = mini.get("articleGrid");
	grid.on("load", function() {
		grid.select(0);
		var record = grid.getSelected();
		if(!record || !record.id){
			document.getElementById("articleContainer").innerHTML = '';
		}else{
			getArticleById(record.id);
		}
		//grid.updateRow(record,{hitNum : record.hitNum+1});
		grid.unmask();
	});

	var searchText = mini.get("searchArticleText");
	searchText.on("keydown", function(event) {
		//		htmlEvent,sender,source,type
		if (event.htmlEvent.keyCode == 13) {
			searchArticle();
		}
	});

	$(document).keydown(function(e) {
		var keyCode = e.keyCode || e.which || e.charCode;
		var shiftKey = e.shiftKey || e.metaKey;
		quickKeyForAddArticle(shiftKey,keyCode);
		quickKeyForWholePage(shiftKey,keyCode);
		quickKeyForNormalPage(shiftKey,keyCode);
	});
}

//ctrl+Q
function quickKeyForAddArticle(shiftKey,keyCode){
	if (shiftKey && keyCode == 81) {
		addArticle();
	}
}

//ctrl+W
function quickKeyForWholePage(shiftKey,keyCode){
	if (shiftKey && keyCode == 87) {
		var layout = mini.get("layout1");
		layout.updateRegion("west", { expanded: false });
		layout.updateRegion("east", { expanded: false });
	}
}

//ctrl+C
function quickKeyForNormalPage(shiftKey,keyCode){
	if (shiftKey && keyCode == 69) {
		var layout = mini.get("layout1");
		layout.updateRegion("west", { expanded: true });
		layout.updateRegion("east", { expanded: true });
	}
}

$(document).ready(function() {
	firstLoadArticles();
	addListener();
});