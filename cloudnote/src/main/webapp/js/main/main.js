mini.parse();

//右键菜单
function onBeforeOpen(e) {
    var menu = e.sender;
    var tree = mini.get("menuTree");

    var node = tree.getSelectedNode();
    if (!node) {
        e.cancel = true;
        return;
    }
    if (node && node.text == "Base") {
        e.cancel = true;
        //阻止浏览器默认右键菜单
        e.htmlEvent.preventDefault();
        return;
    }

    ////////////////////////////////
    var editItem = mini.getbyName("edit", menu);
    var removeItem = mini.getbyName("remove", menu);
    editItem.show();
    removeItem.enable();

    if (node.id == "forms") {
        editItem.hide();
    }
    if (node.id == "lists") {
        removeItem.disable();
    }
}


//tree 单击事件
function onNodeClick1(){
	var tree = mini.get("menuTree")
    var node = tree.getSelectedNode()
    alert(node.text);
}

//新增节点
function onAddNode(e){
	var tree = mini.get("menuTree");
    var node = tree.getSelectedNode();
    var parentNode = tree.getParentNode(node);
    if(isNoteTreeNode(node)){
		if(parentNode._id == -1){
			mini.open({
		    	url: bootPATH+ "main/taskPanel.jsp",
		            title: "任务面板", width: 500, height: 300,
		            onload: function () {
//		                var iframe = this.getIFrameEl();
//		                iframe.contentWindow.SetData(node);
		            }
		        })
		}else{
			 mini.alert("笔记本不能添加子节点！");
		}
	}
    
}

function addNode(fotmData){
	var tree = mini.get("menuTree");
	var nodeName = fotmData.name;
	if(nodeName == null || nodeName == ""){
		 mini.alert("笔记本名称不能为空！");
	}else{
		$.ajax({
			url : basePATH+"/note/addNote",
			type : "post",
			data : {
				name : fotmData.name
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

function onRemoveNode(e){
	var tree = mini.get("menuTree");
    var node = tree.getSelectedNode();
    var childrenNodes = tree.getChildNodes(node);
    if(childrenNodes != null && childrenNodes.length > 0){
    	mini.alert("不允许删除!有子节点！");
    	return false;
	}
    if(isNoteTreeNode(node)){
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
    
    if(isFlagTreeNode(node)){
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
    			        	mini.alert("标签添加失败，请稍候重试！");
    			        }
    				});
    			}
    		}
    	);
    }
}


function  isNoteTreeNode(node){
	 var nodeId = node.id;
	 if(nodeId.substring(0,4) == 'note'){
		 return true;
	 }
	 return false;
}

function  isFlagTreeNode(node){
	 var nodeId = node.id;
	 if(nodeId.substring(0,4) == 'flag'){
		 return true;
	 }
	 return false;
}