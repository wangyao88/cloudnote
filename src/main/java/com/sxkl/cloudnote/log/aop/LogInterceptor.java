package com.sxkl.cloudnote.log.aop;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.log.entity.LogLevel;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;

@Aspect
@Component
public class LogInterceptor {
	
	@Around("@annotation(logger)")
	public Object afterMethod(ProceedingJoinPoint pjp, Logger logger) throws Throwable{
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		Method method = methodSignature.getMethod();
		String methodName = method.getName();
		String className = pjp.getTarget().getClass().getName();
		String message = logger.message();
		LogLevel logLevel = LogLevel.valueOf(LogLevel.class, logger.logLevel());
		Date happenTime = new Date();
		long start = System.currentTimeMillis();
		Object object = pjp.proceed();// 执行该方法
		long end = System.currentTimeMillis();
		long costTime = end - start;
		Log log = new Log();
		log.setLogLevel(logLevel);
		log.setClassName(className);
		log.setMethodName(methodName);
		log.setMessage(message);
		log.setDate(happenTime);
		log.setCostTime(costTime);
		HttpServletRequest request = getRequest();
		User user = UserUtil.getSessionUser(request);
		if(user != null){
			log.setIp(request.getRemoteAddr());
			log.setUserId(user.getId());
			log.setUserName(user.getName());
		}
		System.out.println(log);
		return object;
	}
	
	@AfterThrowing(pointcut="execution(* com.sxkl.cloudnote.*.service.*.*(..))", throwing="e")
    public void doAfterThrowing(JoinPoint jp,Throwable e){
		Signature signature = jp.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		Method method = methodSignature.getMethod();
		String methodName = method.getName();
		String className = jp.getTarget().getClass().getName();
		String message = getServiceMethodDescription(method);
		Date happenTime = new Date();
		Log log = new Log();
		log.setLogLevel(LogLevel.ERROR);
		log.setClassName(className);
		log.setMethodName(methodName);
		log.setMessage(message);
		log.setDate(happenTime);
		log.setErrorMsg(e.getMessage());
		HttpServletRequest request = getRequest();
		User user = UserUtil.getSessionUser(request);
		if(user != null){
			log.setIp(request.getRemoteAddr());
			log.setUserId(user.getId());
			log.setUserName(user.getName());
		}
		System.out.println(log);
	}
	
	private String getServiceMethodDescription(Method method) {
		String methodDescription = method.getName();
		try {
			Logger logger = method.getAnnotation(Logger.class);
			methodDescription = logger.message();
		} catch (Exception e) {
		}
		return methodDescription;
	}

	private HttpServletRequest getRequest(){
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		} catch (Exception e) {
			request = null;
		}
	    return request;
	}

}
