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

var TaskTypes = [ {
	id : 'WORK',
	text : '工作'
}, {
	id : 'STUDY',
	text : '学习'
}, {
	id : 'LIFE',
	text : '生活'
} ];
var grid;
$(document).ready(function(){
	mini.parse();
	grid = mini.get("waitingtaskGrid");
	grid.load();
	//绑定表单
	var db = new mini.DataBinding();
	db.bindForm("waitingtaskEditForm", grid);
});


///////////////////////////////////////////////////////
function onTaskTypeRenderer(e) {
	for (var i = 0, l = TaskTypes.length; i < l; i++) {
		var g = TaskTypes[i];
		if (g.id == e.value) return g.text;
	}
	return "";
}


//////////////////////////////////////////////////////
function addWaitingTask() {
	var newRow = {
		name : "新建待办任务"
	};
	grid.addRow(newRow, 0);

	grid.deselectAll();
	grid.select(newRow);
}
function deleteWaitingTask() {
	var rows = grid.getSelecteds();
	if (rows.length > 0) {
		grid.removeRows(rows, true);
		$.ajax({
			url : basePath+"waitingtask/delete",
			data : {
				id : rows[0].id
			},
			type : "post",
			success : function(text) {
				grid.reload();
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert(jqXHR.responseText);
			}
		});
	}
}
function insertWaitingTask() {
	var data = grid.getChanges();
	if(!data){
		return;
	}
	var url = basePath+"waitingtask/";
	if(data[0].id){
		url = url + "update";
	}else{
		url = url + "insert";
	}
	var json = mini.encode(data[0]);
	var id = json.id;
	grid.loading("保存中，请稍后......");
	$.ajax({
		url : url,
		data : {
			waitingTaskStr : json
		},
		type : "post",
		success : function(text) {
			grid.reload();
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.responseText);
		}
	});
}