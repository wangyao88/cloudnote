package com.sxkl.cloudnote.article.search.lucene.scorefilter;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.article.entity.Article;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午3:04:00
 */
public class ScoreFilterManager {
	
	private List<ScoreFilter> filters;
	
	public ScoreFilterManager(){
		filters = Lists.newArrayList();
	}
	
	public ScoreFilterManager addFilter(ScoreFilter filter){
		filters.add(filter);
		return this;
	}
	
	public void doFilter(Map<String,Article> result, Article article){
		for(ScoreFilter filter : filters){
			filter.doFilte(result, article);
		}
		filters.clear();
	}
}
