package com.sxkl.cloudnote.cache.aop;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.cache.annotation.RedisDisCachable;
import com.sxkl.cloudnote.common.entity.Constant;

@Aspect
@Component
public class RedisCacheAop {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Around("@annotation(redisCachable)")
	public Object doBasicProfiling(ProceedingJoinPoint pjp, RedisCachable redisCachable) throws Throwable {
		String key = redisCachable.key();
		if(!Constant.LOGIN_PAGE_KEY_IN_REDIS.equals(key)){
			key += Constant.SESSION_USER.getId();
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
		String userId = Constant.SESSION_USER.getId();
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
}
