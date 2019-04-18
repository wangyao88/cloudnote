package com.sxkl.cloudnote.searcher.controller;


import com.mashape.unirest.http.exceptions.UnirestException;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.searcher.service.SearchService;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SerachController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView(StringUtils.appendJoinEmpty("baidusearch/search_index","_",PropertyUtil.getMode()));
        return modelAndView;
    }

    @RequestMapping("/result")
    public ModelAndView result(@RequestParam("words") String words, @RequestParam("page") int page, @RequestParam("size") int size){
        ModelAndView modelAndView = new ModelAndView(StringUtils.appendJoinEmpty("baidusearch/search_result","_",PropertyUtil.getMode()));
        List<Article> articles = searchService.searchPage(words, page, size);
        long count = searchService.count(words);
        modelAndView.addObject("words", words);
        modelAndView.addObject("articles", articles);
        modelAndView.addObject("count", count);
        return modelAndView;
    }

    @RequestMapping("/page")
    @ResponseBody
    public List<Article> page(@RequestParam("words") String words, @RequestParam("page") int page, @RequestParam("size") int size){
        return searchService.searchPage(words, page, size);
    }
}
