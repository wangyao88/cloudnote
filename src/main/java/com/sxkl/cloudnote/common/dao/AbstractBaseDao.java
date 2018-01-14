package com.sxkl.cloudnote.common.dao;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.sessionfactory.DynamicSessionFactoryHolder;
import com.sxkl.cloudnote.sessionfactory.ParseAnnotation;

@Repository
public class AbstractBaseDao extends HibernateDaoSupport{ 
	
	@Autowired
	private ApplicationContext applicationContext;
    /** 
     * 说明: 
     * 1.在既使用注解又使用HibernateDaoSupport的情况下,只能这么写, 
     * 原因是HibernateDaoSupport是抽象类,且方法都是final修饰的, 
     * 这样就不能为其注入sessionFactory或者hibernateTemplate 
     * 2.若使用xml配置的话,就可以直接给HibernateDaoSupport注入. 
     */  
    //而使用HibernateDaosupport,又必须为其注入sessionFactory或者hibernateTemplate  
      
    //这里为其注入sessionFactory,最后只需要让自己的Dao继承这个MyDaoSupport.  
    //不直接在自己的Dao里继承HibernateDaoSupport的原因是这样可以简化代码,  
    //不用每次都为其注入sessionFactory或者hibernateTemplate了,在这里注入一次就够了.  
    @Resource(name="sessionFactory")  
    public void setSuperSessionFactory(SessionFactory sessionFactory){  
        super.setSessionFactory(sessionFactory);  
    } 
    
    @SuppressWarnings("rawtypes")
	public SessionFactory getDynamicSessionFactory(Class clazz){
    	String sessionFactoryName = ParseAnnotation.getSessionFactoryName(clazz);
    	return (SessionFactory) applicationContext.getBean(sessionFactoryName);
    }
      
//  或者为其注入hibernateTemplate  
//  @Resource(name="hibernateTemplate")  
//  public void setSuperHibernateTemplate(HibernateTemplate hibernateTemplate){  
//      super.setHibernateTemplate(hibernateTemplate);  
//  }  
}  
