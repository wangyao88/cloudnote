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
import com.sxkl.cloudnote.common.service.DomainService;

@Aspect
@Component
public class RedisCacheAop {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private DomainService domainService;

	@Around("@annotation(redisCachable)")
	public Object doBasicProfiling(ProceedingJoinPoint pjp, RedisCachable redisCachable) throws Throwable {
		String key = redisCachable.key();
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
		String[] keys = redisDisCachable.key();
		for(String key : keys){
			boolean hasCached = redisTemplate.hasKey(key);
			if(hasCached){
				redisTemplate.delete(key);
			}
		}
		Object object = pjp.proceed();// 执行该方法
		return object;
	}
	
	private Object loginPageFilter(String cacheKey, Object value){
		if(Constant.LOGIN_PAGE_KEY_IN_REDIS.equals(cacheKey)){
			String page = String.valueOf(value);
			String domain = domainService.getDomain();
			page = page.replaceAll(Constant.LOGIN_PAGE_DOMAIN,domain);
			return page;
		}
		return value;
	}
}
