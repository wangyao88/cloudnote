function search(){
	alert('请输入');
	var searchKey = $("#searchKey").val();
	if(!searchKey){
		alert('请输入');
	}
	$.ajax({
		url : basePATH + "/spider/search",
		type : "post",
		data : {
			searchKey : searchKey
		},
		dataType : 'json',
		success : function(result) {
			
		},
		error : function() {
			
		}
	});
}