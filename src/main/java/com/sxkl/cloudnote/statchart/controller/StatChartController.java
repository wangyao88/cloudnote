package com.sxkl.cloudnote.statchart.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.statchart.service.StatChartService;

@RestController
@RequestMapping("/statchart")
public class StatChartController {
	
	@Autowired
	private StatChartService statChartService;
	
	@RequestMapping(value = "/monthArticleColumn", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	public String monthArticleColumn(HttpServletRequest request){
		try {
			String chart = statChartService.monthArticleColumn(request);
			return OperateResultService.configurateSuccessResult(chart);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}

}
