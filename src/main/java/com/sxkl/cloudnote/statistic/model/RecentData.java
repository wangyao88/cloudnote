package com.sxkl.cloudnote.statistic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sxkl.cloudnote.article.entity.Article;
import lombok.Data;

import java.util.Date;

/**
 * 新增文章
 *
 * @author wangyao
 */
@Data
public class RecentData {

    private Integer index;
    private String title;
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH::mm:ss", timezone = "GMT+8")
    private Date createDate;

    public static RecentData configureHitData(int index, Article article) {
        RecentData recentData = new RecentData();
        recentData.setIndex(index);
        recentData.setTitle(article.getTitle());
        recentData.setUrl(article.getId());
        recentData.setCreateDate(article.getCreateTime());
        return recentData;
    }
}
