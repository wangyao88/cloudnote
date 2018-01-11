package com.sxkl.cloudnote.eventdriven.entity;

import lombok.Data;

@Data
public class ArticlePublisherBean{

	private String articleId;
	private String articleTitle;
	private String articleContent;
	private int hitNum;
	private String domain;
	private DutyType dutype;
	private String userId;

}
