package com.sxkl.cloudnote.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@RequestMapping(value = "/onLineNum", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String onLineNum(HttpServletRequest request){
		try {
			int onLineNum = Constant.onLineNum();
			return OperateResultService.configurateSuccessResult(onLineNum);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
}
