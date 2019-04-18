package com.sxkl.cloudnote.eventdriven.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ArticlePublisherBean{

	private String articleId;
	private String articleTitle;
	private String articleContent;
	private int hitNum;
	private Date CreateTime;
	private String domain;
	private DutyType dutype;
	private String userId;
	private String noteId;

}
