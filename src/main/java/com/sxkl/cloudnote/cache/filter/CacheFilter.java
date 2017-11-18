package com.sxkl.cloudnote.cache.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sxkl.cloudnote.cache.service.RedisCacheService;
import com.sxkl.cloudnote.utils.PropertyUtil;

@Component
public class CacheFilter  implements Filter, ApplicationContextAware {
	
    private WebApplicationContext springContext;
    private RedisTemplate<Object, Object> redisTemplate;
    private RedisCacheService redisCacheService;
    
    @SuppressWarnings("unchecked")
    @Override
    public void init(FilterConfig config) throws ServletException {
        springContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        redisTemplate = (RedisTemplate<Object, Object>) springContext.getBean("redisTemplate");
        redisCacheService = (RedisCacheService) springContext.getBean("redisCacheService");
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String requestUrl = req.getRequestURI()+"-"+req.getMethod();
        String cachePages = PropertyUtil.getCachePages();
        //访问登录页，并且是GET请求，则拦截
        if(cachePages.contains(requestUrl)){
        	String html = redisCacheService.getHtmlFromCache(resp,req,filterChain);
            // 返回响应
            resp.setContentType("text/html; charset=utf-8");
            resp.getWriter().print(html);
            return;
        }
        filterChain.doFilter(servletRequest, resp);
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        
    }
}

