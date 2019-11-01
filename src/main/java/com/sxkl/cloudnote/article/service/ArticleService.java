package com.sxkl.cloudnote.article.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.*;
import com.sxkl.cloudnote.article.search.handler.impl.LuceneSearcher;
import com.sxkl.cloudnote.article.search.lucene.WordAnalyzer;
import com.sxkl.cloudnote.cache.annotation.RedisDisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.eventdriven.manager.PublishManager;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.image.service.ImageService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.note.service.NoteService;
import com.sxkl.cloudnote.searcher.service.SearchService;
import com.sxkl.cloudnote.spider.entity.SearchComplete;
import com.sxkl.cloudnote.statistic.model.DateRange;
import com.sxkl.cloudnote.statistic.model.KeyValue;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
import com.sxkl.cloudnote.utils.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private NoteService noteService;
    @Autowired
    private FlagService flagService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;
    //	@Qualifier("luceneSearcher")
    @Autowired
    private LuceneSearcher articleSeracher;
    @Autowired
    private SearchService searchService;

    @Logger(message = "添加笔记")
    @RedisDisCachable(key = {Constant.TREE_MENU_KEY_IN_REDIS, Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
    public void addArticle(HttpServletRequest request) {
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        content = FileUtils.replaceAllDomain(content);
        String noteId = request.getParameter("note");
        String flagsStr = request.getParameter("flags");
        String articleId = request.getParameter("articleId");
        String isEdit = request.getParameter("isEdit");
        String isShared = request.getParameter("isShared");
        String[] flagIdTemps = flagsStr.split(",");
        String[] flags = new String[flagIdTemps.length];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = flagIdTemps[i].substring(Constant.TREE_MENU_FLAG_ID_PREFIX.length());
        }
        List<Flag> flagBeans = flagService.selectFlagsByIds(flags);
        User sessionUser = UserUtil.getSessionUser(request);
        User user = userService.selectUser(sessionUser);
        Note note = noteService.selectNoteById(noteId);
        Article article = null;
        if (Boolean.valueOf(isEdit)) {
            article = articleDao.findOne(articleId);
        } else {
            article = new Article();
            article.setHitNum(0);
            article.setCreateTime(new Date());
            article.setUser(user);
        }
        article.setTitle(title);
        article.setNote(note);
        article.setFlags(new HashSet<Flag>(flagBeans));
        article.setShared(Boolean.parseBoolean(isShared));
        String contentFilted = FileUtils.saveHtmlImgToDB(content, imageService);
        contentFilted = FileUtils.filterDraft(contentFilted);
        article.setContent(contentFilted);
        articleDao.saveOrUpdate(article);
        PublishManager.getPublishManager().getArticlePublisher().establishLinkagesBetweenArticleAndImage(article);
        if (Boolean.valueOf(isEdit)) {
            PublishManager.getPublishManager().getArticlePublisher().updateIndexByUpdate(article, user.getId());
        } else {
            PublishManager.getPublishManager().getArticlePublisher().updateIndexByAdd(article, user.getId());
        }
    }

    @SuppressWarnings("rawtypes")
    @Logger(message = "获取所有笔记")
    public String getAllArticles(HttpServletRequest request) {
        String pageIndex = request.getParameter("pageIndex");
        String pageSize = request.getParameter("pageSize");
        String first = request.getParameter("first");
//		String title = request.getParameter("title");
        String title = "";
        String titleOrContent = request.getParameter("titleOrContent");
        User sessionUser = UserUtil.getSessionUser(request);
        String userId = sessionUser.getId();

        List<Article> articles = new ArrayList<Article>();
        List<ArticleForHtml> articleForHtmls = new ArrayList<ArticleForHtml>();
        int total = 0;
        if (Boolean.valueOf(first)) {
            articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex), Integer.parseInt(pageSize), userId);
            total = articleDao.selectAllArticlesOrderByCreateTimeAndHitNumCount(userId);
        } else if (!StringUtils.isEmpty(title)) {
            articles = articleDao.selectAllArticlesByNameOrderByHitNum(title, Integer.parseInt(pageIndex), Integer.parseInt(pageSize), userId);
            total = articleDao.selectAllArticlesByNameOrderByCreateTimeAndHitNumCount(title, userId);
        } else if (!StringUtils.isEmpty(titleOrContent)) {
            articles = searchService.searchPage(titleOrContent, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
            total = Integer.parseInt(searchService.count(titleOrContent) + "");
        } else {
            String flagId = request.getParameter("flagId");
            if (!StringUtils.isEmpty(flagId)) {
                flagId = flagId.substring(Constant.TREE_MENU_FLAG_ID_PREFIX.length());
                List result = articleDao.selectAllFlagArticlesOrderByCreateTimeAndHitNum(flagId, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
                for (Iterator iterator = result.iterator(); iterator.hasNext(); ) {
                    //每个集合元素都是一个数组，数组元素师person_id,person_name,person_age三列值
                    Object[] objects = (Object[]) iterator.next();
                    ArticleForHtml articleForHtml = new ArticleForHtml();
                    articleForHtml.setId(String.valueOf(objects[0]));
                    articleForHtml.setTitle(String.valueOf(objects[1]));
                    articleForHtml.setHitNum(Integer.parseInt(String.valueOf(objects[2])));
                    articleForHtmls.add(articleForHtml);
                }
                total = articleDao.selectAllFlagArticlesOrderByCreateTimeAndHitNumCount(flagId);
                return OperateResultService.configurateSuccessDataGridResult(articleForHtmls, total);
            }
            String noteId = request.getParameter("noteId");
            if (!StringUtils.isEmpty(noteId)) {
                noteId = noteId.substring(Constant.TREE_MENU_NOTE_ID_PREFIX.length());
                articles = articleDao.selectAllNoteArticlesOrderByCreateTimeAndHitNum(noteId, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
                total = articleDao.selectAllNoteArticlesOrderByCreateTimeAndHitNumCount(noteId);
            }
            if (StringUtils.isEmpty(flagId) && StringUtils.isEmpty(noteId)) {
                articles = articleDao.selectAllArticlesOrderByCreateTimeAndHitNum(Integer.parseInt(pageIndex), Integer.parseInt(pageSize), userId);
                total = articleDao.selectAllArticlesOrderByCreateTimeAndHitNumCount(userId);
            }
        }
        return OperateResultService.configurateSuccessDataGridResult(articles, total);
    }

    @Logger(message = "获取笔记")
    public String getArticle(HttpServletRequest request) {
        String id = request.getParameter("id");
        String searchKeys = request.getParameter("searchKeys");
//		String content = redisCacheService.getValueFromHash(Constant.HOT_ARTICLE_KEY_IN_REDIS,id,request);
//		if(StringUtils.isEmpty(content)){
//			Article article = articleDao.selectArticleById(id);
//			content = article.getContent();
//		}

        Article article = articleDao.findOne(id);
        String content = article.getContent();
        content = content.replaceAll(Constant.ARTICLE_CONTENT_DOMAIN, Constant.DOMAIN);
        if (!StringUtils.isEmpty(searchKeys)) {
            Map<String, Integer> keys = WordAnalyzer.analysis(searchKeys);
            Set<String> keySet = keys.keySet();
            for (String key : keySet) {
                content = content.replaceAll(key, Joiner.on("").join("<b><font color='red'>", key, "</font></b>"));
            }
        }
        PublishManager.getPublishManager().getArticlePublisher().increaseArticleHitNum(id);
        return content;
    }

    @Logger(message = "获取笔记")
    public Article getArticle(String id) {
        Article article = articleDao.findOne(id);
        String content = article.getContent();
        content = content.replaceAll(Constant.ARTICLE_CONTENT_DOMAIN, Constant.DOMAIN);
        article.setContent(content);
        PublishManager.getPublishManager().getArticlePublisher().increaseArticleHitNum(id);
        return article;
    }

    @Logger(message = "删除笔记")
    @RedisDisCachable(key = {Constant.TREE_MENU_KEY_IN_REDIS, Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
    public void deleteArticle(HttpServletRequest request) {
        String id = request.getParameter("id");
        User user = UserUtil.getSessionUser(request);
        Article article = articleDao.findOne(id);
        article.setNote(null);
        article.setUser(null);
        article.setFlags(null);
        articleDao.delete(article);
        imageService.deleteImageByArticleId(id);
        PublishManager.getPublishManager().getArticlePublisher().updateIndexByDelete(article, user.getId());
    }

    @Logger(message = "获取待修改笔记")
    public String getArticleForEdit(HttpServletRequest request) {
        String articleId = request.getParameter("articleId");
        Article article = articleDao.findOne(articleId);

        Note note = article.getNote();
        Note noteResult = new Note();
        if (note != null) {
            noteResult.setId(note.getId());
            noteResult.setName(note.getName());
        }

        Flag flagResult = new Flag();
        Set<Flag> flags = article.getFlags();
        if (flags != null) {
            String flagIds = "";
            String flagNames = "";
            for (Flag flag : flags) {
                flagIds += Constant.TREE_MENU_FLAG_ID_PREFIX + flag.getId() + ",";
                flagNames += flag.getName() + ",";
            }
            if (flagIds.endsWith(Constant.COMMA)) {
                flagIds = flagIds.substring(0, flagIds.length() - 1);
            }
            if (flagNames.endsWith(Constant.COMMA)) {
                flagNames = flagNames.substring(0, flagNames.length() - 1);
            }

            flagResult.setId(flagIds);
            flagResult.setName(flagNames);
        }

        article.setHitNum(article.getHitNum() + 1);
        articleDao.update(article);
        String content = article.getContent();
        content = content.replaceAll(Constant.ARTICLE_CONTENT_DOMAIN, Constant.DOMAIN);

//		ArticleForEdit articleForEdit = new ArticleForEdit(noteResult,flagResult,content);
        ArticleForEdit articleForEdit = new ArticleForEdit(noteResult, flagResult, content, article.isShared());

        return new Gson().toJson(articleForEdit);
    }

    @Logger(message = "获取热门笔记")
    public List<ArticleForCache> getHotArticles(String userId, int hotArticleRange) {
        List<Article> articles = articleDao.selectAllArticlesOrderByHitNum(0, hotArticleRange, userId);
        List<ArticleForCache> results = new ArrayList<ArticleForCache>();
        for (Article article : articles) {
            ArticleForCache articleForCache = new ArticleForCache();
            articleForCache.setId(article.getId());
            articleForCache.setContent(article.getContent());
            results.add(articleForCache);
        }
        return results;
    }

    @Logger(message = "获取热门笔记")
    public Article getArticleByDraftName(String fileName) {
        return articleDao.getArticleByDraftName(fileName);
    }

    @Logger(message = "获取自动补全笔记标题")
    public SearchComplete getAllByTitle(String title) {
        List<Article> articles = articleDao.getAllByTitle(title);
        int size = articles.size();
        SearchComplete result = new SearchComplete(size);
        for (int i = 0; i < size; i++) {
            Article article = articles.get(i);
            result.getSuggestions()[i] = article.getTitle();
            result.getData()[i] = article.getId();
        }
        result.setQuery(title);
        return result;
    }

    @Logger(message = "检查笔记标题是否存在")
    public String checkTitle(String title, String userId) {
        List<Article> articles = articleDao.getAllByTitle(title, userId);
        if (!articles.isEmpty()) {
            return OperateResultService.configurateSuccessResult(false);
        }
        return OperateResultService.configurateSuccessResult(true);
    }

    @Logger(message = "获取用户名下所有笔记")
    public List<Article> getAllArticles(String userId) {
        return articleDao.getAllArticles(userId);
    }

    @Logger(message = "根据主键集合获取所有笔记")
    public List<Article> getArticlesByIds(List<String> ids, String userId) {
        return articleDao.getArticlesByIds(ids, userId);
    }

    @Logger(message = "获取笔记数量")
    public int getArticleTotal(String userId) {
        return articleDao.selectAllArticlesOrderByCreateTimeAndHitNumCount(userId);
    }

    @Logger(message = "分页查询笔记")
    public List<Article> findPage(int currentPage, int pageSize, String userId) {
        return articleDao.findPage(currentPage, pageSize, userId);
    }

    @Logger(message = "获取博客首页推荐文章")
    public String getRecommend() {
        Article article = new Article();
        article.setShared(true);
        List<Article> articles = articleDao.getRecommend(article);
        List<Blog> blogs = Lists.newArrayList();
        for (Article temp : articles) {
            blogs.add(transform(temp, false));
        }
        return OperateResultService.configurateSuccessResult(blogs);
    }

    @Logger(message = "获取最新博客列表")
    public String getRecentForBlog() {
        Article article = new Article();
        article.setShared(true);
        List<Article> articles = articleDao.getRecent(article);
        List<Blog> blogs = Lists.newArrayList();
        for (Article temp : articles) {
            Blog blog = new Blog();
            blog.setId(temp.getId());
            blog.setTitle(temp.getTitle());
            blogs.add(blog);
        }
        return OperateResultService.configurateSuccessResult(blogs);
    }

    @Logger(message = "获取最新笔记列表")
    public List<Article> getRecent() {
        Article article = new Article();
        article.setShared(true);
        return articleDao.getRecent(article);
    }

    @Logger(message = "获取博客详情")
    public String getBlog(String id) {
        Article article = articleDao.getArticleById(id);
        int hitNum = article.getHitNum() + 1;
        article.setHitNum(hitNum);
        articleDao.update(article);
        Blog blog = transform(article, true);
        return OperateResultService.configurateSuccessResult(blog);
    }

    @Logger(message = "获取博客总数量")
    public String getTotal() {
        int total = articleDao.getBlogTotal();
        return OperateResultService.configurateSuccessResult(total);
    }

    @Logger(message = "分页获取博客列表")
    public String getBlogList(int page, int pageSize) {
        Article article = new Article();
        article.setShared(true);
        List<Article> articles = articleDao.getBlogList(article, (page - 1), pageSize);
        List<Blog> blogs = Lists.newArrayList();
        for (Article temp : articles) {
            blogs.add(transform(temp, false));
        }
        return OperateResultService.configurateSuccessResult(blogs);
    }

    private Blog transform(Article article, boolean containsContent) {
        Blog blog = new Blog();
        blog.setId(article.getId());
        blog.setAuthor(article.getUser().getName());
        blog.setCreateDate(DateUtils.formatDate2YMDStr(article.getCreateTime()));
        blog.setHitNum(article.getHitNum());
        blog.setTitle(article.getTitle());
        Set<Flag> flags = article.getFlags();
        String flagStr = StringUtils.EMPTY;
        for (Flag flag : flags) {
            flagStr += flag.getName() + ",";
        }
        if (flagStr.endsWith(",")) {
            flagStr = flagStr.substring(0, flagStr.length() - 1);
        }
        blog.setFlags(flagStr);
        String content = article.getContent();
        content = content.replaceAll(Constant.ARTICLE_CONTENT_DOMAIN, Constant.DOMAIN);
        if (containsContent) {
            blog.setContent(content);
        }
        Document contentDoc = Jsoup.parse(content);
        Elements imgs = contentDoc.getElementsByTag("img");
        if (!imgs.isEmpty()) {
            String imgUrl = imgs.get(0).attr("src");
            blog.setImgUrl(imgUrl);
        }
        String text = contentDoc.text();
        if (text.length() > 140) {
            text = text.substring(0, 140);
        }
        blog.setGeneralization(text);
        return blog;
    }

    @Logger(message = "根据图片名获取笔记")
    public Article getArticleByImageName(String name) {
        return articleDao.getArticleByImageName(name);
    }

    @Logger(message = "快速修改笔记")
    public void quickupdate(HttpServletRequest request) {
        String articleId = request.getParameter("articleId");
        String content = request.getParameter("content");
        Article article = articleDao.getArticleById(articleId);
        String contentFilted = FileUtils.saveHtmlImgToDB(content, imageService);
        contentFilted = FileUtils.filterDraft(contentFilted);
        article.setContent(contentFilted);
        articleDao.saveOrUpdate(article);

        User sessionUser = UserUtil.getSessionUser(request);
        User user = userService.selectUser(sessionUser);
        PublishManager.getPublishManager().getArticlePublisher().updateIndexByUpdate(article, user.getId());
    }

    @Logger(message = "查询相似笔记列表")
    public List<Article> getSameArticles(String id) {
        SameArticle sameArticle = articleDao.getSameArticleIds(id);
        if (ObjectUtils.isNull(sameArticle)) {
            return Lists.newArrayList();
        }
        String sameIds = sameArticle.getSameIds();
        if (StringUtils.isBlank(sameIds)) {
            return Lists.newArrayList();
        }
        List<String> idList = StringUtils.str2List(sameIds);
        return articleDao.getSameArticlesInIds(idList);
    }

    public int getArticleNum(String userId) {
        return articleDao.getArticleNum(userId);
    }

    public int getBlogNum(String userId) {
        return articleDao.getBlogNum(userId);
    }

    public int getTodayArticleNum(String userId) {
        return articleDao.getTodayArticleNum(userId);
    }

    public List<Article> getHitDatas(String userId) {
        return articleDao.getHitDatas(userId);
    }

    public List<Article> getRecentDatas(String userId) {
        return articleDao.getRecentDatas(userId);
    }

    public List<KeyValue> getBarData(String userId) {
        DateRange dateRange = DateUtils.getCurrentYearDateRange();
        return articleDao.getBarData(userId, dateRange);
    }
}
