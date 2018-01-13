package com.sxkl.cloudnote.article.search.handler.impl;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.handler.ArticleFilter;
import com.sxkl.cloudnote.article.search.handler.ArticleSeracher;
import com.sxkl.cloudnote.article.search.handler.RedisResultConver;
import com.sxkl.cloudnote.article.search.lucene.WordAnalyzer;
import com.sxkl.cloudnote.utils.PropertyUtil;

/**
 * @author: wangyao
 * @date:2018年1月11日 上午9:32:41
 */
@Service("luceneSearcher")
public class LuceneSearcher implements ArticleSeracher{
	
	@Autowired
	private RedisResultConver redisResultConver;
	private static final int PAGE_SIZE = 20;

	@SuppressWarnings("rawtypes")
	@Override
	public List<Article> search(String searchKeys, String userId) {
		List<Article> result = Lists.newArrayList();
		if(StringUtils.isEmpty(searchKeys)){
			return result;
		}
		Set keysInRedis = WordAnalyzer.analysis(searchKeys).keySet();
		result = redisResultConver.convertMulti(userId, keysInRedis);
		return ArticleFilter.doFilte(result,PAGE_SIZE);
	}
	
	private void updateExt(){
		try {
			String path = PropertyUtil.class.getClassLoader().getResource("lucene/ext.dic").getPath();
			List<String> lines = Files.readLines(new File(path), Charsets.UTF_8);
			StringBuilder content = new StringBuilder();
			for(String line : lines){
				System.out.println(line);
				content.append(line).append(System.getProperty("line.separator"));
			}
			content.append("电脑").append(System.getProperty("line.separator"));
			Files.write(content.toString(), new File(path), Charsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
