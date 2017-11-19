package com.sxkl.cloudnote.article.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.entity.ArticleForCache;
import com.sxkl.cloudnote.article.entity.ArticleForEdit;
import com.sxkl.cloudnote.article.entity.ArticleForHtml;
import com.sxkl.cloudnote.cache.annotation.RedisDisCachable;
import com.sxkl.cloudnote.cache.service.RedisCacheService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.eventdriven.manager.PublishManager;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.image.service.ImageService;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.note.service.NoteService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
import com.sxkl.cloudnote.utils.FileUtils;
import com.sxkl.cloudnote.utils.UserUtil;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private NoteService noteService;
	@Autowired
	private FlagService flagService;
	@Autowired
	private UserService userService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private RedisCacheService redisCacheService;

	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void addArticle(HttpServletRequest request) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		content = content.replaceAll(Constant.DOMAIN, Constant.ARTICLE_CONTENT_DOMAIN);
		String noteId = request.getParameter("note");
		String flagsStr = request.getParameter("flags");
		String articleId = request.getParameter("articleId");
		String isEdit = request.getParameter("isEdit");
		String[] flagIdTemps = flagsStr.split(",");
		String[] flags = new String[flagIdTemps.length];
		for(int  i = 0; i < flags.length; i++){
			flags[i] = flagIdTemps[i].substring(Constant.TREE_MENU_FLAG_ID_PREFIX.length());
		}
		List<Flag> flagBeans = flagService.selectFlagsByIds(flags);
		User sessionUser = UserUtil.getSessionUser(request);
		User user = userService.selectUser(sessionUser);
		Note note = noteService.selectNoteById(noteId);
		Article article = null;
		if(Boolean.valueOf(isEdit)){
			article = articleDao.selectArticleById(articleId);
		}else{
			article = new Article();
			article.setHitNum(0);
			article.setCreateTime(new Date());
			article.setUser(user);
		}
		article.setTitle(title);
		article.setNote(note);
		article.setFlags(new HashSet<Flag>(flagBeans));
		String contentFilted = FileUtils.saveHtmlImgToDB(content,imageService);
		article.setContent(contentFilted);
		articleDao.saveOrUpdateArticle(article);
		PublishManager.getPublishManager().getArticlePublisher().establishLinkagesBetweenArticleAndImage(article);
	}
	
	public String getAllArticles(HttpServletRequest request) {
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		String first = request.getParameter("first");
		String articleTitle = request.getParameter("articleTitle");
		User sessionUser = UserUtil.getSessionUser(request);
		String userId = sessionUser.getId();
		
		List<Article> articles = new ArrayList<Article>();
		List<ArticleForHtml> articleForHtmls = new ArrayList<ArticleForHtml>();
		int total = 0;
		if(Boolean.valueOf(first)){
			articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex),Integer.parseInt(pageSize),userId);
			total = articleDao.selectAllArticlesOrderByCreateTimeAndHitNumCount(userId);
		}else if(!StringUtils.isEmpty(articleTitle)){
			articles = articleDao.selectAllArticlesByNameOrderByHitNum(articleTitle,Integer.parseInt(pageIndex),Integer.parseInt(pageSize),userId);
			total = articleDao.selectAllArticlesByNameOrderByCreateTimeAndHitNumCount(articleTitle,userId);
		}else{
			String flagId = request.getParameter("flagId");
			if(!StringUtils.isEmpty(flagId)){
				flagId = flagId.substring(Constant.TREE_MENU_FLAG_ID_PREFIX.length());
				List result = articleDao.selectAllFlagArticlesOrderByCreateTimeAndHitNum(flagId,Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
				for(Iterator iterator = result.iterator();iterator.hasNext();){  
		            //每个集合元素都是一个数组，数组元素师person_id,person_name,person_age三列值 
		            Object[] objects = (Object[]) iterator.next();
		            ArticleForHtml articleForHtml = new ArticleForHtml();
					articleForHtml.setId(String.valueOf(objects[0]));
					articleForHtml.setTitle(String.valueOf(objects[1]));
					articleForHtml.setHitNum(Integer.parseInt(String.valueOf(objects[2])));
					articleForHtmls.add(articleForHtml);
		        }  
				total = articleDao.selectAllFlagArticlesOrderByCreateTimeAndHitNumCount(flagId);
				return OperateResultService.configurateSuccessDataGridResult(articleForHtmls,total);
			}
			String noteId = request.getParameter("noteId");
			if(!StringUtils.isEmpty(noteId)){
				noteId = noteId.substring(Constant.TREE_MENU_NOTE_ID_PREFIX.length());
				articles = articleDao.selectAllNoteArticlesOrderByCreateTimeAndHitNum(noteId,Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
				total = articleDao.selectAllNoteArticlesOrderByCreateTimeAndHitNumCount(noteId);
			}
			if(StringUtils.isEmpty(flagId) && StringUtils.isEmpty(noteId)){
				articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex),Integer.parseInt(pageSize),userId);
				total = articleDao.selectAllArticlesOrderByCreateTimeAndHitNumCount(userId);
			}
		}
		return OperateResultService.configurateSuccessDataGridResult(articles,total);
	}

	public String getArticle(HttpServletRequest request) {
		String id = request.getParameter("id");
		String content = redisCacheService.getValueFromHash(Constant.HOT_ARTICLE_KEY_IN_REDIS,id,request);
		if(StringUtils.isEmpty(content)){
			Article article = articleDao.selectArticleById(id);
			content = article.getContent();
		}
		content = content.replaceAll(Constant.ARTICLE_CONTENT_DOMAIN, Constant.DOMAIN);
		PublishManager.getPublishManager().getArticlePublisher().increaseArticleHitNum(id);
		return content;
	}
	
	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void deleteArticle(HttpServletRequest request) {
		String id = request.getParameter("id");
		Article article = articleDao.selectArticleById(id);
		article.setNote(null);
		article.setUser(null);
		article.setFlags(null);
		articleDao.deleteArticle(article);
		imageService.deleteImageByArticleId(id);
	}

	public String getArticleForEdit(HttpServletRequest request) {
		String articleId = request.getParameter("articleId");
		Article article = articleDao.selectArticleById(articleId);
		
		Note note = article.getNote();
		Note noteResult = new Note();
		if(note != null){
			noteResult.setId(note.getId());
			noteResult.setName(note.getName());
		}
		
		Flag flagResult = new Flag();
		Set<Flag> flags = article.getFlags();
		if(flags != null){
			String flagIds = "";
			String flagNames = "";
			for(Flag flag : flags){
				flagIds += Constant.TREE_MENU_FLAG_ID_PREFIX + flag.getId() + ",";
				flagNames += flag.getName() + ",";
			}
			if(flagIds.endsWith(Constant.COMMA)){
				flagIds = flagIds.substring(0, flagIds.length()-1);
			}
			if(flagNames.endsWith(Constant.COMMA)){
				flagNames = flagNames.substring(0, flagNames.length()-1);
			}
			
			flagResult.setId(flagIds);
			flagResult.setName(flagNames);
		}
		
		article.setHitNum(article.getHitNum()+1);
		articleDao.updateArticle(article);
		String content = article.getContent();
		content = content.replaceAll(Constant.ARTICLE_CONTENT_DOMAIN, Constant.DOMAIN);
		
		ArticleForEdit articleForEdit = new ArticleForEdit(noteResult,flagResult,content);
		
		return new Gson().toJson(articleForEdit);
	}

	public List<ArticleForCache> getHotArticles(String userId, int hotArticleRange) {
		List<Article> articles = articleDao.selectAllArticlesOrderByHitNum(0, hotArticleRange,userId);
		List<ArticleForCache> results = new ArrayList<ArticleForCache>();
		for(Article article : articles){
			ArticleForCache articleForCache = new ArticleForCache();
			articleForCache.setId(article.getId());
			articleForCache.setContent(article.getContent());
			results.add(articleForCache);
		}
		return results;
	}
}
