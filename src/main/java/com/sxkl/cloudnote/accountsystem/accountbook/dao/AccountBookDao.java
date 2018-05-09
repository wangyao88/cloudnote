package com.sxkl.cloudnote.accountsystem.accountbook.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.accountsystem.accountbook.entity.AccountBook;
import com.sxkl.cloudnote.common.dao.BaseDao;

/**
 * @author: wangyao
 * @date: 2018年5月8日 下午2:00:12
 * @description: 
 */
@Repository
public class AccountBookDao extends BaseDao<String,AccountBook> {

	@SuppressWarnings("unchecked")
	public List<AccountBook> getAccountBookList(int pageIndex, int pageSize, String userId, String name) {
		String hql = "select new AccountBook(id,name,mark,createDate) from AccountBook a where a.user.id=:userId";
		if(!StringUtils.isEmpty(name)){
			hql += " and a.name like :name";
		}
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString("userId", userId);
		if(!StringUtils.isEmpty(name)){
			query.setString("name", '%'+name+'%');
		}
	    query.setFirstResult(pageIndex*pageSize);
        query.setMaxResults(pageSize);
		return query.list();
	}
	
	public int getAccountBookListCount(String userId, String name) {
		String hql = "select count(1) from cn_account_book c where c.user_id=:userId";
		if(!StringUtils.isEmpty(name)){
			hql += " and c.name like :name";
		}
		Session session = this.getSession();
		SQLQuery query = session.createSQLQuery(hql);
		query.setString("userId", userId);
		if(!StringUtils.isEmpty(name)){
			query.setString("name", '%'+name+'%');
		}
		BigInteger bInt = (BigInteger) query.uniqueResult();
	    return bInt.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<AccountBook> findAllSimple(String userId) {
		String hql = "select new AccountBook(id,name) from AccountBook a where a.user.id=:userId";
		Session session = this.getSession();
		Query query = session.createQuery(hql);
		query.setString("userId", userId);
		return query.list();
	}
}
