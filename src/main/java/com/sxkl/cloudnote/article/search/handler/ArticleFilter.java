package com.sxkl.cloudnote.article.search.handler;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.sxkl.cloudnote.article.entity.Article;

/**
 * @author: wangyao
 * @date:2018年1月12日 上午9:26:51
 */
public class ArticleFilter {

    public static List<Article> unique(List<Article> source) {
        Set<Article> temp = Sets.newHashSet();
        for (Article article : source) {
            temp.add(article);
        }
        return Lists.newArrayList(temp);
    }

    public static List<Article> sortAsc(List<Article> unSort, int size) {
        Ordering<Article> articleAscOrdering = new Ordering<Article>() {
            public int compare(Article left, Article right) {
                return right.getWeight() - left.getWeight();
            }
        };
        List<Article> sortedArticles = articleAscOrdering.greatestOf(unSort, size);
        return copyResult(sortedArticles);
    }

    public static List<Article> sortDesc(List<Article> unSort, int size) {
        Ordering<Article> articleDescOrdering = new Ordering<Article>() {
            public int compare(Article left, Article right) {
                return left.getWeight() - right.getWeight();
            }
        };
        List<Article> sortedArticles = articleDescOrdering.greatestOf(unSort, size);
        return copyResult(sortedArticles);
    }

    private static List<Article> copyResult(List<Article> sortedArticles) {
        List<Article> result = Lists.newArrayList();
        for (Article article : sortedArticles) {
            result.add(article);
        }
        return result;
    }

    public static List<Article> doFilte(List<Article> result, int size) {
        List<Article> sortAscArticles = sortAsc(result, result.size());
        List<Article> filtedResult = unique(sortAscArticles);
        int realSize = Math.min(size, filtedResult.size());
        List<Article> sortDescArticles = sortDesc(filtedResult, realSize);
        return sortDescArticles;
    }
}
