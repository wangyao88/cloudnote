package com.sxkl.cloudnote.eventdriven.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.sxkl.cloudnote.cache.service.RedisCacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OnlineSessionListener implements HttpSessionListener, ApplicationContextAware{
	
	@Autowired
    private RedisCacheService redisCacheService;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		redisCacheService.cacheSessionInfo(session);
		log.info("session["+session.getId()+"]已上线");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		redisCacheService.unCacheSessionInfo(session);
		log.info("session["+session.getId()+"]已下线");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
	}

}
