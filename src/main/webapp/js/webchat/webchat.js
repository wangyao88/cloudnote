function notifyMe(text) {
   if (!("Notification" in window)) {
//     alert("This browser does not support desktop notification");
   }else if (Notification.permission === "granted") {
	   showWin(text);
   }else if (Notification.permission !== 'denied') {
	   Notification.requestPermission(function (permission) {
	       if(!('permission' in Notification)) {
	    	   Notification.permission = permission;
	       }
	       if (permission === "granted") {
	    	   showWin(text);
	       }
	   });
   }
}

function showWin(text){
	var content = text+"给您发来一条新消息";
	var num = getRandom(0,27);
	var url = basePATH + "/images/webchat/"+num+".jpg";
    var notification = new Notification("提醒",{
    	body : content,
    	icon : url
    });
    notification.onshow = function() {  
        setTimeout(function() {  
        	notification.close();  
        }, 5000);  
    };  
    notification.onclick = function() {  
    	notification.close();  
    };  
}

function getRandom(min, max){
    var r = Math.random() * (max - min);
    var re = Math.round(r + min);
    re = Math.max(Math.min(re, max), min)
    return re;
}

mini.parse();

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
	var userTo = mini.get("userTo").getValue();
	if(!userTo){
		mini.alert('请选择好友');
		return;
	}
	var v = webchatInput.getContent();
	if(v == ""){
		mini.alert('请输入发送内容');
		return;
	}
	var data={};
	data["from"]=from;
	data["fromName"]=fromName;
	data["to"]=userTo;
	data["text"]=v;
	websocket.send(JSON.stringify(data));
	var text = data.text;
	$("#content").append("<div class='tmsg'><label class='to name'>我&nbsp;"+new Date().Format("yyyy-MM-dd hh:mm:ss")+"</label><div class='tmsg_text'>"+text+"</div></div>");
	scrollToBottom();
	webchatInput.setContent("");
	gotoMsgInput();
}

function scrollToBottom(){
	var div = document.getElementById('content');
	div.scrollTop = div.scrollHeight;
	div.scrollIntoView(); 
	
	var msg_end = document.getElementById('msg_end');
	msg_end.click(); 
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
		websocket = new WebSocket("wss"+wsPath+"mywebsocket?uid="+uid);
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket("wss"+wsPath+"mywebsocket?uid="+uid);
	} else {
		websocket = new SockJS("https"+wsPath+"mywebsocket/sockjs?uid="+uid);
	}
	websocket.onopen = function(event) {
		console.log("WebSocket:已连接");
		gotoMsgInput();
	};
	websocket.onmessage = function(event) {
		var data=JSON.parse(event.data);
		var textCss=data.from==-1?"sfmsg_text":"fmsg_text";
		var text = data.text;
		$("#content").append("<div class='fmsg'><label class='from name'>"+data.fromName+"&nbsp;"+data.date+"</label><div class='"+textCss+"'>"+text+"</div></div>");
		scrollToBottom();
		gotoMsgInput();
		
		notifyMe(data.fromName);
	};
	websocket.onerror = function(event) {
		console.log("WebSocket:发生错误 ");
		console.log(event);
	};
	websocket.onclose = function(event) {
		console.log("WebSocket:已关闭");
	}
}

function gotoMsgInput(){
//	var msgWin = $("#msg");
//	msgWin.focus();
	webchatInput.focus();
}

//linkToWebsocketServer(from);

//$(document).keydown(function(e) {
//	var keyCode = e.keyCode || e.which || e.charCode;
//	if(keyCode == 13){
//		sendMsg();
//		return false;
//	}
//});

function selectFriend(){
	var userTo = mini.get("userTo").getValue();
	$.ajax({
		url : basePATH + "/msg/getHistory",
		type : "post",
		data : {
			userTo : userTo
		},
		dataType : "json",
		success : function(result) {
			if(result){
				$("#content").empty();
				$(result).each(function(index,msg) { 
					if(msg.to == userTo){
						var text = msg.text;
						$("#content").append("<div class='tmsg'><label class='to name'>我&nbsp;"+msg.dateStr+"</label><div class='tmsg_text'>"+text+"</div></div>");
					}
					if(msg.from == userTo){
						var textCss = "fmsg_text";
						var text = msg.text;
						$("#content").append("<div class='fmsg'><label class='from name'>"+msg.fromName+"&nbsp;"+msg.dateStr+"</label><div class='"+textCss+"'>"+text+"</div></div>");
					}
				});
				scrollToBottom();
			}
		}
	});
	webchatInput.setContent("");
	gotoMsgInput();
}

$(document).ready(function(){
	try {
		linkToWebsocketServer(from);
    } catch (e) {
		console.log(e);
    }
});
			