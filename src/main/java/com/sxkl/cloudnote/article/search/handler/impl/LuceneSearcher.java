package com.sxkl.cloudnote.article.search.handler.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.handler.ArticleSeracher;
import com.sxkl.cloudnote.common.entity.Constant;

/**
 * @author: wangyao
 * @date:2018年1月11日 上午9:32:41
 */
@Service("luceneSearcher")
public class LuceneSearcher implements ArticleSeracher{
	
	@Autowired
    private RedisTemplate<String, List<Article>> redisTemplate;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Article> search(String searchKeys, String userId) {
		List<Article> result = Lists.newArrayList();
		String[] keys = searchKeys.split(",");
		Set<Object> keysInRedis = Sets.newHashSet(Arrays.asList(keys));
		List articles = redisTemplate.opsForHash().multiGet(Constant.WORD_ARTICLE_MAPPING_IN_REDIS, keysInRedis);
		if(articles.isEmpty()){
			return result;
		}
		Object objs = articles.get(0);
		for(Object obj : (List)objs){
			result.add((Article)obj);
		}
		Ordering<Article> articleAscOrdering = new Ordering<Article>() {
			public int compare(Article left, Article right) {
				return right.getWeight() - left.getWeight();
			}
		};
		List<Article> sortedArticles = articleAscOrdering.greatestOf(result, result.size());
		Set temp = new HashSet();
		for(Article article : sortedArticles){
			temp.add(article);
		}
		List filtedResult = Lists.newArrayList(temp);
		Ordering<Article> articleDescOrdering = new Ordering<Article>() {
			public int compare(Article left, Article right) {
				return left.getWeight() - right.getWeight();
			}
		};
		int size = Math.min(filtedResult.size(), 20);
		List<Article> sortedResults = articleDescOrdering.greatestOf(filtedResult, size);
		return sortedResults;
	}

}
