function backupDB(){
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '数据备份中...'
	});
	
    $.ajax({
        url : basePATH+"/backup/backupDB",
        type : "post",
        success : function(){
        	mini.unmask(document.body);
            mini.alert("备份数据成功！");
        },
        error : function(){
        	mini.unmask(document.body);
            mini.alert("备份数据失败，请稍候重试！");
        }
    });
}