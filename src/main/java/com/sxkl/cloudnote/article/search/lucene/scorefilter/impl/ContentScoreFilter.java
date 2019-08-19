package com.sxkl.cloudnote.article.search.lucene.scorefilter.impl;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.scorefilter.AbstractScoreFilter;
import com.sxkl.cloudnote.article.search.lucene.scorefilter.ScoreFilter;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午2:28:18
 */
public class ContentScoreFilter extends AbstractScoreFilter implements ScoreFilter {

    private static final int CONTENTS_WEIGHT = 10;

    @Override
    public void doFilte(Map<String, Article> result, Article article) {
        String content = article.getContent();
        Document document = Jsoup.parse(content);
        doTextFilte(result, document.text(), article);
    }

    @Override
    protected int getWeight() {
        return CONTENTS_WEIGHT;
    }

    @Override
    public int getPriority() {
        return 2;
    }

}
