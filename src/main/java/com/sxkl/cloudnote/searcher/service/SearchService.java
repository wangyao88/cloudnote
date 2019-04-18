package com.sxkl.cloudnote.searcher.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.log.annotation.Logger;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class SearchService {

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
        return articles;
    }

    public long count(String words) {
        try {
            String url = "http://127.0.0.1:11000/es/count";
            HttpResponse<String> response = Unirest.get(url).queryString("words", words).asString();
            String count = response.getBody();
            return Long.valueOf(count);
        }catch (Exception e) {
            log.error("搜索知识库失败！错误信息：", e.getMessage());
        }
        return 0;
    }
}
