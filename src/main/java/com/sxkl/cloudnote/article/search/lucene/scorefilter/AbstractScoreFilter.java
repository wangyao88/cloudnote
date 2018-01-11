package com.sxkl.cloudnote.article.search.lucene.scorefilter;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.WordAnalyzer;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午2:26:26
 */
public abstract class AbstractScoreFilter implements ScoreFilter{
	
	protected int priority;
	
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getPriority();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractScoreFilter other = (AbstractScoreFilter) obj;
		if (getPriority() != other.getPriority())
			return false;
		return true;
	}
}
