package com.sxkl.cloudnote.spider.entity;

import lombok.Data;

/**
 *  * @author wangyao
 *  * @date 2018年1月7日 下午2:25:45
 *  * @description: 新闻实体类
 *  
 */
@Data
public class News {

    private String content;
    private String source;
    private String date;
    private String href;
    private String image;
}
