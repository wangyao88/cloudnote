package com.sxkl.cloudnote.sessionfactory;

import java.lang.reflect.Method;

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
	 * @param <T>
	 * @param clazz
	 */
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
	 * @param <T>
	 * @param clazz
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	@SuppressWarnings("rawtypes")
	public static String getSessionFactoryName(Class clazz){
		String sessionFactoryName = parseMethod(clazz);
		if(StringUtils.isEmpty(sessionFactoryName)){
			sessionFactoryName = parseType(clazz);
		}
		if(StringUtils.isEmpty(sessionFactoryName)){
			sessionFactoryName = Constant.DEFAULT_SESSION_FACTORY;
		}
		return sessionFactoryName;
	}
	

	public static void main(String[] args) {
		parseMethod(LogDao.class);
		parseType(LogDao.class);
	}

}
