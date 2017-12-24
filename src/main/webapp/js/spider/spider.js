$(document).ready(function(){
	reload();
	addListener();
});

function spider(){
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '抓取中...'
	});
	$.ajax({
		url : basePATH + "/spider/spider",
		type : "post",
		dataType : 'json',
		success : function(result) {
			reload();
			mini.unmask(document.body);
		},
		error : function() {
			reload();
			mini.unmask(document.body);
		}
	});
}

function reload(){
	mini.parse();
	var grid = mini.get("articleGrid");
	console.log(grid);
	grid.load();
}

function removeArticle(){
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	if (!record) {
		mini.alert("请选择需要删除的文章！");
		return false;
	}
	mini.confirm("确定删除文章[" + record.title + "]吗？", "确定？",
		function(action) {
			if (action == "ok") {
				$.ajax({
					url : basePATH + "/spider/delete",
					type : "post",
					data : {
						id : record.id
					},
					success : function() {
						reload();
					},
					error : function() {
						mini.alert("文章删除失败，请稍候重试！");
					}
				});
			}
		}
	);
}

function removeAll(){
	mini.confirm("确定删除所有文章吗？", "确定？",
			function(action) {
				if (action == "ok") {
					$.ajax({
						url : basePATH + "/spider/deleteAll",
						type : "post",
						success : function() {
							reload();
						},
						error : function() {
							reload();
						}
					});
				}
			}
		);
}

function gridRowClick() {
	var grid = mini.get("articleGrid");
	var record = grid.getSelected();
	getArticleById(record.id);
}

function getArticleById(articleId) {
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '加载中...'
	});
	$.ajax({
		url : basePATH + "/spider/fetch",
		type : "post",
		data : {
			id : articleId
		},
		dataType : 'json',
		success : function(result) {
			document.getElementById("articleContainer").innerHTML = result;
			mini.unmask(document.body);
		},
		error : function() {
			mini.unmask(document.body);
			mini.alert("获取文章详情失败，请稍候重试！");
		}
	});
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
}