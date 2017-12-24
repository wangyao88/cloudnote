package com.sxkl.cloudnote.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.spider.service.SpiderService;

@Controller
@RequestMapping("/spider")
public class SpiderController {
	
	@Autowired
	private SpiderService spiderService;
	
	@RequestMapping("/index")
	public ModelAndView index(){
		ModelAndView modelAndView = new ModelAndView("spider/index");
		return modelAndView;
	}
	
	@RequestMapping("/getAll")
	@ResponseBody
	public String getAll(){
		return spiderService.getArticles();
	}
	
	@RequestMapping("/fetch")
	@ResponseBody
	public String fetch(String id){
		return spiderService.getArticleById(id);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(String id){
		spiderService.delete(id);
	}
	
	@RequestMapping("/deleteAll")
	@ResponseBody
	public void deleteAll(){
		spiderService.deleteAll();
	}
	
	@RequestMapping("/spider")
	@ResponseBody
	public void spider(){
		spiderService.spider();
	}
}
