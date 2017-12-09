package com.sxkl.cloudnote.cache.aop;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.cache.annotation.RedisDisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;

@Aspect
@Component
public class RedisCacheAop {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	private static final String REQUEST_NAME = "request";

	@Around("@annotation(redisCachable)")
	public synchronized Object doBasicProfiling(ProceedingJoinPoint pjp, RedisCachable redisCachable) throws Throwable {
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		String[] paramNames = methodSignature.getParameterNames();
		int i = 0;
		for(String paramName : paramNames){
			if(REQUEST_NAME.equals(paramName)){
				break;
			}
			i++;
		}
		Object[] objs = pjp.getArgs();
		String key = redisCachable.key();
		if(!Constant.LOGIN_PAGE_KEY_IN_REDIS.equals(key)){
			HttpServletRequest request = (HttpServletRequest) objs[i];
			if(request == null){
				request = getRequest();
			}
			i = 0;
			User sessionUser = UserUtil.getSessionUser(request);
			key += sessionUser.getId();
		}
		long dateTime = redisCachable.dateTime();
		boolean hasCached = redisTemplate.hasKey(key);
		if(hasCached){
			Object value = redisTemplate.opsForValue().get(key);
			value = loginPageFilter(key,value);
			return value;
		}
		Object object = pjp.proceed();// 执行该方法
		redisTemplate.opsForValue().set(key, object);
		if(dateTime != -1L && dateTime > 0L){
			long baseKeyTime = redisTemplate.getExpire(key);
			if (baseKeyTime == -1) {
				redisTemplate.expire(key, dateTime, TimeUnit.MINUTES);
			}
		}
		return object;
	}
	
	@Around("@annotation(redisDisCachable)")
	public Object doBasicProfiling1(ProceedingJoinPoint pjp, RedisDisCachable redisDisCachable) throws Throwable {
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature)signature;
		String[] paramNames = methodSignature.getParameterNames();
		int i = 0;
		for(String paramName : paramNames){
			if(REQUEST_NAME.equals(paramName)){
				break;
			}
			i++;
		}
		Object[] objs = pjp.getArgs();
		HttpServletRequest request = (HttpServletRequest) objs[i];
		if(request == null){
			request = getRequest();
		}
		i = 0;
		User sessionUser = UserUtil.getSessionUser(request);
		String userId = sessionUser.getId();
		String[] keys = redisDisCachable.key();
		for(String key : keys){
			boolean hasCached = redisTemplate.hasKey(key+userId);
			if(hasCached){
				redisTemplate.delete(key+userId);
			}
		}
		Object object = pjp.proceed();// 执行该方法
		return object;
	}
	
	private Object loginPageFilter(String cacheKey, Object value){
		if(Constant.LOGIN_PAGE_KEY_IN_REDIS.equals(cacheKey)){
			String page = String.valueOf(value);
			page = page.replaceAll(Constant.LOGIN_PAGE_DOMAIN,Constant.DOMAIN);
			return page;
		}
		return value;
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
