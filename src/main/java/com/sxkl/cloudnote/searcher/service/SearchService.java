package com.sxkl.cloudnote.searcher.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.ikanalyzer.IKAnalyzerHandler;
import com.sxkl.cloudnote.log.annotation.Logger;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Service
public class SearchService {

    private static final String HOT_LABELS_ZSET = "hot_labels";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Logger(message = "搜索知识库")
    public List<Article> searchPage(String words, int page, int size) {
        List<Article> articles = Lists.newArrayList();
        try {
            String url = "http://127.0.0.1:11000/es/search";
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
        }catch (Exception e) {
            log.error("搜索知识库失败！错误信息：", e.getMessage());
        }
        saveSearchWordsToRedis(words);
        return articles;
    }

    private void saveSearchWordsToRedis(String words) {
        List<String> results = IKAnalyzerHandler.handle(words);
        results.forEach(result -> {
            redisTemplate.opsForZSet().incrementScore(HOT_LABELS_ZSET, result, 1);
        });
    }

    public long count(String words) {
        try {
            String url = "http://127.0.0.1:11000/es/count";
            HttpResponse<String> response = Unirest.get(url).queryString("words", words).asString();
            String count = response.getBody();
            return Long.valueOf(count);
        }catch (Exception e) {
            log.error("搜索知识库命中数量失败！错误信息：", e.getMessage());
        }
        return 0;
    }

    public long total() {
        try {
            String url = "http://127.0.0.1:11000/es/total";
            HttpResponse<String> response = Unirest.get(url).asString();
            String total = response.getBody();
            return Long.valueOf(total);
        }catch (Exception e) {
            log.error("搜索知识库总数量失败！错误信息：", e.getMessage());
        }
        return 0;
    }

    public Set<Object> getHotLabel() {
        return redisTemplate.opsForZSet().reverseRange(HOT_LABELS_ZSET, 0, 14);
    }


    public List<Map<String,String>> getRecommendArticles() {
        // TODO 获取推荐文章
        List<Map<String,String>> datas = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = Maps.newHashMap();
            map.put("title", "java"+i);
            map.put("url", "http://www.baidu.com");
            datas.add(map);
        }
        return datas;
    }
}
