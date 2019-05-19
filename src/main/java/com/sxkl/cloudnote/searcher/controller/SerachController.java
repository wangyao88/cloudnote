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
    public ModelAndView result(@RequestParam("words") String words){
        ModelAndView modelAndView = new ModelAndView(StringUtils.appendJoinEmpty("baidusearch/search_result","_",PropertyUtil.getMode()));
        long count = searchService.count(words);
        modelAndView.addObject("words", words);
        modelAndView.addObject("count", count);
        return modelAndView;
    }

    @RequestMapping("/page")
    @ResponseBody
    public JSONObject page(@RequestParam("first") int first, @RequestParam("words") String words,
                           @RequestParam("page") int page, @RequestParam("size") int size,
                           @RequestParam("count") int count){
        JSONObject json = new JSONObject();
        if(first == 1) {
            long total = searchService.total();
            DecimalFormat df = new DecimalFormat("#0.00");
            String rate = StringUtils.appendJoinEmpty(df.format(count*100d/total), "%");
            json.put("count", count);
            json.put("total", total);
            json.put("rate", rate);
        }
        long start = System.currentTimeMillis();
        List<Article> articles = searchService.searchPage(words, page, size);
        long end = System.currentTimeMillis();
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
    public List<Article> recommendArticles() {
        return searchService.getRecommendArticles();
    }

    @RequestMapping("/todayNews")
    @ResponseBody
    public List<Object> todayNews() {
        return searchService.getTodayNews();
    }
}
