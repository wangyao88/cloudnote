function backupDB(){
    $.ajax({
        url : basePATH+"/backup/backupDB",
        type : "post",
        success : function(){
            mini.alert("备份数据成功！");
        },
        error : function(){
            mini.alert("备份数据失败，请稍候重试！");
        }
    });
}