package com.sxkl.cloudnote.article.search.lucene.scorefilter.impl;

import java.util.Map;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.scorefilter.AbstractScoreFilter;
import com.sxkl.cloudnote.article.search.lucene.scorefilter.ScoreFilter;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午2:28:18
 */
public class TitleScoreFilter extends AbstractScoreFilter implements ScoreFilter {

	private static final int TITLE_WEIGHT = 200;
	
	@Override
	public void doFilte(Map<String, Article> result, Article article) {
		String title = article.getTitle();
		title = title.replaceAll("-", ",");
		doTextFilte(result,title,article);
	}

	@Override
	protected int getWeight() {
		return TITLE_WEIGHT;
	}

	@Override
	public int getPriority() {
		return 1;
	}

}
