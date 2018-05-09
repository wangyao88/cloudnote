package com.sxkl.cloudnote.accountsystem.tally.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.accountsystem.tally.service.TallyService;

/**
 * @author: wangyao
 * @date: 2018年5月8日 上午9:20:56
 * @description: 
 */
@Controller
@RequestMapping("/accountsystem/tally")
public class TallyController {

	@Autowired
	private TallyService tallyService;
	
	@RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.HEAD})
	public ModelAndView login(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView("accountsystem/tally/index");
		return modelAndView;
	}
	
	@RequestMapping(value = "/getTallyList", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getTallyList(HttpServletRequest request){
		return tallyService.getTallyList(request);
	}
}
