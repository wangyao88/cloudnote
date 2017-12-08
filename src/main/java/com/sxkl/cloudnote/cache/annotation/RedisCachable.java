package com.sxkl.cloudnote.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisCachable {
	
	/**
	 * cache key
	 * @return
	 */
    String key() default "";
    
    /**
     * expire time 
     * unit minute
     * @return
     */
    long dateTime() default -1L;
}
