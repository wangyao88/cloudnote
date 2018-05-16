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
