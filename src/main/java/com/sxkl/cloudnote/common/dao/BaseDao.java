package com.sxkl.cloudnote.common.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.SessionFactoryUtils;

import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.utils.StringAppendUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wangyao
 * @date 2018年1月13日 下午12:44:56
 * @description: hibernate抽象Dao
 */
@Slf4j
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
        return this.getDynamicSessionFactory(this.getClass()).getCurrentSession();
    }
	
	public Connection getConnection(){
		Connection connection = null;
		try {
			connection = SessionFactoryUtils.getDataSource(this.getDynamicSessionFactory(this.getClass())).getConnection();
		} catch (SQLException e) {
			log.error("获取数据库连接失败！出错误信息:{}",e.getMessage());
		}
		return connection;
	}

	public E findOne(ID id){
		return getSession().load(clazz, id);
	}
	
	public void save(E e){
		getSession().save(e);
		getSession().flush();
	}
	
	public void update(E e){
		getSession().update(e);
		getSession().flush();
	}
	
	public void saveOrUpdate(E e){
		getSession().saveOrUpdate(e);
		getSession().flush();
	}
	
	public void delete(E e){
		getSession().delete(e);
		getSession().flush();
	}
	
	public void deleteById(ID id){
		getSession().delete(clazz.getName(),id);
		getSession().flush();
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
