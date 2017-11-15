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

import com.google.gson.Gson;
import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.note.service.NoteService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
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

	public void addArticle(HttpServletRequest request) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String noteId = request.getParameter("note");
		String flagsStr = request.getParameter("flags");
		String[] flagIdTemps = flagsStr.split(",");
		String[] flags = new String[flagIdTemps.length];
		for(int  i = 0; i < flags.length; i++){
			flags[i] = flagIdTemps[i].substring(Constant.TREE_MENU_FLAG_ID_PREFIX.length());
		}
		
		List<Flag> flagBeans = flagService.selectFlagsByIds(flags);
		
		User sessionUser = UserUtil.getSessionUser(request);
		User user = userService.selectUser(sessionUser);
		
		Note note = noteService.selectNoteById(noteId);
		
		Article article = new Article();
		article.setTitle(title);
		article.setContent(content);
		article.setHitNum(0);
		article.setCreateTime(new Date());
		article.setUser(user);
		article.setNote(note);
		article.setFlags(new HashSet<Flag>(flagBeans));
		
		articleDao.insertArticle(article);
	}

	public String getAllArticles(HttpServletRequest request) {
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		String first = request.getParameter("first");
		List<Article> articles = new ArrayList<Article>();
		if(Boolean.valueOf(first)){
			articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
		}else{
			String flagId = request.getParameter("flagId");
			if(!StringUtils.isEmpty(flagId)){
				flagId = flagId.substring(Constant.TREE_MENU_FLAG_ID_PREFIX.length());
				List result = articleDao.selectAllFlagArticlesOrderByCreateTimeAndHitNum(flagId,Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
				for(Iterator iterator = result.iterator();iterator.hasNext();){  
		            //每个集合元素都是一个数组，数组元素师person_id,person_name,person_age三列值 
		            Object[] objects = (Object[]) iterator.next();
		            Article article = new Article();
					article.setId(String.valueOf(objects[0]));
					article.setTitle(String.valueOf(objects[1]));
					article.setHitNum(Integer.parseInt(String.valueOf(objects[2])));
					articles.add(article);
		        }  

			}
			String noteId = request.getParameter("noteId");
			if(!StringUtils.isEmpty(noteId)){
				noteId = noteId.substring(Constant.TREE_MENU_NOTE_ID_PREFIX.length());
				articles = articleDao.selectAllNoteArticlesOrderByCreateTimeAndHitNum(noteId,Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
			}
			if(StringUtils.isEmpty(flagId) && StringUtils.isEmpty(noteId)){
				articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex),Integer.parseInt(pageSize));
			}
		}
		return new Gson().toJson(articles);
	}

	public String getArticle(HttpServletRequest request) {
		String id = request.getParameter("id");
		Article article = articleDao.selectArticleById(id);
		article.setHitNum(article.getHitNum()+1);
		articleDao.updateArticle(article);
		return article.getContent();
	}

	public void deleteArticle(HttpServletRequest request) {
		String id = request.getParameter("id");
		System.out.println(id);
	}

}
