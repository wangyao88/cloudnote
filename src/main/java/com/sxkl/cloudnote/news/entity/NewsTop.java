package com.sxkl.cloudnote.news.entity;

import java.util.List;

import lombok.Data;

/**
 * @author: wangyao
 * @date: 2018年4月27日 下午1:10:33
 * @description: 
 */
@Data
public class NewsTop {

	private String title;
	private String category;
	private List<News> news;
}
