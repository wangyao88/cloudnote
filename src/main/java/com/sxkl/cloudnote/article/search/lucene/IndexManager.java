package com.sxkl.cloudnote.article.search.lucene;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.handler.ArticleFilter;
import com.sxkl.cloudnote.article.search.handler.RedisResultConver;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.StringAppendUtils;

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
	@Autowired
	private RedisResultConver redisResultConver;
	
	@Logger(message="创建云笔记搜索索引")
	public void createIndex(String userId){
		List<Article> articles = articleService.getAllArticles(userId);
		Map<String,List<Article>> mappings = scoreHandler.createWordArticleMapping(articles);
		redisTemplate.delete(Constant.WORD_ARTICLE_MAPPING_IN_REDIS);
		redisTemplate.delete(getWordArticleMappingKey(userId));
		redisTemplate.opsForHash().putAll(getWordArticleMappingKey(userId), mappings);
	}
	
	@Logger(message="创建-新增笔记的搜索索引")
	public void updateIndexByAdd(Article article, String userId) {
		Map<String,Article> scores = scoreHandler.calculateArticleScore(article);
		for(Map.Entry<String, Article> entry : scores.entrySet()){
			String key = entry.getKey();
			Article temp = entry.getValue();
			List<Article> articles = redisResultConver.convertSingle(userId, key);
			if(articles.contains(article)){
				Article elem = articles.get(articles.indexOf(article));
				temp.setWeight(elem.getWeight()+temp.getWeight());
				handleResult(userId, key, articles);
				continue;
			}
			Article result = new Article();
			result.setId(temp.getId());
			result.setTitle(temp.getTitle());
			result.setHitNum(temp.getHitNum());
			result.setWeight(temp.getWeight());
			articles.add(result);
			handleResult(userId, key, articles);
		}
	}

	@Logger(message="更新-编辑笔记的搜索索引-带建设")
	public void updateIndexByUpdate(Article article, String userId) {
		
	}

	@Logger(message="删除-删除笔记的搜索索引-带建设")
	public void updateIndexByDelete(Article article, String userId) {
		
	}
	
	public String getWordArticleMappingKey(String userId){
		return StringAppendUtils.append(Constant.WORD_ARTICLE_MAPPING_IN_REDIS,"-",userId);
	}
	
	private void handleResult(String userId, String key, List<Article> articles) {
		List<Article> reuslts = ArticleFilter.sortDesc(articles, articles.size());
		redisTemplate.opsForHash().delete(getWordArticleMappingKey(userId), key);
		redisTemplate.opsForHash().put(getWordArticleMappingKey(userId),key,reuslts);
	}
}
