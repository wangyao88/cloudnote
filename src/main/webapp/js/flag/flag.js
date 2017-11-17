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
	 mini.prompt("请输入标签名称：", "请输入",
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
		               			success : function(){
		               				tree.setNodeText(node,value);
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