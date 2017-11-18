package com.sxkl.cloudnote.article.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ArticleForCache implements Serializable{

	private static final long serialVersionUID = 5761758553627421217L;
	private String id;
	private String content;
}
