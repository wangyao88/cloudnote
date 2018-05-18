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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sxkl.cloudnote.cache.service.RedisCacheService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.config.service.ConfigService;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.SpringContextUtil;

@Component
public class CacheFilter  implements Filter, ApplicationContextAware {
	
    private WebApplicationContext springContext;
    private RedisCacheService redisCacheService;
    @Autowired
	private ConfigService configService;
    
    @Override
    public void init(FilterConfig config) throws ServletException {
        springContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        redisCacheService = (RedisCacheService) springContext.getBean("redisCacheService");
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String requestUrl = req.getRequestURI()+"-"+req.getMethod();
        configService = (ConfigService) SpringContextUtil.getBean("configService",ConfigService.class);
        String cachePages = configService.getCachePages();
        //访问登录页，并且是GET请求，则拦截
        if(cachePages.contains(requestUrl)){
        	String html = "provide".equals(PropertyUtil.getMode()) ? 
        			redisCacheService.getProvideLoginPageFromCache(resp,req,filterChain) : 
        			redisCacheService.getProduceLoginPageFromCache(resp, req, filterChain);
        	html = html.replaceAll(Constant.LOGIN_PAGE_DOMAIN, Constant.DOMAIN);
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
    
//    private String getDomain(HttpServletRequest request){
//		StringBuilder domain = new StringBuilder();
//		domain.append(request.getScheme())
//			  .append("://")
//			  .append(request.getServerName())
//			  .append(":")
//			  .append(request.getServerPort())
//			  .append(request.getContextPath());
//		return domain.toString();
//	}
}

