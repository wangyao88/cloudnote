package com.sxkl.cloudnote.article.controller;

import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.article.service.BlogService;
import com.sxkl.cloudnote.log.annotation.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *  * @author wangyao
 *  * @date 2018年3月22日 下午10:47:59
 *  * @description: 个人博客接口
 *  
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private BlogService blogService;

    @Logger(message = "获取推荐博客")
    @RequestMapping(value = "/getRecommend", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getRecommend() {
        return articleService.getRecommend();
    }

    @Logger(message = "获取最新博客")
    @RequestMapping(value = "/getRecent", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getRecent() {
        return articleService.getRecentForBlog();
    }

    @Logger(message = "获取指定博客")
    @RequestMapping(value = "/getBlog", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getBlog(String id) {
        return articleService.getBlog(id);
    }

    @Logger(message = "获取博客总数量")
    @RequestMapping(value = "/getTotal", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getTotal() {
        return articleService.getTotal();
    }

    @Logger(message = "获取博客列表")
    @RequestMapping(value = "/getBlogList", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getBlogList(int page, int pageSize) {
        return articleService.getBlogList(page, pageSize);
    }

    @Logger(message = "获取导航栏")
    @RequestMapping(value = "/getNavigation", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getBlogList(HttpServletRequest request) {
        return blogService.getNavigation(request);
    }

    @Logger(message = "获取个人基本信息")
    @RequestMapping(value = "/getBaseInfo", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getBaseInfo(HttpServletRequest request) {
        return blogService.getBaseInfo(request);
    }

    @Logger(message = "获取友情链接")
    @RequestMapping(value = "/getLink", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getLink(HttpServletRequest request) {
        return blogService.getLink(request);
    }

    @Logger(message = "获取职业技能")
    @RequestMapping(value = "/getSkill", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getSkill(HttpServletRequest request) {
        return blogService.getSkill(request);
    }

    @Logger(message = "获取心路历程")
    @RequestMapping(value = "/getProcess", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getProcess(HttpServletRequest request) {
        return blogService.getProcess(request);
    }
}
