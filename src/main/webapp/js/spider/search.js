function searchToggle(obj, evt){
	var container = $(obj).closest('.search-wrapper');
	if(!container.hasClass('active')){
		  container.addClass('active');
		  evt.preventDefault();
		  return;
	}
    if(container.hasClass('active') && $(obj).closest('.input-holder').length == 0){
		  container.removeClass('active');
		  // clear input
		  container.find('.search-input').val('');
		  // clear and hide result container when we press close
		  container.find('.result-container').fadeOut(100, function(){$(this).empty();});
		  return;
	}
	if(container.hasClass('active')){
		search();
	}
}

function submitFn(obj, evt){
	value = $(obj).find('.search-input').val().trim();

	_html = "您搜索的关键词： ";
	if(!value.length){
		_html = "关键词不能为空。";
	}
	else{
		_html += "<b>" + value + "</b>";
	}

	$(obj).find('.result-container').html('<span>' + _html + '</span>');
	$(obj).find('.result-container').fadeIn(100);

	evt.preventDefault();
}

$(function() {
    var onAutocompleteSelect =function(value, data) {  　　
　　//根据返回结果自定义一些操作
    }; 
    var options = {
        serviceUrl: basePATH + "spider/searchKey",//获取数据的后台页面
        width: 700,//提示框的宽度
        delimiter: /(,|;)\s*/,//分隔符
        onSelect: onAutocompleteSelect,//选中之后的回调函数
        deferRequestBy: 0, //单位微秒
        params: { country: 'Yes' },//参数
        noCache: false //是否启用缓存 默认是开启缓存的
    };

    a1 = $('#searchText').autocomplete(options);
                
});



function search(){
	var searchKey = $("#searchText").val();
	if(!searchKey){
		return;
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