package com.sxkl.cloudnote.article.search.handler.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.handler.ArticleSeracher;

/**
 * @author: wangyao
 * @date:2018年1月11日 上午9:33:00
 */
@Service("customSearcher")
public class CustomSearcher implements ArticleSeracher{

	@Autowired
	private ArticleDao articleDao;
	private static final int PAGE_INDEX = 0;
	private static final int PAGE_SIZE = 20;
	private static final int TITLE_WEIGHT = 70;
	private static final int CONTENTS_WEIGHT = 10;
	private static final int HITNUM_WEIGHT = 2;
	
	@Override
	public List<Article> search(String searchKeys, String userId) {
		Set<Article> temps = new HashSet<Article>();
		String[] keys = searchKeys.split(",");
		for(String key : keys){
			temps.addAll(articleDao.selectAllArticlesByTitle(key,PAGE_INDEX,PAGE_SIZE,userId));
			temps.addAll(articleDao.selectAllArticlesByContent(key,PAGE_INDEX,PAGE_SIZE,userId));
		}
		List<Article> articles = Lists.newArrayList(temps);
		for(String key : keys){
			for(Article article : articles){
				int weight = article.getWeight();
				int titleCount = wordCount(article.getTitle(),key);
				article.setWeight(TITLE_WEIGHT*titleCount+weight);
				weight = article.getWeight();
				int contentCount = wordCount(article.getContent(),key);
				article.setWeight(CONTENTS_WEIGHT*contentCount+weight);
				weight = article.getWeight();
				article.setWeight(HITNUM_WEIGHT*article.getHitNum()+weight);
			}
		}
		Ordering<Article> articleOrdering = new Ordering<Article>() {
			public int compare(Article left, Article right) {
				return left.getWeight() - right.getWeight();
			}
		};
		return articleOrdering.greatestOf(articles, PAGE_SIZE);
	}
	
    private static int wordCount(String content, String word){
    	if(StringUtils.isEmpty(content) || StringUtils.isEmpty(word)){
    		return 0;
    	}
    	int count = 0;
		int start = 0;
		while (content.indexOf(word, start) >= 0 && start < content.length()) {
			count++;
			start = content.indexOf(word, start) + word.length();
		}
		return count;
    }
}
