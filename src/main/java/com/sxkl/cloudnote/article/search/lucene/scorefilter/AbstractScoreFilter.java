package com.sxkl.cloudnote.article.search.lucene.scorefilter;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.WordAnalyzer;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午2:26:26
 */
public abstract class AbstractScoreFilter {
	
	protected void doTextFilte(Map<String, Article> result, String text, Article article) {
		Map<String,Integer> words = WordAnalyzer.analysis(text);
		Map<String,Integer> score = calculateScore(words);
		initScore(result, article, score);
	}

	private void initScore(Map<String, Article> result, Article article, Map<String, Integer> score) {
		for(Map.Entry<String, Integer> entry : score.entrySet()){
			String word = entry.getKey();
			if(!result.containsKey(word)){
				Article simpleArticle = new Article();
				simpleArticle.setId(article.getId());
				simpleArticle.setTitle(article.getTitle());
				simpleArticle.setHitNum(article.getHitNum());
				simpleArticle.setWeight(entry.getValue());
				result.put(word, simpleArticle);
			}else{
				Article simpleArticle = result.get(word);
				simpleArticle.setWeight(simpleArticle.getWeight()+entry.getValue());
			}
		}
	}
	
	private Map<String,Integer> calculateScore(Map<String,Integer> words) {
		Map<String,Integer> scores = Maps.newHashMap();
		for(Map.Entry<String, Integer> entry : words.entrySet()){
			String word = entry.getKey();
			int score = entry.getValue() * getWeight();
			scores.put(word, score);
		}
		return scores;
	}
	
	abstract protected int getWeight();

}
