package com.sxkl.cloudnote.spider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.spider.entity.SearchComplete;
import com.sxkl.cloudnote.spider.service.SpiderService;

@Controller
@RequestMapping("/spider")
public class SpiderController {
	
	@Autowired
	private SpiderService spiderService;
	@Autowired
	private ArticleService articleService;
	
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
	
	@RequestMapping("/getTotal")
	@ResponseBody
	public int getTotal(){
		return spiderService.getTotal();
	}
	
	@RequestMapping("/searchPage")
	public ModelAndView searchPage(){
		ModelAndView modelAndView = new ModelAndView("spider/searchPage");
		return modelAndView;
	}
	
	@RequestMapping("/search")
	public ModelAndView searchIndex(String searchKey){
		System.out.println(searchKey);
		ModelAndView modelAndView = new ModelAndView("spider/searchResult");
		return modelAndView;
	}
	
	@RequestMapping(value="/searchKey", method = RequestMethod.GET)
	@ResponseBody
	public SearchComplete search(String query){
		return articleService.getAllByTitle(query);
	}
	
	@RequestMapping(value="/search", method = RequestMethod.POST)
	@ResponseBody
	public SearchComplete searchOnLine(String query){
		return articleService.getAllByTitle(query);
	}
}
