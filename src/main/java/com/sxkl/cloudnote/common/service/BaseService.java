package com.sxkl.cloudnote.common.service;

import java.util.List;

import com.sxkl.cloudnote.common.dao.BaseDao;

/**
 * @author wangyao
 * @date 2018年1月13日 下午1:12:13
 * @description:
 */
public abstract class BaseService<E> {

	protected abstract BaseDao<E> getDao();
	
	public E findOne(String id){
		return getDao().findOne(id);
	}
	
	public void save(E e){
		getDao().save(e);
	}
	
	public void update(E e){
		getDao().update(e);
	}
	
	public void saveOrUpdate(E e){
		getDao().saveOrUpdate(e);
	}
	
	public void delete(E e){
		getDao().delete(e);
	}
	
	public List<E> findAll(){
		return getDao().findAll();
	}
	
	public List<E> findPage(int pageIndex, int pageSize){
		return getDao().findPage(pageIndex,pageSize);
	}
}
