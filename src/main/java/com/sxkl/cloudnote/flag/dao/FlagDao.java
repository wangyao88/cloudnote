package com.sxkl.cloudnote.flag.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.flag.entity.Flag;

@Repository
public class FlagDao extends BaseDao<String,Flag> {

	@SuppressWarnings("unchecked")
	public List<Flag> selectFlagsByIds(String[] flags) {
		Session session = this.getSessionFactory().getCurrentSession();
		String hql="from Flag f where f.id in (:flags)";  
		Query query = session.createQuery(hql);  
		query.setParameterList("flags", flags); 
		return query.list();
	}

//	@SuppressWarnings("unchecked")
//	public List<Flag> getAllFlagByUserId(String userId) {
//		Session session = this.getSessionFactory().getCurrentSession();
//		String hql="from Flag f where f.user.id = :userId";  
//		Query query = session.createQuery(hql);  
//		query.setString("userId", userId); 
//		return query.list();
//	}
	
	@SuppressWarnings("rawtypes")
	public List getAllFlagByUserId(String userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select f.id,f.name,f.fId from cn_flag f ")
		   .append("where f.uId = :userId");
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setString("userId", userId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Flag> getAllByName(String name, String userId) {
		String hql = "select new Flag(id,name) from Flag a where a.name like :name and a.user.id=:userId";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    query.setString("name", '%'+name+'%');
	    query.setString("userId", userId);
	    query.setFirstResult(0);
        query.setMaxResults(11);
		return query.list();
	}

	public Flag getByName(String flagName) {
		String hql = "select new Flag(id,name) from Flag a where a.name = :name";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    query.setString("name", flagName);
		return (Flag) query.uniqueResult();
	}

}
