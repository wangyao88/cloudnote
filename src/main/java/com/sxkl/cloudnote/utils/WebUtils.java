package com.sxkl.cloudnote.utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: wangyao
 * @date: 2018年1月25日 上午11:27:33
 * @description:
 */
@Slf4j
public class WebUtils {
	
	public static <T> T Requst2Bean(HttpServletRequest request, Class<T> bean) {
		T t = null;
		try {
			t = bean.newInstance();
			Enumeration<String> parameterNames = request.getParameterNames();
			DateConverter convert = new DateConverter();
			String[] patterns = {"yyyy/MM","yyyy/MM/dd","yyyy-MM-dd", "yyyy/MM/dd hh:mm:ss"};
			convert.setPatterns(patterns);
			ConvertUtils.register(convert, Date.class);
			Set<String> filedSet = getFields(bean);
			while (parameterNames.hasMoreElements()) {
				String searchName = parameterNames.nextElement();
				String value = request.getParameter(searchName);
				System.out.println(searchName + "," + value);
				if(filedSet.contains(searchName) && !StringUtils.isEmpty(value)){
					BeanUtils.setProperty(t, searchName, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("设置：{}属性失败！错误信息:{}",bean.getName(),e.getMessage());
		}
		return t;
	}

	private static <T> Set<String> getFields(Class<T> bean) {
		Field[] fileds = bean.getDeclaredFields();
		Set<String> filedSet = Sets.newHashSet();
		for(Field field : fileds){
			filedSet.add(field.getName());
		}
		return filedSet;
	}
}
