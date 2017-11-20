function openWebchatPage(){
	mini.open({
		url : bootPATH + "webchat/webchat.jsp",
		title : "聊天窗口",
		width : 1000,
		height : 700,
		allowResize: false,
	    allowDrag: false,
	    showMaxButton: true, 
	    showMinButton: true, 
		onload : function() {
			//                var iframe = this.getIFrameEl();
			//                iframe.contentWindow.SetData(node);
		},
		ondestroy : function(action) {
			if (action == "ok") {
				
			}
		}
	})
}

var websocket;

function send(event){
	var code;
	 if(window.event){
		 code = window.event.keyCode; // IE
	 }else{
		 code = event.which; // Firefox
	 }
	if(code==13){ 
		sendMsg();            
	}
}

function clearAll(){
	$("#content").empty();
}

function sendMsg(){
	var v=$("#msg").val();
	if(v==""){
		return;
	}else{
		var data={};
		data["from"]=from;
		data["fromName"]=fromName;
		data["to"]='fdsfds';
		data["text"]=v;
		websocket.send(JSON.stringify(data));
		$("#content").append("<div class='tmsg'><label class='name'>我&nbsp;"+new Date().Format("yyyy-MM-dd hh:mm:ss")+"</label><div class='tmsg_text'>"+data.text+"</div></div>");
		scrollToBottom();
		$("#msg").val("");
	}
}

function scrollToBottom(){
	var div = document.getElementById('content');
	div.scrollTop = div.scrollHeight;
}

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function linkToWebsocketServer(uid){
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://127.0.0.1:8080/cloudnote/mywebsocket?uid="+uid);
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("ws://127.0.0.1:8080/cloudnote/mywebsocket?uid="+uid);
	} else {
		websocket = new SockJS("http://127.0.0.1:8080/cloudnote/mywebsocket/sockjs?uid="+uid);
	}
	websocket.onopen = function(event) {
		console.log("WebSocket:已连接");
		console.log(event);
	};
	websocket.onmessage = function(event) {
		var data=JSON.parse(event.data);
		console.log("WebSocket:收到一条消息",data);
		var textCss=data.from==-1?"sfmsg_text":"fmsg_text";
		$("#content").append("<div class='fmsg'><label class='name'>"+data.fromName+"&nbsp;"+data.date+"</label><div class='"+textCss+"'>"+data.text+"</div></div>");
		scrollToBottom();
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

console.log('from' + from);
linkToWebsocketServer(from);

$(document).ready(function(){
	
});
			