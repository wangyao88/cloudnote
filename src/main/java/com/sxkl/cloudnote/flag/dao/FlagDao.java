package com.sxkl.cloudnote.flag.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.flag.entity.Flag;

@Repository
public class FlagDao extends BaseDao {

	public Flag selectFlagById(String pid) {
		Session session = this.getSessionFactory().getCurrentSession();
		try {
			return session.get(Flag.class, pid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public void saveFlag(Flag flag) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(flag);
	}

	public void deleteFlag(Flag flag) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(flag);
	}

	public void updateFlag(Flag flag) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(flag);
	}

	public void clearSession(){
		Session session = this.getSessionFactory().getCurrentSession();
		session.clear();
	}

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
		   .append("where f.uId = :userId ");
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setString("userId", userId);
		return query.list();
	}

}
