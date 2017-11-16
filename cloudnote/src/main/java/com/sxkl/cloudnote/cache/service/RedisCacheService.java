package com.sxkl.cloudnote.cache.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.common.entity.Constant;

@Service
public class RedisCacheService {

	@Autowired
    private RedisTemplate<Object, Object> redisTemplate;
	
    public void initAllData() throws Exception{
        RedisCacheService redisCacheServiceProxy = (RedisCacheService)AopContext.currentProxy();
        //请缓存
        redisCacheServiceProxy.clearAllCache();
    }
    
    public String clearAllCache(){
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }
    
    @RedisCachable(key=Constant.LOGIN_PAGE_KEY_IN_REDIS,dateTime=200)
    public String getHtmlFromCache(HttpServletResponse resp, ServletRequest servletRequest, FilterChain filterChain) throws IOException, ServletException {
        ResponseWrapper wrapper = new ResponseWrapper(resp);
        filterChain.doFilter(servletRequest, wrapper);
        System.out.println("缓存不存在，生成缓存");
        return wrapper.getResult();
    }

}
