function backupDB(){
	mini.mask({
		el : document.body,
		cls : 'mini-mask-loading',
		html : '数据备份中...'
	});
	
    $.ajax({
        url : basePATH+"/backup/backupDB",
        type : "post",
        dataType : "json",
        success : function(result){
        	mini.unmask(document.body);
        	var path = result.data;
        	downloadDB(path);
        },
        error : function(){
        	mini.unmask(document.body);
            mini.alert("备份数据失败，请稍候重试！");
        }
    });
}

function downloadDB(filePath){
	mini.confirm("备份数据成功！下载备份文件吗？", "确定？",
    		function(action) {
    			if (action == "ok") {
                    var hrefPath = basePATH+"/file/downloadDB?filePath="+filePath;
    				mini.open({
		           		url : bootPATH + "backup/downloadDB.jsp",
		        		title : "下载备份文件",
		        		width : 350,
		        		height : 54,
		        		onload : function() {
			                var iframe = this.getIFrameEl();
			                iframe.contentWindow.SetData(filePath,hrefPath);
		        		},
		        		ondestroy : function(action) {
		        			
		        		}
		        	})
    			}
    		}
    	);
}