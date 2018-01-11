package com.sxkl.cloudnote.article.search.lucene.scorefilter;

import java.util.Map;

import com.sxkl.cloudnote.article.entity.Article;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午2:26:26
 */
public interface ScoreFilter {
	
	void doFilte(Map<String,Article> result, Article article);
	
	int getPriority();
}
