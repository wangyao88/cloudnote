package com.sxkl.cloudnote.statistic.model;

import com.sxkl.cloudnote.article.entity.Article;
import lombok.Data;

/**
 * 浏览量最多的文章
 * @author wangyao
 */
@Data
public class HitData {

    private Integer index;
    private String title;
    private String url;
    private Integer hitNum;

    public static HitData configureHitData(int index, Article article) {
        HitData hitData = new HitData();
        hitData.setIndex(index);
        hitData.setTitle(article.getTitle());
        hitData.setUrl(article.getId());
        hitData.setHitNum(article.getHitNum());
        return hitData;
    }
}
