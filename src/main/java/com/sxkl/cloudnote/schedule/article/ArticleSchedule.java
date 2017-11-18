package com.sxkl.cloudnote.schedule.article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.sxkl.cloudnote.user.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleSchedule {
	
	private static final int HOT_ARTICLE_RANGE = 10;
	
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private ArticleService articleService;
	
	@Scheduled(fixedRate=Constant.HOT_ARTICLE_EXPIRE_IN_REDIS)
    public void cacheHotArticle(){
		User user = Constant.SESSION_USER;
		if(user == null){
			return;
		}
		List<ArticleForCache> articles = articleService.getHotArticles(HOT_ARTICLE_RANGE);
		Map<String,String> result = new HashMap<String,String>();
		for(ArticleForCache articleForCache : articles){
			result.put(articleForCache.getId(), articleForCache.getContent());
		}
		redisCacheService.cacheMap(Constant.HOT_ARTICLE_KEY_IN_REDIS,result);
        log.info("缓存热门笔记");
    }


}
