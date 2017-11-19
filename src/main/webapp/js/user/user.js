function logout() {
	//	logout
	$.ajax({
		url : basePATH + "/logout",
		type : "get"
	});
}

function onLineNum(){
	$.ajax({
			url : basePATH+"/user/onLineNum",
			type : "post",
			dataType : "json",
			success : function(result){
				mini.showMessageBox({
		            showModal: false,
		            width: 250,
		            title: "提示",
		            iconCls: "mini-messagebox-warning",
		            message: "目前在线人数为"+result.data+"人",
		            timeout: 3000,
		            x: 'right',
		            y: 'bottom'
		        });
	        }
		});
}