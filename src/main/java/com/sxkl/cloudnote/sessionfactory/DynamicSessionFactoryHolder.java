package com.sxkl.cloudnote.sessionfactory;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;

/**
 * @author wangyao  
 * @date 2018年1月14日 下午4:26:19  
 * @description:  动态切换sessionfactory
 */
public class DynamicSessionFactoryHolder {

    private static final ThreadLocal<String> THREAD_SESSION_FACTORY = new ThreadLocal<>();

    @Logger(message = "获取SessionFactory")
    public static String getSessionFactory() {
        return THREAD_SESSION_FACTORY.get();
    }

    @Logger(message = "设置SessionFactory")
    public static void setSessionFactory(String sessionfactory) {
        THREAD_SESSION_FACTORY.set(sessionfactory);
    }

    @Logger(message = "删除SessionFactory")
    public static void cleaeSessionFactory() {
        THREAD_SESSION_FACTORY.remove();
    }

    @Logger(message = "设置默认SessionFactory")
    public static void setDefaultSessionFactory() {
        THREAD_SESSION_FACTORY.set(Constant.DEFAULT_SESSION_FACTORY);
    }
}