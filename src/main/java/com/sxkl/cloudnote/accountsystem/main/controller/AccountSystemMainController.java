package com.sxkl.cloudnote.accountsystem.main.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author: wangyao
 * @date: 2018年5月8日 上午9:20:56
 * @description: 
 */
@Controller
@RequestMapping("/accountsystem/main")
public class AccountSystemMainController {

	
	@RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.HEAD})
	public ModelAndView login(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView("accountsystem/main/index");
		return modelAndView;
	}
}
