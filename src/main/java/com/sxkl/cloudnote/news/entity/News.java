package com.sxkl.cloudnote.news.entity;

import lombok.Data;

/**
 * @author: wangyao
 * @date: 2018年4月27日 下午1:05:36
 * @description: 
 */
@Data
public class News {

	private String title;
	private String source;
	private int num;
	private String date;
	private String url;
	
	public News() {
		super();
	}
	
	public News(String title, String source, String date, String url) {
		super();
		this.title = title;
		this.source = source;
		this.date = date;
		this.url = url;
	}
	
	public News(String title, String source, int num, String date, String url) {
		super();
		this.title = title;
		this.source = source;
		this.num = num;
		this.date = date;
		this.url = url;
	}
}
