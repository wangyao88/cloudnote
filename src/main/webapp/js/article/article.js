var fisrt = true;
var load = false;
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
			isEdit : data.isEdit,
			isShared : data.isShared
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

function contentChange(){
	var content = editor.getContent();
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	$.ajax({
		url : basePATH + "/article/quickupdate",
		type : "post",
		data : {
			articleId : record.id,
			content : content
		},
		dataType : 'json',
		success : function(result) {
			mini.showTips({
	            showModal: false,
	            state : "info",
	            content: "快速保存笔记成功！",
	            timeout: 2000,
	            x: 'center',
	            y: 'center'
	        });
		}
	});
}

function getArticleById(articleId) {
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '加载中...'
	});
	editor.ready(function(){
		$.ajax({
			url : basePATH + "/article/getArticle",
			type : "post",
			data : {
				id : articleId,
				searchKeys : record.searchKeys
			},
			dataType : 'json',
			success : function(result) {
				load = true;
				if(!result.data){
					mini.alert("笔记已删除，请重建索引，再搜索。或者忽略此条笔记！");
					return;
				}
				var title = record.title;
//				var content = '<center><h1>'+title+'</h1></center><br>'+result.data;
				var content = result.data;
				if(fisrt){
					setTimeout(function() {
						editor.setContent(content);
						fisrt = false;
					}, 500)
				}else{
					editor.setContent(content);
				}
				mini.unmask(document.body);
			},
			error : function() {
//				mini.unmask(document.body);
				mini.alert("获取笔记详情失败，请稍候重试！");
			}
		});
		
		$.ajax({
			url : basePATH + "/flag/getFlagByArticleId",
			type : "post",
			data : {
				articleId : articleId
			},
			dataType : 'json',
			success : function(result) {
				var flagIds = result.data.id;
				if(!flagIds){
					return;
				}
				var flagIdArr = flagIds.split(",");
				var tree = mini.get("menuTree");
				var node = tree.getNode(flagIdArr[0]);
				if(node){
					tree.scrollIntoView(node);
				}
			}
		});
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

function searchArticleByTitle() {
	mini.get("searchArticleByTitleOrContentText").setValue("");
	var text = mini.get("searchArticleByTitleText");
	var key = text.value;
	if(key == '520'){
		$.ajax({
			url : basePATH + "/love/index",
			type : "post",
			data : {
				key : key
			},
			success : function(result) {
				var openUrl = basePATH + "/love/page?path=" + result;
				window.open(openUrl,"_blank"); 
			}
		});
	}else{
		var grid = mini.get("articleGrid");
		grid.load({
			title : key
		});
	}
}

function searchArticleByTitleOrContentText() {
	// mini.get("searchArticleByTitleText").setValue("");
	var text = mini.get("searchArticleByTitleOrContentText");
	var grid = mini.get("articleGrid");
	grid.load({
		titleOrContent : text.value
	});
}

function firstLoadArticles(){
	var data = {
			first : true
		};
		loadArticles(data);
}

var isEditorReady = true;
function changeEditorMode(){
	if(isEditorReady){
		var content = editor.getContent();
		$("#articleContainer").hide();
		$("#articleContainerSimple").html(content);
		$("#articleContainerSimple").show();
		isEditorReady = false;
		return;
	}
	$("#articleContainerSimple").hide();
	$("#articleContainerSimple").html('');
	$("#articleContainer").show();
	isEditorReady = true;
}

function addListener(){
	mini.parse();
	var grid = mini.get("articleGrid");
	grid.on("load", function() {
		grid.select(0);
		var record = grid.getSelected();
		if(!record || !record.id){
			editor.setContent('');
		}else{
			getArticleById(record.id);
		}
		//grid.updateRow(record,{hitNum : record.hitNum+1});
	});

	// var searchArticleByTitleText = mini.get("searchArticleByTitleText");
	// searchArticleByTitleText.on("keydown", function(event) {
	// 	//		htmlEvent,sender,source,type
	// 	if (event.htmlEvent.keyCode == 13) {
	// 		searchArticle();
	// 	}
	// });

	$(document).keydown(function(e) {
		var keyCode = e.keyCode || e.which || e.charCode;
		var shiftKey = e.shiftKey || e.metaKey;
		quickKeyForAddArticle(shiftKey,keyCode);
		quickKeyForWholePage(shiftKey,keyCode);
		quickKeyForNormalPage(shiftKey,keyCode);
		quickKeyForChangeEditorMode(shiftKey,keyCode);
		quickKeyForContentChange(shiftKey,keyCode);
	});
}

//shift+Q
function quickKeyForAddArticle(shiftKey,keyCode){
	if (shiftKey && keyCode == 81) {
		addArticle();
	}
}

//shift+W
function quickKeyForWholePage(shiftKey,keyCode){
	if (shiftKey && keyCode == 87) {
		var layout = mini.get("layout1");
		layout.updateRegion("west", { expanded: false });
		layout.updateRegion("east", { expanded: false });
	}
}

//shift+C
function quickKeyForNormalPage(shiftKey,keyCode){
	if (shiftKey && keyCode == 69) {
		var layout = mini.get("layout1");
		layout.updateRegion("west", { expanded: true });
		layout.updateRegion("east", { expanded: true });
	}
}

//shift+A
function quickKeyForChangeEditorMode(shiftKey,keyCode){
	if (shiftKey && keyCode == 65) {
		changeEditorMode();
	}
}

//shift+S
function quickKeyForContentChange(shiftKey,keyCode){
	if (shiftKey && keyCode == 83) {
		contentChange();
	}
}

$(document).ready(function() {
	firstLoadArticles();
	addListener();
});