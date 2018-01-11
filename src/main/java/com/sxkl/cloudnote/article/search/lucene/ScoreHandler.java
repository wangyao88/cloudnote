package com.sxkl.cloudnote.article.search.lucene;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.log.annotation.Logger;

/**
 * @author wangyao
 * @date 2018年1月10日 下午10:18:25
 * @description: 搜索关键词对应的文章得分
 */
@Service
public class ScoreHandler {
	
	private static final int TITLE_WEIGHT = 70;
	private static final int CONTENTS_WEIGHT = 10;
	private static final int HITNUM_WEIGHT = 2;
	private static final int PAGE_SIZE = 20;
	
	@Autowired
	private WordAnalyzer analyzer;
	
	@Logger(message="创建云笔记搜索关键词与文章映射关系")
	public Map<String,List<Article>> createWordArticleMapping(List<Article> articles){
		Map<String,List<Article>> indexs = Maps.newHashMap();
		Map<String,List<Article>> scores = batchCalculateArticleScore(articles);
		Ordering<Article> articleOrdering = new Ordering<Article>() {
			public int compare(Article left, Article right) {
				return left.getWeight() - right.getWeight();
			}
		};
		for(Map.Entry<String,List<Article>> entry : scores.entrySet()){
			String word = entry.getKey();
			List<Article> unSortedArticles = entry.getValue();
			List<Article> sortedArticles = articleOrdering.greatestOf(unSortedArticles, PAGE_SIZE);
			indexs.put(word, sortedArticles);
		}
		return indexs;
	}
	
	private Map<String,Article> calculateArticleScore(Article article){
		Map<String,Article> result = Maps.newHashMap();
		Map<String,Integer> titleScore = calculateScore(article.getTitle(),TITLE_WEIGHT);
		Map<String,Integer> contentScore = calculateScore(article.getContent(),CONTENTS_WEIGHT);
		for(Map.Entry<String, Integer> entry : contentScore.entrySet()){
			String word = entry.getKey();
			if(titleScore.containsKey(word)){
				entry.setValue(entry.getValue() + titleScore.get(word));
			}
			entry.setValue(entry.getValue() + article.getHitNum()*HITNUM_WEIGHT);
			Article simpleArticle = new Article();
			simpleArticle.setId(article.getId());
			simpleArticle.setTitle(article.getTitle());
			simpleArticle.setHitNum(article.getHitNum());
			simpleArticle.setWeight(entry.getValue());
			result.put(word, simpleArticle);
		}
		for(Map.Entry<String, Integer> entry : titleScore.entrySet()){
			String word = entry.getKey();
			if(!result.containsKey(word)){
				entry.setValue(entry.getValue() + article.getHitNum()*HITNUM_WEIGHT);
				Article simpleArticle = new Article();
				simpleArticle.setId(article.getId());
				simpleArticle.setTitle(article.getTitle());
				simpleArticle.setHitNum(article.getHitNum());
				simpleArticle.setWeight(entry.getValue());
				result.put(word, simpleArticle);
			}
		}
		return result;
	}
	
	private Map<String,List<Article>> batchCalculateArticleScore(List<Article> articles){
		Map<String,List<Article>> result = Maps.newHashMap();
		for(Article artice : articles){
			Map<String,Article> temp = calculateArticleScore(artice);
			for(Map.Entry<String, Article> entry : temp.entrySet()){
				String word = entry.getKey();
				List<Article> values = Lists.newArrayList();
				if(result.containsKey(word)){
					values = result.get(word);
					values.add(entry.getValue());
				}else{
					values.add(entry.getValue());
					result.put(word, values);
				}
			}
		}
		return result;
	}
	
	private Map<String,Integer> calculateScore(String content, int weight) {
		Map<String,Integer> words = analyzer.analysis(content);
		Map<String,Integer> scores = Maps.newHashMap();
		for(Map.Entry<String, Integer> entry : words.entrySet()){
			String word = entry.getKey();
			int score = entry.getValue() * weight;
			scores.put(word, score);
		}
		return scores;
	}
}
