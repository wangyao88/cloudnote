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
var finishedRateChart;

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
	var data = grid.getChanges();
	if(data){
		mini.alert("请先保存!");
	}
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
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '保存中...'
	});
	$.ajax({
		url : url,
		data : {
			waitingTaskStr : json
		},
		type : "post",
		success : function(text) {
			grid.reload();
			mini.unmask(document.body);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.responseText);
			mini.unmask(document.body);
		}
	});
}

function onDrawCell(e){
	var node = e.node,
    column = e.column,
    field = e.field,
    value = e.value;
	//进度
	if (field == "process") {
		if(value){
//			e.cellHtml =  '<div class="mini-progressbar" value="'+value+'"></div>';
			e.cellHtml = '<div class="mini-progressbar">'
                + '<div class="progressbar-percent" style="width:' + value + 'px;"></div>'
                + '<div class="progressbar-label">' + value + '</div>'
            +'</div>';
		}
	}
}

function linkToWaitingTaskWebsocketServer(uid){
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws"+wsPath+"mywebsocket?uid="+uid);
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("ws"+wsPath+"mywebsocket?uid="+uid);
	} else {
		websocket = new SockJS("http"+wsPath+"mywebsocket/sockjs?uid="+uid);
	}
	websocket.onopen = function(event) {
		mini.showTips({
            showModal: false,
            state : "info",
            content: "开始工作",
            timeout: 3000,
            x: 'center',
            y: 'center'
        });
	};
	websocket.onmessage = function(event) {
		var data = JSON.parse(event.data);
		mini.showTips({
            showModal: false,
            state : "info",
            content: "尚有"+data.count+"个任务未完成",
            timeout: 3000,
            x: 'center',
            y: 'center'
        });
		drawFinishedRateChart(data.rate);
	};
	websocket.onerror = function(event) {
		console.log("WebSocket:发生错误 ");
		console.log(event);
	};
	websocket.onclose = function(event) {
		console.log("WebSocket:已关闭");
		console.log(event);
	}
}

function drawFinishedRateChart(rate){
	option = {
		    tooltip : {
		        formatter: "{a} <br/>{b} : {c}%"
		    },
		    series : [
		        {
		            name:'待办任务',
		            type:'gauge',
		            detail : {formatter:'{value}%'},
		            data:[{value: 50, name: '完成率'}]
		        }
		    ]
		};

	option.series[0].data[0].value = rate;
    finishedRateChart.setOption(option, true);
}

$(document).ready(function(){
	mini.parse();
	grid = mini.get("waitingtaskGrid");
	try {
		grid.load();
    } catch (e) {
		console.log(e);
    }
	//绑定表单
	var db = new mini.DataBinding();
	db.bindForm("waitingtaskEditForm", grid);
	linkToWaitingTaskWebsocketServer(uid);
//	finishedRateChart = echarts.init(document.getElementById('finishedRateChart'));
//	drawFinishedRateChart();
});