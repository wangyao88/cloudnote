package com.sxkl.cloudnote.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.utils.StringAppendUtils;

/**
 * @author wangyao
 * @date 2018年1月13日 下午12:44:56
 * @description: hibernate抽象Dao
 */
public class BaseDao<ID extends Serializable,E> extends AbstractBaseDao{
	
	private Class<E> clazz;

    @SuppressWarnings("unchecked")
    public BaseDao() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<E>) type.getActualTypeArguments()[1];
    }
    
    public String getClassName() {
    	String className = clazz.getName();
    	className = className.substring(className.lastIndexOf(".")+1,className.length());
		return className;
	}

	protected Session getSession(){
        return this.getSessionFactory().getCurrentSession();
    }

	public E findOne(ID id){
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
	
	public void deleteById(ID id){
		getSession().delete(clazz.getName(),id);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findAll(){
		String hql = StringAppendUtils.append("from ",getClassName());
		Query query = getSession().createQuery(hql);  
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<E> findPage(Page page){
		String hql;
		if(page.isUseDefaultHql()){
			hql = StringAppendUtils.append("from ",getClassName(), " e where e.userId=:userId");
		}else{
			hql = page.getHql();
		}
		Query query = getSession().createQuery(hql);
		if(page.isUseDefaultHql()){
			query.setString("userId", page.getUserId());
		}
		query.setFirstResult(page.getIndex()*page.getSize());
	    query.setMaxResults(page.getSize());
		return query.list();
	}
}
