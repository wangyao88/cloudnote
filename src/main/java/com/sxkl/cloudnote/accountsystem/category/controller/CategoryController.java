package com.sxkl.cloudnote.accountsystem.category.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.accountsystem.category.entity.CategoryFront;
import com.sxkl.cloudnote.accountsystem.category.service.CategoryService;

/**
 * @author: wangyao
 * @date: 2018年5月8日 上午9:20:56
 * @description: 
 */
@Controller
@RequestMapping("/accountsystem/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.HEAD})
	public ModelAndView login(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView("accountsystem/category/index");
		return modelAndView;
	}
	
	@RequestMapping(value = "/getIncomeCategoryTree", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getIncomeCategoryTree(HttpServletRequest request){
		return categoryService.getIncomeCategoryTree(request);
	}
	
	@RequestMapping(value = "/getOutcomeCategoryTree", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getOutcomeCategoryTree(HttpServletRequest request){
		return categoryService.getOutcomeCategoryTree(request);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String add(CategoryFront categoryFront, HttpServletRequest request){
		return categoryService.add(categoryFront,request);
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String update(String id, String name, HttpServletRequest request){
		return categoryService.update(id,name,request);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String delete(String id, HttpServletRequest request){
		return categoryService.delete(id,request);
	}
}
