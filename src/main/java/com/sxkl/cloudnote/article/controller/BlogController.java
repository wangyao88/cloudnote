package com.sxkl.cloudnote.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.article.service.ArticleService;

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

    @RequestMapping(value = "/getRecommend", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getRecommend() {
        return articleService.getRecommend();
    }

    @RequestMapping(value = "/getRecent", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getRecent() {
        return articleService.getRecentForBlog();
    }

    @RequestMapping(value = "/getBlog", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getBlog(String id) {
        return articleService.getBlog(id);
    }

    @RequestMapping(value = "/getTotal", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getTotal() {
        return articleService.getTotal();
    }

    @RequestMapping(value = "/getBlogList", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getBlogList(int page, int pageSize) {
        return articleService.getBlogList(page, pageSize);
    }
}
