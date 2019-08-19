package com.sxkl.cloudnote.article.service;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.article.entity.Article;

/**
 * @author: wangyao
 * @date: 2018年3月15日 上午10:30:43
 * @description:
 */
public class ArticleRecursiveTask extends RecursiveTask<List<Article>> {

    private static final long serialVersionUID = -6532228097607355926L;
    private static final int THRESHOLD = 2;
    private int start;
    private int end;
    private String userId;
    private int pageSize;
    private ArticleService articleService;

    public ArticleRecursiveTask(int start, int end, String userId, int pageSize, ArticleService articleService) {
        super();
        this.start = start;
        this.end = end;
        this.userId = userId;
        this.pageSize = pageSize;
        this.articleService = articleService;
    }


    @Override
    protected List<Article> compute() {
        List<Article> articles = Lists.newArrayList();
        boolean canCompute = (end - start) <= THRESHOLD;
        if (canCompute) {
            for (int i = start; i <= end; i++) {
                articles.addAll(articleService.findPage(i, pageSize, userId));
            }
        } else {
            int mid = (start + end) / 2;
            ArticleRecursiveTask leftTask = new ArticleRecursiveTask(start, mid, userId, pageSize, articleService);
            ArticleRecursiveTask rightTask = new ArticleRecursiveTask(mid + 1, end, userId, pageSize, articleService);
            leftTask.fork();
            rightTask.fork();

            List<Article> leftResult = leftTask.join();
            List<Article> rightResult = leftTask.join();
            articles.addAll(leftResult);
            articles.addAll(rightResult);
        }
        return articles;
    }
}
