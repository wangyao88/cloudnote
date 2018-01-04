package com.sxkl.cloudnote.schedule.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.entity.ArticleForCache;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.cache.service.RedisCacheService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;

@Service
public class ArticleSchedule {
	
	private static final int HOT_ARTICLE_RANGE = 10;
	
	@Autowired
	private RedisCacheService redisCacheService;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	
	@Logger(message="定时缓存用户热门笔记")
//	@Scheduled(fixedRate=Constant.HOT_ARTICLE_EXPIRE_IN_REDIS)
    public void cacheHotArticle(){
		List<User> users = userService.getAllUsers();
		for(User user : users){
			List<ArticleForCache> articles = articleService.getHotArticles(user.getId(),HOT_ARTICLE_RANGE);
			Map<String,String> result = new HashMap<String,String>();
			for(ArticleForCache articleForCache : articles){
				result.put(articleForCache.getId(), articleForCache.getContent());
			}
			redisCacheService.cacheMap(Constant.HOT_ARTICLE_KEY_IN_REDIS+user.getId(),result);
		}
    }
}
