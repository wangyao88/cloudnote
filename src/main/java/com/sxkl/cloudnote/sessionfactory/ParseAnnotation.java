package com.sxkl.cloudnote.sessionfactory;

import java.lang.reflect.Method;

import com.sxkl.cloudnote.log.annotation.Logger;
import org.apache.commons.lang3.StringUtils;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.dao.LogDao;

/**
 * @author wangyao  
 * @date 2018年1月14日 下午5:05:13  
 * @description: 解析注解  
 */
public class ParseAnnotation {

    /**
     * 解析方法注解
     *
     * @param <T>
     * @param clazz
     */
    @Logger(message = "获取方法上的SessionFactory注解信息")
    @SuppressWarnings("rawtypes")
    public static String parseMethod(Class clazz) {
        String sessionFactoryName = StringUtils.EMPTY;
        try {
            for (Method method : clazz.getDeclaredMethods()) {
                SessionFactory sessionFactory = method.getAnnotation(SessionFactory.class);
                if (sessionFactory != null) {
                    sessionFactoryName = sessionFactory.value();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactoryName;
    }

    /**
     * 解析类注解
     *
     * @param <T>
     * @param clazz
     */
    @Logger(message = "获取类上的SessionFactory注解信息")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String parseType(Class clazz) {
        String sessionFactoryName = StringUtils.EMPTY;
        try {
            SessionFactory sessionFactory = (SessionFactory) clazz.getAnnotation(SessionFactory.class);
            if (sessionFactory != null) {
                sessionFactoryName = sessionFactory.value();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sessionFactoryName;
    }

    @Logger(message = "获取类上的SessionFactory注解的SessionFactory名称")
    @SuppressWarnings("rawtypes")
    public static String getSessionFactoryName(Class clazz) {
        String sessionFactoryName = parseMethod(clazz);
        if (StringUtils.isEmpty(sessionFactoryName)) {
            sessionFactoryName = parseType(clazz);
        }
        if (StringUtils.isEmpty(sessionFactoryName)) {
            sessionFactoryName = Constant.DEFAULT_SESSION_FACTORY;
        }
        return sessionFactoryName;
    }
}