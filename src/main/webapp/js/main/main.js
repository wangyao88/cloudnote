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
    var node = tree.getSelectedNode();
	var parentNode = tree.getParentNode(node);
	if(parentNode._id == -1){
		return false;
	}
	var data = {
			first : false
	};
	if(isNoteTreeNode(node)){
		data.noteId = node.id;
	}
	if(isFlagTreeNode(node)){
		data.flagId = node.id;
	}
	
//	var grid = mini.get("articleGrid");
//	 grid.loading();
//     grid.load(data);
//     grid.unmask();
//     grid.hideColumn("idColumn");
//     grid.select(0);
//	 	var record = grid.getSelected();
//	 	getArticleById(record.id);
	
	loadArticles(data);
}

//新增节点
function onAddNode(e){
	var tree = mini.get("menuTree");
    var node = tree.getSelectedNode();
    var parentNode = tree.getParentNode(node);
    if(isNoteTreeNode(node)){
		if(parentNode._id == -1){
//			mini.open({
//		    	url: bootPATH+ "main/taskPanel.jsp",
//		            title: "任务面板", width: 500, height: 300,
//		            onload: function () {
//		                var iframe = this.getIFrameEl();
//		                iframe.contentWindow.SetData(node);
//		            }
//		        })
			addNoteNode(tree);
		  }else{
			   mini.alert("笔记本不能添加子节点！");
		  }
	}
    
    if(isFlagTreeNode(node)){
    	addFlagNode(tree);
    }
    
}

function onEditNode(){
	var tree = mini.get("menuTree");
    var node = tree.getSelectedNode();
    var parentNode = tree.getParentNode(node);
    if(parentNode._id == -1){
		 mini.alert("根节点不能编辑！");
		 return;
	}
    
    if(isNoteTreeNode(node)){
    	editNoteNode(tree,node);
	}
    if(isFlagTreeNode(node)){
    	editFlagNode(tree,node);
    }
}

function onRemoveNode(e){
	var tree = mini.get("menuTree");
    var node = tree.getSelectedNode();
    var parentNode = tree.getParentNode(node);
    if(parentNode._id == -1){
		 mini.alert("根节点不能删除！");
		 return;
	}
    var childrenNodes = tree.getChildNodes(node);
    if(childrenNodes != null && childrenNodes.length > 0){
    	mini.alert("不允许删除!有子节点！");
    	return false;
	}
    if(isNoteTreeNode(node)){
    	removeNoteNode(tree,node);
    }
    
    if(isFlagTreeNode(node)){
    	removeFlagNode(tree,node);
    }
}


function isNoteTreeNode(node){
	 if(node && node.id.substring(0,4) == 'note'){
		 return true;
	 }
	 return false;
}

function  isFlagTreeNode(node){
	 if(node &&  node.id.substring(0,4) == 'flag'){
		 return true;
	 }
	 return false;
}

function oneNews(){
	$.ajax({
		url : basePATH + "/spider/oneNews",
		type : "get",
		dataType : "json",
		success : function(result) {
			var content = '<font size="20"><strong>' + 
						   		result.date + 
						   		"<br>" + 
						   		'<a target="_blank" href="' +result.href+'">'+
						   		    result.content + 
						   		'</a>' +
						  '</strong></font>' +
						  "<br>" + 
						 '<img style="margin-top:3px" width="280px" height="200px" src="'+result.image+'"></img>';
			mini.showMessageBox({
	            showModal: false,
	            width: 300,
	            height: 280,
	            title: result.source,
	            message: content,
	            timeout: 10000,
	            x: "right",
	            y: "bottom"
	        });
		}
	});
}

function getWeather(){
	$.ajax({
		url : basePATH + "/main/getWeather",
		type : "get",
		dataType : "json",
		timeout: 100000,
		success : function(result) {
			console.log(result);
			var title = "";
			var content = "<table>";
			$(result).each(function(index,weather) { 
				if(index == 0){
					title = weather.city + "-未来七天天气预报";
				}
				content += "<tr><td>"+weather.date+"</td><td>"+weather.status+"</td><td>"+weather.temprature+"</td><td>"+weather.wind+"</td><td></tr>"
			});
			content += "</table>";
			mini.showMessageBox({
	            showModal: false,
	            width: 340,
	            height: 280,
	            title: title,
	            message: content,
	            timeout: 8000,
	            x: "right",
	            y: "bottom"
	        });
		}
	});
}

$(document).ready(function(){
	getWeather();
	window.setTimeout(oneNews, 10000)
	window.setInterval(oneNews, 1800000); 
});
