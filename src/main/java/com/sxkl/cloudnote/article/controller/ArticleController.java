package com.sxkl.cloudnote.article.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.LuceneManager;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.CloudnoteServiceUrlConstant;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;
import com.sxkl.cloudnote.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
//			User user = UserUtil.getSessionUser(request);
//			indexManager.createIndex(user.getId());
//			luceneManager.creatIndexOnDisk(user.getId());

			String url = CloudnoteServiceUrlConstant.LOAD_DATA_URL;
			HttpResponse<String> response = Unirest.get(url).asString();
			String result = response.getBody();
			if(Boolean.valueOf(result)) {
				return OperateResultService.configurateSuccessResult();
			}else {
				return OperateResultService.configurateFailureResult("创建索引失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	@RequestMapping("/detail")
	public ModelAndView result(@RequestParam("id") String id){
		ModelAndView modelAndView = new ModelAndView(StringUtils.appendJoinEmpty("article/detail","_",PropertyUtil.getMode()));
		Article article = articleService.getArticle(id);
		modelAndView.addObject("article", article);
		return modelAndView;
	}

	@RequestMapping(value = "/sameArticles", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public List<Article> sameArticles(@RequestParam("id") String id) {
		return articleService.getSameArticles(id);
	}
}
