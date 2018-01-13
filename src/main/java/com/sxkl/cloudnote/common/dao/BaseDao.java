package com.sxkl.cloudnote.common.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sxkl.cloudnote.utils.StringAppendUtils;

/**
 * @author wangyao
 * @date 2018年1月13日 下午12:44:56
 * @description: hibernate抽象Dao
 */
public class BaseDao<E> extends AbstractBaseDao{
	
	private Class<E> clazz; //T的具体类

    @SuppressWarnings("unchecked")
    public BaseDao() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<E>) type.getActualTypeArguments()[0];
    }
    
    public String getClassName() {
    	String className = clazz.getName();
    	className = className.substring(className.lastIndexOf(".")+1,className.length());
		return className;
	}

	protected Session getSession(){
        return this.getSessionFactory().getCurrentSession();
    }

	public E findOne(String id){
		return getSession().load(clazz, id);
	}
	
	public void save(E e){
		getSession().save(e);
	}
	
	public void update(E e){
		getSession().update(e);
	}
	
	public void saveOrUpdate(E e){
		getSession().saveOrUpdate(e);
	}
	
	public void delete(E e){
		getSession().delete(e);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findAll(){
		String hql = StringAppendUtils.append("from ",getClassName());
		Query query = getSession().createQuery(hql);  
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findPage(int pageIndex, int pageSize){
		String hql = StringAppendUtils.append("from ",getClassName());
		Query query = getSession().createQuery(hql);  
	    query.setFirstResult(pageIndex*pageSize);
	    query.setMaxResults(pageSize);
		return query.list();
	}
}
