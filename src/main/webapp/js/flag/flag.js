function addFlagNode(tree){
	 var node = tree.getSelectedNode();
	 var level = tree.getLevel(node);
	 mini.prompt("请输入标签名称：", "请输入",
            function (action, value) {
                if (action == "ok") {
	                if(value == null || value == ""){
	               		 mini.alert("标签名称不能为空！");
	               		 return false;
	               	}else{
	               		$.ajax({
	               			url : basePATH+"/flag/addFlag",
	               			type : "post",
	               			data : {
	               				id : node.id,
	               				name : value,
	               				level : level
	               			},
	               			success : function(){
	               				tree.load(basePATH+"/main/getTree");
	               	        },
	               	        error : function(){
	               	        	mini.alert("标签添加失败，请稍候重试！");
	               	        }
	               		});
	               	}
                }
            }
         );
}

function editFlagNode(tree,node){
	 mini.prompt("请输入标签名称："+node.text, "编辑标签",
	            function (action, value) {
	                if (action == "ok") {
		                if(value == null || value == ""){
		               		 mini.alert("标签名称不能为空！");
		               		 return false;
		               	}else{
		               		$.ajax({
		               			url : basePATH+"/flag/updateFlag",
		               			type : "post",
		               			data : {
		               				id : node.id,
		               				name : value
		               			},
		               			dataType : "json",
		               			success : function(result){
		               				if(result.status){
		               					tree.setNodeText(node,value);
		               					return;
		               				}
		               				mini.alert("标签更新失败，标签名称过长！");
		               	        },
		               	        error : function(){
		               	        	mini.alert("标签更新失败，请稍候重试！");
		               	        }
		               		});
		               	}
	                }
	            }
	         );
}

function removeFlagNode(tree,node){
	mini.confirm("确定删除标签["+node.text+"]吗？", "确定？",
    		function(action) {
    			if (action == "ok") {
    				$.ajax({
    					url : basePATH+"/flag/deleteFlag",
    					type : "post",
    					data : {
    						id : node.id
    					},
    					success : function(){
    						tree.load(basePATH+"/main/getTree");
    			        },
    			        error : function(){
    			        	mini.alert("标签删除失败，请稍候重试！");
    			        }
    				});
    			}
    		}
    	);
}

$(function() {
    var onAutocompleteSelect =function(value, data) {  　　
　　//根据返回结果自定义一些操作
    }; 
    var options = {
        serviceUrl: basePATH + "/flag/searchFlag",//获取数据的后台页面
        width: 140,//提示框的宽度
        delimiter: /(,|;)\s*/,//分隔符
        onSelect: onAutocompleteSelect,//选中之后的回调函数
        deferRequestBy: 0, //单位微秒
        noCache: true //是否启用缓存 默认是开启缓存的
    };
    a1 = $('#flagNameInput').autocomplete(options);
                
});

function selectFlag(){
	var nodeName = $("#flagNameInput").val();
	if(!nodeName){
		return;
	}
	
	$.ajax({
		url : basePATH + "/flag/getFlagId",
		type : "post",
		data : {
			flagName : nodeName
		},
		dataType : 'json',
		success : function(result) {
			var flagId = "flag" + result;
			var tree = mini.get("menuTree");
			var node = tree.getNode(flagId);
			if(node){
				tree.scrollIntoView(node);
				tree.selectNode(node);
			}
		},
		error : function() {
			
		}
	});
}