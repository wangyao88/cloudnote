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
	grid.load();
	
	$.ajax({
		url : basePATH + "/spider/getTotal",
		type : "post",
		dataType : 'json',
		success : function(result) {
			mini.showTips({
	            showModal: false,
	            state : "info",
	            content: "共有"+result+"篇文章",
	            timeout: 3000,
	            x: 'center',
	            y: 'center'
	        });
		},
		error : function() {
			
		}
	});
}

function removeArticle(){
	var grid = mini.get("articleGrid");
	var records = grid.getSelecteds();
	if (!records) {
		mini.alert("请选择需要删除的文章！");
		return false;
	}
	var length = records.length;
	var tip;
	if(length == 1){
		tip = "确定删除文章[" + records[0].title + "]吗？";
	}else{
		tip = "确定删除这"+length+"篇文章吗？";
	}
	mini.confirm(tip, "确定？",
		function(action) {
			if (action == "ok") {
				var ids = "";
				for(var i = 0; i < length; i++){
					ids += records[i].id + ",";
				}
				$.ajax({
					url : basePATH + "/spider/delete",
					type : "post",
					data : {
						ids : ids
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

function addArticle(){
	mini.alert("待建设");
}