package com.sxkl.cloudnote.article.search.lucene;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;

/**
 * @author wangyao
 * @date 2018年1月10日 下午11:23:36
 * @description: 云笔记搜索索引管理类
 */
@Service
public class IndexManager {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private ScoreHandler scoreHandler;
	@Autowired
    private RedisTemplate<Object, Object> redisTemplate;
	
	@Logger(message="创建云笔记搜索索引")
	public void createIndex(String userId){
		List<Article> articles = articleService.getAllArticles(userId);
		Map<String,List<Article>> mappings = scoreHandler.createWordArticleMapping(articles);
		redisTemplate.delete(Constant.WORD_ARTICLE_MAPPING_IN_REDIS);
		redisTemplate.opsForHash().putAll(Constant.WORD_ARTICLE_MAPPING_IN_REDIS, mappings);
	}
}
