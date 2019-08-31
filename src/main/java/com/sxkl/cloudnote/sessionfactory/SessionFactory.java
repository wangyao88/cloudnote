package com.sxkl.cloudnote.sessionfactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.sxkl.cloudnote.common.entity.Constant;

/**
 *  * @author wangyao
 *  * @date 2018年1月14日 下午2:59:57
 *  * @description: 数据源注解
 *  
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SessionFactory {

    String value() default Constant.DEFAULT_SESSION_FACTORY;
}