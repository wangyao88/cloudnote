package com.sxkl.cloudnote.searcher.controller;


import com.alibaba.fastjson.JSONObject;
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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

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
        long start = System.currentTimeMillis();
        List<Article> articles = searchService.searchPage(words, page, size);
        long end = System.currentTimeMillis();
        long count = searchService.count(words);
        long total = searchService.total();
        DecimalFormat df = new DecimalFormat("#.00");
        String rate = StringUtils.appendJoinEmpty(df.format(count*100d/total), "%");
        modelAndView.addObject("words", words);
        modelAndView.addObject("articles", articles);
        modelAndView.addObject("count", count);
        modelAndView.addObject("total", total);
        modelAndView.addObject("rate", rate);
        modelAndView.addObject("pageNum", articles.size());
        modelAndView.addObject("cost", end-start);
        return modelAndView;
    }

    @RequestMapping("/page")
    @ResponseBody
    public JSONObject page(@RequestParam("words") String words, @RequestParam("page") int page, @RequestParam("size") int size){
        long start = System.currentTimeMillis();
        List<Article> articles = searchService.searchPage(words, page, size);
        long end = System.currentTimeMillis();
        JSONObject json = new JSONObject();
        json.put("articles", articles);
        json.put("cost", end-start);
        json.put("pageNum", articles.size());
        return json;
    }

    @RequestMapping("/hotLabel")
    @ResponseBody
    public Set<Object> hotLabel() {
        return searchService.getHotLabel();
    }

    @RequestMapping("/recommendArticles")
    @ResponseBody
    public List<Object> recommendArticles() {
        return searchService.getRecommendArticles();
    }

    @RequestMapping("/todayNews")
    @ResponseBody
    public List<Object> todayNews() {
        return searchService.getTodayNews();
    }
}
