var publicKey;

var options={  
    beforeSerialize: modifySubmitData,  //提交到Action时，可以自己对某些值进行处理。  
    url : basePath+'login',    //默认是form的action，如果申明，则会覆盖  
    type : "POST",    // 默认值是form的method("GET" or "POST")，如果声明，则会覆盖  
    clearForm : true,    // 成功提交后，清除所有表单元素的值  
    resetForm : true,    // 成功提交后，重置所有表单元素的值  
    timeout : 10000,    // 限制请求的时间，当请求大于3秒后，跳出请求
    success : showResponse
}  

$("#submitBtn").click(function () {
	$.ajax({
		url : basePath+"login/getPublicKey",
		type : "post",
		success : function(publicKey){
			 publicKey = publicKey;
			 $("#loginForm").ajaxSubmit(options);
		     return false;
        }
	});
});

function modifySubmitData(){
	var password = $("#password").val();
	var encrypt = new JSEncrypt();
	encrypt.setPublicKey(publicKey);
	var data = encrypt.encrypt(password);
	$("#password").val(data);
}

function showResponse(responseText, statusText){
	window.location.href = basePath + responseText; 
}
