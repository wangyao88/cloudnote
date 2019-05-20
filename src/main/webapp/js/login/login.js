function doLogin(){
	$.ajax({
		url : basePath+"login/getPublicKey",
		type : "post",
		success : function(publicKey){
			 var password = $("#password").val();
			 var encrypt = new JSEncrypt();
			 encrypt.setPublicKey(publicKey);
			 var encryptPassword = encrypt.encrypt(password);
			 $("#passwordFront").val(encryptPassword);
			 $.ajax({
					url : basePath+"login",
					type : "post",
					data : {
						userName : $("#userName").val(),
						password : $("#passwordFront").val()
					},
					success : function(result){
						if("login/login" == result){
							layer.alert("用户名或密码错误", {
				    			skin : 'layui-layer-lan',
				    			closeBtn : 1,
				    			anim : 4 //动画类型
				    		}) ;
							return false;
						}
                        document.cookie="miniuiSkin=metro-green";
						window.location.href = basePath + result; 
			        }
				});
        }
	});
}

$("#submitBtn").click(function () {
	doLogin();
});

$(document).keydown(function(e) {
	var keyCode = e.keyCode || e.which || e.charCode;
	if(keyCode == 13){
		doLogin();
	}
});
