package com.sxkl.cloudnote.article.search.lucene.scorefilter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.sxkl.cloudnote.article.entity.Article;

/**
 * @author: wangyao
 * @date:2018年1月11日 下午3:04:00
 */
public class ScoreFilterManager {

    private Set<ScoreFilter> filters;

    public ScoreFilterManager() {
        filters = Sets.newHashSet();
    }

    public ScoreFilterManager addFilter(ScoreFilter filter) {
        filters.add(filter);
        return this;
    }

    public void doFilter(Map<String, Article> result, Article article) {
        Ordering<ScoreFilter> scoreFilterOrdering = new Ordering<ScoreFilter>() {
            public int compare(ScoreFilter left, ScoreFilter right) {
                return left.getPriority() - right.getPriority();
            }
        };
        List<ScoreFilter> sortedScoreFilters = scoreFilterOrdering.greatestOf(filters, filters.size());
        for (ScoreFilter filter : sortedScoreFilters) {
            filter.doFilte(result, article);
        }
        filters.clear();
    }
}
