package com.sxkl.cloudnote.accountsystem.accountbook.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.accountsystem.accountbook.entity.AccountBook;
import com.sxkl.cloudnote.accountsystem.accountbook.service.AccountBookService;

/**
 * @author: wangyao
 * @date: 2018年5月8日 上午9:20:56
 * @description: 
 */
@Controller
@RequestMapping("/accountsystem/accountbook")
public class AccountBookController {

	@Autowired
	private AccountBookService accountBookService;
	
	@RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.HEAD})
	public ModelAndView login(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView("accountsystem/accountbook/index");
		return modelAndView;
	}
	
	@RequestMapping(value = "/getAccountBookList", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getAccountBookList(HttpServletRequest request){
		return accountBookService.getAccountBookList(request);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String add(AccountBook accountBook, HttpServletRequest request){
		return accountBookService.add(accountBook,request);
	}
	
	@RequestMapping(value = "/saveChanges", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String saveChanges(String data, HttpServletRequest request){
		return accountBookService.saveChanges(data,request);
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String remove(String id, HttpServletRequest request){
		return accountBookService.remove(id,request);
	}
}
