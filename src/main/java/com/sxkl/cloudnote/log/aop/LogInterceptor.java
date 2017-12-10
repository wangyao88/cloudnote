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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.log.entity.LogLevel;
import com.sxkl.cloudnote.log.service.LogService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.IPUtils;
import com.sxkl.cloudnote.utils.UserUtil;

@Aspect
@Component
public class LogInterceptor {
	
	@Autowired
	private LogService logService;
	
	@Around("@annotation(logger)")
	public Object afterMethod(ProceedingJoinPoint pjp, Logger logger) throws Throwable{
		Log log = configurateLog(pjp);
		LogLevel logLevel = LogLevel.valueOf(LogLevel.class, logger.logLevel());
		long start = System.currentTimeMillis();
		Object object = pjp.proceed();
		long end = System.currentTimeMillis();
		long costTime = end - start;
		log.setLogLevel(logLevel);
		log.setCostTime(costTime);
		logService.showLogInConsole(log);
		return object;
	}
	
	@AfterThrowing(pointcut="execution(* com.sxkl.cloudnote.*.service.*.*(..))", throwing="e")
    public void doAfterThrowing(JoinPoint jp,Throwable e){
		Log log = configurateLog(jp);
		log.setLogLevel(LogLevel.ERROR);
		log.setErrorMsg(e.getMessage());
		logService.showLogInConsole(log);
	}

	private Log configurateLog(JoinPoint jp) {
		Signature signature = jp.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		Method method = methodSignature.getMethod();
		String methodName = method.getName();
		String className = jp.getTarget().getClass().getName();
		String message = getServiceMethodDescription(method);
		Date happenTime = new Date();
		Log log = new Log();
		log.setClassName(className);
		log.setMethodName(methodName);
		log.setMessage(message);
		log.setDate(happenTime);
		HttpServletRequest request = getRequest();
		if(request != null){
			try {
				log.setIp(IPUtils.getIPAddr(request));
			} catch (Exception e) {
				System.out.println("获取用户IP失败！错误信息："+e.getMessage());
			}
			User user = UserUtil.getSessionUser(request);
			if(user != null){
				log.setUserId(user.getId());
				log.setUserName(user.getName());
			}
		}
		return log;
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