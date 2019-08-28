package com.sxkl.cloudnote.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class RequestUtils {

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = null;
        try {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            request = null;
        }
        return request;
    }

    public static <T> T requestToBean(Class<T> classType, HttpServletRequest request) {
        try {
            T bean = classType.newInstance();
            Enumeration<String> parameterNames = request.getParameterNames();
            while(parameterNames.hasMoreElements()){
                String name = parameterNames.nextElement();
                String value = request.getParameter(name);
                BeanUtils.setProperty(bean, name, value);
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}