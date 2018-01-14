function createIndexs(){
	mini.confirm("创建笔记搜索索引比较耗时，确定创建吗？", "确定？",
			function(action) {
				if (action == "ok") {
					$.ajax({
						url : basePATH + "/article/createIndex",
						type : "post",
						success : function() {
							mini.showTips({
					            showModal: false,
					            state : "info",
					            content: "创建搜索索引成功",
					            timeout: 3000,
					            x: 'center',
					            y: 'center'
					        });
						},
						error : function() {
							mini.alert("创建搜索索引失败，请稍候重试！");
						}
					});
				}
			}
		);
}

function addLexicon(gridId,url){
	mini.prompt("请输入停用词：", "请输入",
            function (action, value) {
                if (action == "ok") {
	                if(value == null || value == ""){
	               		 mini.alert("名称不能为空！");
	               		 return false;
	               	}else{
	               		$.ajax({
	               			url : basePATH+"/lexicon/"+url,
	               			type : "post",
	               			data : {
	               				name : value
	               			},
	               			success : function(){
	               				mini.parse();
	               				var grid = mini.get(gridId);
	               				grid.loading();
	               				grid.load();
	               	        },
	               	        error : function(){
	               	        	mini.alert("添加失败，请稍候重试！");
	               	        }
	               		});
	               	}
                }
            }
         );
}

function addStopLexicon(){
	addLexicon("stopLexiconGrid","saveStopLexicon");
}

function addExtLexicon(){
	addLexicon("extLexiconGrid","saveExtLexicon");
}

function addKeyLexicon(){
	addLexicon("keyLexiconGrid","saveKeyLexicon");
}

function removeLexicon(gridId){
	var grid = mini.get(gridId);
	var record = grid.getSelected();
	if (!record) {
		mini.alert("请选择需要删除的词！");
		return false;
	}
	mini.confirm("确定删除词[" + record.name + "]吗？", "确定？",
		function(action) {
			if (action == "ok") {
				$.ajax({
					url : basePATH + "/lexicon/delete",
					type : "post",
					data : {
						id : record.id
					},
					success : function() {
						mini.parse();
           				grid.loading();
           				grid.load();
					},
					error : function() {
						mini.alert("删除失败，请稍候重试！");
					}
				});
			}
		}
	);
}

function removeStopLexicon(){
	removeLexicon("stopLexiconGrid");
}

function removeExtLexicon(){
	removeLexicon("extLexiconGrid");
}

function removeKeyLexicon(){
	removeLexicon("keyLexiconGrid");
}

function changeToStop(){
	var grid = mini.get("keyLexiconGrid");
	var record = grid.getSelected();
	if (!record) {
		mini.alert("请选择需要停用的词！");
		return false;
	}
	mini.confirm("确定停用词[" + record.name + "]吗？", "确定？",
		function(action) {
			if (action == "ok") {
				$.ajax({
					url : basePATH + "/lexicon/changeToStop",
					type : "post",
					data : {
						id : record.id,
						name : record.name
					},
					success : function() {
						mini.parse();
           				grid.loading();
           				grid.load();
           				mini.get("stopLexiconGrid").load();
					},
					error : function() {
						mini.alert("停用失败，请稍候重试！");
					}
				});
			}
		}
	);
}

function initKey(){
	var grid = mini.get("keyLexiconGrid");
	$.ajax({
		url : basePATH + "/lexicon/initKey",
		type : "post",
		success : function() {
			mini.parse();
				grid.loading();
				grid.load();
		},
		error : function() {
			mini.alert("初始化失败，请稍候重试！");
		}
	});
}

