package com.sxkl.cloudnote.article.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.article.search.lucene.LuceneManager;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;

@RestController
@RequestMapping("/article")
public class ArticleController {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private LuceneManager luceneManager;
	
	@RequestMapping(value = "/addArticle", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String addArticle(HttpServletRequest request){
		try {
		    articleService.addArticle(request);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getAllArticles", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String getAllArticles(HttpServletRequest request){
		try {
		    return articleService.getAllArticles(request);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getArticle", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String getArticle(HttpServletRequest request){
		try {
		    String content = articleService.getArticle(request);
		    return OperateResultService.configurateSuccessResult(content);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/deleteArticle", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String deleteArticle(HttpServletRequest request){
		try {
		    articleService.deleteArticle(request);
		    return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getArticleForEdit", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String getArticleForEdit(HttpServletRequest request){
		try {
		    return articleService.getArticleForEdit(request);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/checkTitle", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String checkTitle(String title, HttpServletRequest request){
		try {
			User user = UserUtil.getSessionUser(request);
		    return articleService.checkTitle(title,user.getId());
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/createIndex", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String createIndex(HttpServletRequest request){
		try {
			User user = UserUtil.getSessionUser(request);
//			indexManager.createIndex(user.getId());
			luceneManager.creatIndexOnDisk(user.getId());
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/quickupdate", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String quickupdate(HttpServletRequest request){
		try {
		    articleService.quickupdate(request);
			return OperateResultService.configurateSuccessResult();
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult(e.getMessage());
		}
	}
}
