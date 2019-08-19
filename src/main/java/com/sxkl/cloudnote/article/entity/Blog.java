package com.sxkl.cloudnote.article.entity;

import lombok.Data;

/**
 *  * @author wangyao
 *  * @date 2018年3月22日 下午11:23:08
 *  * @description:博客文章
 *  
 */
@Data
public class Blog {

    private String id;
    private String title;
    private String generalization;
    private String content;
    private String createDate;
    private String author;
    private String flags;
    private int hitNum;
    private String imgUrl;
}
