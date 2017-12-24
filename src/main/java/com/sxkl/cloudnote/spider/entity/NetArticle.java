package com.sxkl.cloudnote.spider.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class NetArticle implements Serializable{
	
	private static final long serialVersionUID = -3057153756196294585L;
	private String id;
	private String title;
	private String url;
	private String content;
	private String date;
	private String category;
}
