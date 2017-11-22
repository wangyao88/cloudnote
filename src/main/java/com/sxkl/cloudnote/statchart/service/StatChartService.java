package com.sxkl.cloudnote.statchart.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.Trigger;

@Service
public class StatChartService {
	

	public String monthArticleColumn(HttpServletRequest request) {
//		     var option = {
//		         tooltip: {},
//		         legend: {
//		             data:['销量']
//		         },
//		         xAxis: {
//		             data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
//		         },
//		         yAxis: {},
//		         series: [{
//		             name: '销量',
//		             type: 'bar',
//		             data: [5, 20, 36, 10, 10, 20]
//		         }]
//		     };
		Option option = new Option();
		
		option.tooltip(Trigger.axis)
			  .legend("数量(篇)");
		return null;
	}

}
