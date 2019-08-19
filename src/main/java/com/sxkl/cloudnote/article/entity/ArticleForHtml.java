package com.sxkl.cloudnote.article.entity;

import java.io.Serializable;

import lombok.Data;


@Data
public class ArticleForHtml implements Serializable {

    private static final long serialVersionUID = 88293756691459840L;

    private String id;

    private String title;

    private String content;

    private Integer hitNum;

    public ArticleForHtml() {
        super();
    }

    public ArticleForHtml(String id, String title, Integer hitNum) {
        this.id = id;
        this.title = title;
        this.hitNum = hitNum;
    }
}
