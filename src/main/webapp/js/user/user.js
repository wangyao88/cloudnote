function logout() {
	$.ajax({
		url : basePATH + "/logout",
		type : "get",
		success : function(result){
			if(result){
				window.location.href = basePath + "login"; 
			}
        }
	});
}

function onLineNum(){
	$.ajax({
			url : basePATH+"/user/onLineNum",
			type : "post",
			dataType : "json",
			success : function(result){
				mini.showTips({
		            showModal: false,
		            state : "info",
		            content: "目前在线人数为"+result.data+"人",
		            timeout: 3000,
		            x: 'center',
		            y: 'center'
		        });
	        }
		});
}