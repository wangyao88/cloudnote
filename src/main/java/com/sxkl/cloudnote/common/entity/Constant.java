package com.sxkl.cloudnote.common.entity;

import com.sxkl.cloudnote.user.entity.User;

public class Constant {
	
	public static final String USER_IN_SESSION_KEY = "user";
	
	public static final String TREE_MENU_PREFIX = "[";
	
	public static final String TREE_MENU_SUFFIX = "]";
	
	public static final String TREE_MENU_KEY_IN_REDIS = "tree_menu";
	
	public static final String COMMA = ",";
	
	public static final String TREE_MENU_NOTE_ID_PREFIX = "note";
	
	public static final String TREE_MENU_FLAG_ID_PREFIX = "flag";

	public static final String CACHE_PAGE_KEYS = "cache.page.keys";

	public static final String LOGIN_PAGE_KEY_IN_REDIS = "login_page";

	public static final String TREE_FOR_ARTICLE_KEY_IN_REDIS = "tree_article";

	public static final String ARTICLE_CONTENT_DOMAIN = "cloudnote_domain";

	public static final String LOGIN_PAGE_DOMAIN = "login_page_domain";;
	
	public static final String HOT_ARTICLE_KEY_IN_REDIS = "hot_article";
	
	public static final long HOT_ARTICLE_EXPIRE_IN_REDIS = 1000*60*30;
	
	public static String DOMAIN = "";

}
