package com.sxkl.cloudnote.article.search.lucene.scorefilter.impl;

import java.util.Map;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.scorefilter.AbstractScoreFilter;
import com.sxkl.cloudnote.article.search.lucene.scorefilter.ScoreFilter;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午2:28:18
 */
public class HitnumScoreFilter extends AbstractScoreFilter implements ScoreFilter {

	private static final int HITNUM_WEIGHT = 2;
	
	@Override
	public void doFilte(Map<String, Article> result, Article article) {
		for(Map.Entry<String, Article> entry : result.entrySet()){
			Article simpleArticle = entry.getValue();
			simpleArticle.setWeight(simpleArticle.getWeight()+article.getHitNum()*HITNUM_WEIGHT);
		}
	}

	@Override
	protected int getWeight() {
		return HITNUM_WEIGHT;
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
