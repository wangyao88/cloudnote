package com.sxkl.cloudnote.waitingtask.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.waitingtask.entity.WaitingTask;

@Repository
public class WaitingTaskDao extends BaseDao{

	public void insert(WaitingTask waitingTask) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(waitingTask);
	}
	
	public void update(WaitingTask waitingTask) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(waitingTask);
	}
	
	public void delete(WaitingTask waitingTask) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(waitingTask);
	}
	
	@SuppressWarnings("unchecked")
	public List<WaitingTask> findPage(int pageIndex, int pageSize,String userId) {
		String hql = "select new WaitingTask(id,name,createDate,expire,process,content,taskType) from WaitingTask w where w.user.id=:userId";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    query.setString("userId", userId);
	    query.setFirstResult(pageIndex*pageSize);
        query.setMaxResults(pageSize);
		return query.list();
	}
	
	public int findPageCount(String userId) {
		String hql = "select count(1) from cn_waitingtask w where w.uId=:userId";
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(hql);
		query.setString("userId", userId);
		BigInteger bInt = (BigInteger) query.uniqueResult();
	    return bInt.intValue();
	}

	public WaitingTask find(String id) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.get(WaitingTask.class, id);
	}
}
