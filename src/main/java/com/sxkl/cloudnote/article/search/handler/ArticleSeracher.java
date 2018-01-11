package com.sxkl.cloudnote.article.search.handler;

import java.util.List;

import com.sxkl.cloudnote.article.entity.Article;

/**
 * @author: wangyao
 * @date:2018年1月11日 上午9:30:30
 */
public interface ArticleSeracher {

	List<Article> search(String searchKeys, String userId);
}
