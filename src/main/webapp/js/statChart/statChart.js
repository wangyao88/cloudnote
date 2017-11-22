function openStatChart(){
	mini.open({
		url : bootPATH + "statChart/statChart.jsp",
		title : "报表管理",
		width : 1000,
		height : 700,
		onload : function() {
			//                var iframe = this.getIFrameEl();
			//                iframe.contentWindow.SetData(node);
		},
		ondestroy : function(action) {
			if (action == "ok") {
				
			}
		}
	})
}

function monthArticleColumn(){
	$.ajax({
		url : basePATH + "/article/addArticle",
		type : "post",
		success : function(result) {
			var myChart = echarts.init(document.getElementById('monthArticleColumn'));
			var option = result.data;
			myChart.setOption(option);
		},
		error : function() {
			mini.alert("笔记添加失败，请稍候重试！");
		}
	});
	
	
	
	
//	 // 基于准备好的dom，初始化echarts实例
//    var myChart = echarts.init(document.getElementById('monthArticleColumn'));
//
//    // 指定图表的配置项和数据
//    var option = {
//        tooltip: {},
//        legend: {
//            data:['销量']
//        },
//        xAxis: {
//            data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
//        },
//        yAxis: {},
//        series: [{
//            name: '销量',
//            type: 'bar',
//            data: [5, 20, 36, 10, 10, 20]
//        }]
//    };

    // 使用刚指定的配置项和数据显示图表。
//    myChart.setOption(option);
}


$(document).ready(function(){
	monthArticleColumn();
});