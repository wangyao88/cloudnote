package com.sxkl.cloudnote.eventdriven.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.sxkl.cloudnote.log.annotation.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OnlineSessionListener implements HttpSessionListener, ApplicationContextAware {

    @Logger(message = "创建session")
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
//		redisCacheService.cacheSessionInfo(session);
        log.info("session[" + session.getId() + "]已上线");
    }

    @Logger(message = "销毁session")
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
//		redisCacheService.unCacheSessionInfo(session);
        log.info("session[" + session.getId() + "]已下线");
    }

    @Logger(message = "setApplicationContext")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}