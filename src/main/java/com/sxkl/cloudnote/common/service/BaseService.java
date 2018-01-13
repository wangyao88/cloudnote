package com.sxkl.cloudnote.common.service;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;

/**
 * @author wangyao
 * @date 2018年1月13日 下午1:12:13
 * @description:
 */
public abstract class BaseService<ID extends Serializable,E> {

	protected abstract BaseDao<ID,E> getDao();
	
	public E findOne(ID id){
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
	
	public void deleteById(ID id){
		E e = findOne(id);
		getDao().delete(e);
	}
	
	public List<E> findAll(){
		return getDao().findAll();
	}
	
	public List<E> findPage(Page page){
		return getDao().findPage(page);
	}
	
	public Page getPage(HttpServletRequest request){
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		User user = UserUtil.getSessionUser(request);
		Page page = new Page();
		page.setIndex(Integer.parseInt(pageIndex));
		page.setSize(Integer.parseInt(pageSize));
		page.setUserId(user.getId());
		return page;
	}
}
