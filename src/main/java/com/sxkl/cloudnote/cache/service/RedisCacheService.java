package com.sxkl.cloudnote.cache.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisCacheService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public void initAllData() throws Exception {
        RedisCacheService redisCacheServiceProxy = (RedisCacheService) AopContext.currentProxy();
        //请缓存
        redisCacheServiceProxy.clearAllCache();
    }

    public String clearAllCache() {
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    @RedisCachable(key = Constant.LOGIN_PAGE_KEY_IN_REDIS_PRODUCE, dateTime = 200)
    public String getProduceLoginPageFromCache(HttpServletResponse resp, HttpServletRequest req, FilterChain filterChain) throws IOException, ServletException {
        return getPageFromCache(resp, req, filterChain);
    }

    @RedisCachable(key = Constant.LOGIN_PAGE_KEY_IN_REDIS_PROVIDE, dateTime = 200)
    public String getProvideLoginPageFromCache(HttpServletResponse resp, HttpServletRequest req, FilterChain filterChain) throws IOException, ServletException {
        return getPageFromCache(resp, req, filterChain);
    }

    private String getPageFromCache(HttpServletResponse resp, HttpServletRequest req, FilterChain filterChain)
            throws IOException, ServletException {
        ResponseWrapper wrapper = new ResponseWrapper(resp);
        filterChain.doFilter(req, wrapper);
        String loginPage = wrapper.getResult();
        String domain = Constant.DOMAIN;
        loginPage = loginPage.replaceAll(domain, Constant.LOGIN_PAGE_DOMAIN);
        log.info("<--------------------登录页缓存不存在，生成缓存-------------------->");
        return loginPage;
    }

    public void cacheMap(String hotArticleKeyInRedis, Map<String, String> result) {
        redisTemplate.opsForHash().putAll(hotArticleKeyInRedis, result);
    }

    public String getValueFromHash(String hotArticleKeyInRedis, String hashKey, HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        String userId = sessionUser.getId();
        hotArticleKeyInRedis += userId;
        if (redisTemplate.opsForHash().hasKey(hotArticleKeyInRedis, hashKey)) {
            return String.valueOf(redisTemplate.opsForHash().get(hotArticleKeyInRedis, hashKey));
        }
        return null;
    }

}
