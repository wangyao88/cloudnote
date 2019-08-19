package com.sxkl.cloudnote.common.entity;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import com.sxkl.cloudnote.config.zkclient.ZKClientConfig;

public class Constant {

    public static final String PROJECT_NAME = "cloudnote";

    public static final String USER_IN_SESSION_KEY = "user";

    public static final String TREE_MENU_PREFIX = "[";

    public static final String TREE_MENU_SUFFIX = "]";

    public static final String TREE_MENU_KEY_IN_REDIS = "tree_menu";

    public static final String COMMA = ",";

    public static final String TREE_MENU_NOTE_ID_PREFIX = "note";

    public static final String TREE_MENU_FLAG_ID_PREFIX = "flag";

    public static final String CACHE_PAGE_KEYS = "cache.page.keys";

    public static final String LOGIN_PAGE_KEY_IN_REDIS_PROVIDE = "provide_login_page";

    public static final String LOGIN_PAGE_KEY_IN_REDIS_PRODUCE = "produce_login_page";

    public static final String TREE_FOR_ARTICLE_KEY_IN_REDIS = "tree_article";

    public static final String ARTICLE_CONTENT_DOMAIN = "cloudnote_domain";

    public static final String LOGIN_PAGE_DOMAIN = "login_page_domain";
    ;

    public static final String HOT_ARTICLE_KEY_IN_REDIS = "hot_article";

    public static final long HOT_ARTICLE_EXPIRE_IN_REDIS = 1000 * 60 * 30;

    public static final String WORD_ARTICLE_MAPPING_IN_REDIS = "word_article_mapping";

    public static final String DOMAIN = ZKClientConfig.getDomain();

    public static final String ONLINE_SESSION_PREFIX_IN_REDIS = "online_session";

    public static final String UEDITOR_UPLOAD_PATH = "/ueditor/jsp/upload/image";

    public static final String SQL_FILE_EXTENSION = ".sql";

    public static final String DRAFT_PATH_PREFIX = "/ueditor/jsp/upload/file/";

    public static final String DOWNLOAD_DRAFT = "/file/downloadDraft?filepath=";

    public static final String STRING_EMPTY = "";

    public static final String SLASH = "slash";

    public static final String FILE_SEPARATOR = "/";

    public static final String WAITING_TASK_PREFIX_FOR_WEBSOCKET = "waitingtask-";

    public static final String IMAGE_SAVED_PATH = "/home/wy/backup/images";
//	public static final String IMAGE_SAVED_PATH = "/Users/localadmin/temp/images";

    public static String REAL_DRAFT_PATH = STRING_EMPTY;//seted in login

    private static ConcurrentHashMap<String, HttpSession> onLineSession = new ConcurrentHashMap<String, HttpSession>();

    public static final String NETARTICLE_KEY_IN_REDIS = "net_article";

    public static final String SIMPLE_NETARTICLE_KEY_IN_REDIS = "simple_net_article";

    public static final String DEFAULT_SESSION_FACTORY = "sessionFactory";

    public static final String CVM_SESSION_FACTORY = "cvmSessionFactory";

    public static void onLine(String userId, HttpSession session) {
        onLineSession.put(userId, session);
    }

    public static void outLine(String userId) {
        onLineSession.remove(userId);
    }

    public static int onLineNum() {
        return onLineSession.size();
    }

}
