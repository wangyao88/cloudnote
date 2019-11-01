package com.sxkl.cloudnote.searcher.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.CloudnoteServiceUrlConstant;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class SearchService {

    private static final String HOT_LABELS_ZSET_KEY_IN_REDIS = "hot_labels";
    private static final int HOT_LABELS_ZSET_SIZE = 14;
    private static final String RECOMMEND_ARTICLE_LIST_KEY_IN_REDIS = "recommend_article";
    private static final int RECOMMEND_ARTICLE_LIST_SIZE = 9;
    private static final String TODAY_NEWS_LIST_KEY_IN_REDIS = "today_news";
    private static final int TODAY_NEWS_LIST_SIZE = 9;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ArticleService articleService;

    @Logger(message = "分页查询笔记")
    public List<Article> searchPage(String words, int page, int size) {
        List<Article> articles = Lists.newArrayList();
        try {
            String url = CloudnoteServiceUrlConstant.SEARCH_URL;
            Map<String, Object> params = Maps.newHashMap();
            params.put("words", words);
            params.put("page", page);
            params.put("size", size);
            HttpResponse<JsonNode> response = Unirest.get(url).queryString(params).asJson();
            JsonNode body = response.getBody();
            JSONArray jsonArray = body.getArray();
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObject.put("isShared", jsonObject.getInt("isShared") == 1);
                articles.add(gson.fromJson(jsonObject.toString(), Article.class));
            }
        } catch (Exception e) {
            log.error("搜索知识库失败！错误信息：" + Throwables.getStackTraceAsString(e));
        }
        return articles;
    }

    @Logger(message = "查询 分页查询笔记 的总量")
    public long count(String words) {
        try {
            String url = CloudnoteServiceUrlConstant.COUNT_URL;
            HttpResponse<String> response = Unirest.get(url).queryString("words", words).asString();
            String count = response.getBody();
            return Long.valueOf(count);
        } catch (Exception e) {
            log.error("搜索知识库命中数量失败！错误信息：" + Throwables.getStackTraceAsString(e));
        }
        return 0;
    }

    @Logger(message = "查询知识库总数")
    public long total() {
        try {
            String url = CloudnoteServiceUrlConstant.TOTAL_URL;
            HttpResponse<String> response = Unirest.get(url).asString();
            String total = response.getBody();
            return Long.valueOf(total);
        } catch (Exception e) {
            log.error("搜索知识库总数量失败！错误信息：" + Throwables.getStackTraceAsString(e));
        }
        return 0;
    }

    @Logger(message = "获取热门标签")
    public Set<Object> getHotLabel() {
        return redisTemplate.opsForZSet().reverseRange(HOT_LABELS_ZSET_KEY_IN_REDIS, 0, HOT_LABELS_ZSET_SIZE);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getHotLabelWithScore() {
        return redisTemplate.opsForZSet().reverseRangeWithScores(HOT_LABELS_ZSET_KEY_IN_REDIS, 0, 5);
    }

    @Logger(message = "获取推荐笔记")
    public List<Article> getRecommendArticles() {
        return articleService.getRecent();
        // title url source date
//        return redisTemplate.opsForList().range(RECOMMEND_ARTICLE_LIST_KEY_IN_REDIS, 0, RECOMMEND_ARTICLE_LIST_SIZE);
    }

    @Logger(message = "获取今日新闻")
    public List<Object> getTodayNews() {
        // title url source date
        return redisTemplate.opsForList().range(TODAY_NEWS_LIST_KEY_IN_REDIS, 0, TODAY_NEWS_LIST_SIZE);
    }
}