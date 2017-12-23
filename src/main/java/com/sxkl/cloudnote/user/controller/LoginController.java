package com.sxkl.cloudnote.user.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.sxkl.cloudnote.user.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	public ModelAndView all() {
		ModelAndView modelAndView = new ModelAndView("login/login");
		return modelAndView;
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.HEAD })
	public ModelAndView login(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("login/login");
		return modelAndView;
	}

	/**
	 * 登录
	 * @param httpServletRequest
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest httpServletRequest, RedirectAttributesModelMap modelMap) {
		return userService.login(httpServletRequest, modelMap);
	}
	
	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.HEAD })
	public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		userService.logout(request);
		return "redirect:/login";
	}

}
