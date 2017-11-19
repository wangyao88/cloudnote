package com.sxkl.cloudnote.eventdriven.entity;

import lombok.Data;

@Data
public class ArticlePublisherBean{

	private String articleId;
	private String articleContent;
	private String domain;
	private DutyType dutype;

}
