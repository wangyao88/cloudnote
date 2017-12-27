package com.sxkl.cloudnote.user.controller;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
import com.sxkl.cloudnote.utils.DESUtil;
import com.sxkl.cloudnote.utils.UUIDUtil;

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
	
	@RequestMapping(value = "/login/registe", method = { RequestMethod.GET, RequestMethod.HEAD })
	public ModelAndView registeIndex() {
		ModelAndView modelAndView = new ModelAndView("login/registe");
		return modelAndView;
	}
	
	@RequestMapping(value = "/login/checkName", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkName(String name) {
		User user = userService.getUserByName(name);
		if(Objects.isNull(user)){
			return true;
		}
		return false;
	}

	@RequestMapping(value = "/login/registe", method = RequestMethod.POST)
	@ResponseBody
	public boolean registe(String name, String password, String repassword) {
		if(checkName(name) || checkPassword(password,repassword)){
			DESUtil desUtil = new DESUtil();
			User user = new User();
			user.setName(name);
			user.setPassword(desUtil.encrypt(password));
			userService.registe(user);
			return true;
		}
		return false;
	}

	private boolean checkPassword(String password, String repassword) {
		return !StringUtils.isEmpty(password) && !StringUtils.isEmpty(repassword) && password.equals(repassword);
	}
}
