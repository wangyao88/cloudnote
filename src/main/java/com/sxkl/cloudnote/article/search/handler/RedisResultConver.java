package com.sxkl.cloudnote.article.search.handler;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.IndexManager;

/**
 * @author: wangyao
 * @date:2018年1月12日 上午9:20:52
 */
@Service
public class RedisResultConver {

    @Autowired
    private IndexManager indexManager;
    @Autowired
    private RedisTemplate<String, List<Article>> redisTemplate;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Article> convertMulti(String userId, Set keysInRedis) {
        List<Article> result = Lists.newArrayList();
        List articles = redisTemplate.opsForHash().multiGet(indexManager.getWordArticleMappingKey(userId), keysInRedis);
        if (articles.isEmpty()) {
            return result;
        }
        for (Object objs : articles) {
            for (Object obj : (List) objs) {
                result.add((Article) obj);
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public List<Article> convertSingle(String userId, String keyInRedis) {
        List<Article> result = Lists.newArrayList();
        Object articles = redisTemplate.opsForHash().get(indexManager.getWordArticleMappingKey(userId), keyInRedis);
        if (Objects.isNull(articles)) {
            return result;
        }
        for (Object obj : (List) articles) {
            result.add((Article) obj);
        }
        return result;
    }


}
