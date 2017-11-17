package com.sxkl.cloudnote.article.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.entity.ArticleForHtml;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.eventdriven.manager.PublishManager;
import com.sxkl.cloudnote.eventdriven.publisher.ArticlePublisher;
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

	public void addArticle(HttpServletRequest request) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
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
		String domain = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		String contentFilted = FileUtils.saveHtmlImgToDB(domain,content,imageService);
		article.setContent(contentFilted);
		articleDao.saveOrUpdateArticle(article);
		PublishManager.getPublishManager().getArticlePublisher().establishLinkagesBetweenArticleAndImage(article,domain);
	}

	public String getAllArticles(HttpServletRequest request) {
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		String first = request.getParameter("first");
		String articleTitle = request.getParameter("articleTitle");
		
		
		List<Article> articles = new ArrayList<Article>();
		List<ArticleForHtml> articleForHtmls = new ArrayList<ArticleForHtml>();
		int total = 0;
		if(Boolean.valueOf(first)){
			articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
			total = articleDao.selectAllArticlesOrderByCreateTimeAndHitNumCount();
		}else if(!StringUtils.isEmpty(articleTitle)){
			articles = articleDao.selectAllArticlesByNameOrderByHitNum(articleTitle,Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
			total = articleDao.selectAllArticlesByNameOrderByCreateTimeAndHitNumCount(articleTitle);
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
				articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
				total = articleDao.selectAllArticlesOrderByCreateTimeAndHitNumCount();
			}
		}
		return OperateResultService.configurateSuccessDataGridResult(articles,total);
	}

	public String getArticle(HttpServletRequest request) {
		String id = request.getParameter("id");
		Article article = articleDao.selectArticleById(id);//加入二级缓存
		article.setHitNum(article.getHitNum()+1);
		articleDao.updateArticle(article);
		return article.getContent();
	}

	public void deleteArticle(HttpServletRequest request) {
		String id = request.getParameter("id");
		Article article = articleDao.selectArticleById(id);
		article.setNote(null);
		article.setUser(null);
		article.setFlags(null);
		articleDao.deleteArticle(article);
	}

}
