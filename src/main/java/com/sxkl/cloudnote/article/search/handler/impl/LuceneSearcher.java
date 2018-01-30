package com.sxkl.cloudnote.article.search.handler.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.handler.ArticleSeracher;
import com.sxkl.cloudnote.article.search.handler.RedisResultConver;
import com.sxkl.cloudnote.article.search.lucene.LuceneManager;
import com.sxkl.cloudnote.article.search.lucene.WordAnalyzer;
import com.sxkl.cloudnote.article.service.ArticleService;

/**
 * @author: wangyao
 * @date:2018年1月11日 上午9:32:41
 */
@Service("luceneSearcher")
public class LuceneSearcher implements ArticleSeracher{
	
	@Autowired
	private LuceneManager luceneManager;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private RedisResultConver redisResultConver;
	private static final int PAGE_SIZE = 20;

	@Override
	public List<Article> search(String searchKeys, String userId) {
		Map<String,Integer> keys = WordAnalyzer.analysis(searchKeys);
		Set<String> keySet = keys.keySet();
		List<Article> result = Lists.newArrayList();
		if(StringUtils.isEmpty(searchKeys)){
			return result;
		}
		result = luceneManager.search(searchKeys, userId,PAGE_SIZE);
		if(result.isEmpty()){
			return result;
		}
		List<String> ids = Lists.newArrayList();
		for(Article article : result){
			if(!Objects.isNull(article) && !StringUtils.isEmpty(article.getId())){
				ids.add(article.getId());
			}
		}
		List<Article> temps = articleService.getArticlesByIds(ids,userId);
		Iterator<Article> it = result.iterator();
		while(it.hasNext()){
			Article article = it.next();
			if(Objects.isNull(article)){
				it.remove();
				continue;
			}
			Article temp = null;
			Iterator<Article> tempsIt = temps.iterator();
			while(tempsIt.hasNext()){
				temp = tempsIt.next();
				if(temp.getId().equals(article.getId())){
					tempsIt.remove();
					break;
				}
			}
			if(!Objects.isNull(temp)){
				if(StringUtils.isEmpty(article.getTitle())){
					article.setTitle(temp.getTitle());
				}
				String content = temp.getContent();
				for(String key : keySet){
					content = content.replaceAll(key,Joiner.on("").join("<b><font color='red'>",key,"</font></b>"));
				}
				article.setContent(content);
			}
		}
		return result;
//		Set keysInRedis = WordAnalyzer.analysis(searchKeys).keySet();
//		result = redisResultConver.convertMulti(userId, keysInRedis);
//		return ArticleFilter.doFilte(result,PAGE_SIZE);
	}
}
