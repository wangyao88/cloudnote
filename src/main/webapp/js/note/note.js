function addNoteNode(tree){
	mini.prompt("请输入笔记本名称：", "请输入",
            function (action, value) {
                if (action == "ok") {
	                if(value == null || value == ""){
	               		 mini.alert("笔记本名称不能为空！");
	               		 return false;
	               	}else{
	               		$.ajax({
	               			url : basePATH+"/note/addNote",
	               			type : "post",
	               			data : {
	               				name : value
	               			},
	               			success : function(){
	               				tree.load(basePATH+"/main/getTree");
	               	        },
	               	        error : function(){
	               	        	mini.alert("笔记本添加失败，请稍候重试！");
	               	        }
	               		});
	               	}
                }
            }
         );
}

function editNoteNode(tree,node){
    mini.prompt("请输入笔记本名称：", "请输入",
            function (action, value) {
                if (action == "ok") {
	                if(value == null || value == ""){
	               		 mini.alert("笔记本名称不能为空！");
	               		 return false;
	               	}else{
	               		$.ajax({
	               			url : basePATH+"/note/updateNote",
	               			type : "post",
	               			data : {
	               				id : node.id,
	               				name : value
	               			},
	               			success : function(){
	               				tree.setNodeText(node,value);
	               	        },
	               	        error : function(){
	               	        	mini.alert("笔记本更新失败，请稍候重试！");
	               	        }
	               		});
	               	}
                }
            }
         );
}

function removeNoteNode(tree,node){
	mini.confirm("确定删除笔记本？", "确定？",
    		function(action) {
    			if (action == "ok") {
    				$.ajax({
    					url : basePATH+"/note/deleteNote",
    					type : "post",
    					data : {
    						id : node.id
    					},
    					success : function(){
    						tree.load(basePATH+"/main/getTree");
    			        },
    			        error : function(){
    			        	mini.alert("笔记本添加失败，请稍候重试！");
    			        }
    				});
    			}
    		}
    	);
}