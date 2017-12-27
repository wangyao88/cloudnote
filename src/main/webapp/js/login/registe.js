function onNameFocus(){
	var name = $("#name").val();
	if(name == '昵称'){
		$("#name").val('');
		return;
	}
	$("#name").val(name);
}

function onNameBlur(){
	var name = $("#name").val();
	if(name == ''){
		$("#name").val('昵称');
		return;
	}
	$("#name").val(name);
	
	$.ajax({
		url : basePATH + "login/checkName",
		type : "post",
		data : {
			name : name
		},
		success : function(result) {
			if(!result){
				layer.msg('昵称【'+name+'】已存在');
			}
		},
		error : function(){
		}
	});
}

function onPasswordFocus(){
	var password = $("#password").val();
	if(password == '密码'){
		$("#password").val('');
		return;
	}
	$("#password").val(password);
}

function onPasswordBlur(){
	var password = $("#password").val();
	if(password == ''){
		$("#password").val('密码');
		return;
	}
	$("#password").val(password);
}

function onRepasswordFocus(){
	var repassword = $("#repassword").val();
	if(repassword == '确认密码'){
		$("#repassword").val('');
		return;
	}
	$("#repassword").val(repassword);
}

function onRepasswordBlur(){
	var repassword = $("#repassword").val();
	if(repassword == ''){
		$("#repassword").val('确认密码');
		return;
	}
	$("#repassword").val(repassword);
}

function submit(){
	var name = $("#name").val();
	var password = $("#password").val();
	var repassword = $("#repassword").val();

	if(password != repassword){
		layer.msg('两次密码不一致', {icon: 5});
		return;
	}
	
	$.ajax({
		url : basePATH + "login/registe",
		type : "post",
		data : {
			name : name,
			password : password,
			repassword : repassword
		},
		success : function(result) {
			if(result){
				layer.confirm('注册成功。现在去登陆？', {
					  btn: ['确定','取消']
					}, function(){
					  window.location.href = basePATH + "login"
					}, function(){
					  
				});
			}
			
			if(!result){
				layer.msg('注册失败！请稍后重试！', {icon: 5});
			}
		},
		error : function(){
			layer.msg('注册失败！请稍后重试！', {icon: 5});
		}
	});
}