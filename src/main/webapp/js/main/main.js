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
			var title = "";
			var content = "<table align='center' border='1px' style='width:480px;margin-left=20px;'>" +
			                  "<thead>"+
				                  "<th>日期</th>"+
				                  "<th>天气</th>"+
				                  "<th>温度</th>"+
				                  "<th>风力</th>"+
			                  "</thead>"+
			                  "<tbody>";
			$(result).each(function(index,weather) {
				if(index == 0){
					title = weather.city + "-未来七天天气预报";
				}
				content += "<tr align='center'>"+
								"<td style='width=90px;'>"+weather.date+"</td>"+
								"<td style='width=250px;'>"+weather.status+"</td>"+
								"<td style='width=70px;'>"+weather.temprature+"</td>"+
								"<td style='width=100px;'>"+weather.wind+"</td>"+
								"</tr>";
			});
			content += "</tbody></table>";
			mini.showMessageBox({
	            showModal: false,
	            width: 500,
	            height: 280,
	            title: title,
	            message: content,
	            timeout: 10000,
	            x: "right",
	            y: "bottom"
	        });
		},
		error : function(XMLHttpRequest, textStatus, errorThrown){
			mini.showMessageBox({
	            showModal: false,
	            width: 300,
	            height: 280,
	            title: '天气预报',
	            message: '<center>暂无天气预报信息</center>',
	            timeout: 3000,
	            x: "right",
	            y: "bottom"
	        });
		}
	});
}

function selectSkin(){
	var skin = mini.get("skin").getValue();
	document.cookie="miniuiSkin="+skin;
	location.reload();
}

function setSkin(){
	var skin = getCookie("miniuiSkin");
	mini.get("skin").setValue(skin);
}

function openwaitingtask(){
	mini.open({
		url : bootPATH + "waitingtask/waitingtask.jsp",
		title : "待办任务",
		width : 1200,
		height : 700,
		onload : function() {
            
		},
		ondestroy : function(action) {
			
		}
	})
}

function openWebchatPage(){
	mini.open({
		url : bootPATH + "webchat/webchat.jsp",
		title : "聊天窗口",
		width : 800,
		height : 650,
		allowResize: false,
	    allowDrag: true,
	    showMaxButton: true, 
	    showMinButton: true, 
	    showModal: false,
		onload : function() {
		},
		ondestroy : function(action) {
			if (action == "ok") {
				
			}
		}
	})
}

function showClock() {
	var clock = $("#clock");
	var date = new Date();
	var year = date.getFullYear(); //获取当前年份   
	var mon = date.getMonth() + 1; //获取当前月份   
	var da = date.getDate(); //获取当前日   
	var day = date.getDay(); //获取当前星期几   
	var h = date.getHours(); //获取小时   
	var m = date.getMinutes(); //获取分钟   
	var s = date.getSeconds(); //获取秒   
	if(s < 10){
		s = "0" + s;
	}
	var dayStr = getDay(day);
	var dateStr = year + '年' + mon + '月' + da + '日' + ' ' + h + ':' + m + ':' + s + ' ' + '星期' + dayStr ;
	clock.html(dateStr);
}

function getDay(day) {
	if(day == 0) {
		return "日";
	}else if (day == 1) {
        return "一";
    }else if (day == 2) {
        return "二";
    }else if (day == 3) {
        return "三";
    }else if (day == 4) {
        return "四";
    }else if (day == 5) {
        return "五";
    }else if (day == 6) {
        return "六";
    }
}

function addQuickKey(){
	$(document).keydown(function(e) {
		var keyCode = e.keyCode || e.which || e.charCode;
		var shiftKey = e.shiftKey || e.metaKey;
		if (shiftKey && keyCode == 84) {//t
			getWeather();
		}
		if (shiftKey && keyCode == 78) {//n
			oneNews();
		}
	});
}

$(document).ready(function(){
	addQuickKey();
	getWeather();
	// window.setTimeout(oneNews, 15000)
	// window.setInterval(oneNews, 1800000);
    setSkin();
	setInterval(showClock,1000);
});
