package com.sxkl.cloudnote.waitingtask.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.waitingtask.entity.WaitingTask;

@Repository
public class WaitingTaskDao extends BaseDao<String, WaitingTask> {

    @SuppressWarnings("unchecked")
    public List<WaitingTask> findPage(int pageIndex, int pageSize, String userId) {
        String hql = "select new WaitingTask(id,name,createDate,beginDate,expireDate,process,content,taskType) from WaitingTask w where w.user.id=:userId order by w.createDate desc";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setFirstResult(pageIndex * pageSize);
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

    @SuppressWarnings("unchecked")
    public List<WaitingTask> findAllUnDoByUserId(String userId) {
        String hql = "select new WaitingTask(id,name,createDate,beginDate,expireDate,process,content,taskType) from WaitingTask w where w.user.id=:userId and w.process<100";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        return query.list();
    }

    public int findAllUnDoCount(String userId) {
        String hql = "select count(1) from cn_waitingtask w where w.uId=:userId and w.process<100";
        Session session = this.getSessionFactory().getCurrentSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<WaitingTask> findWeek(String userId, Date date) {
        String hql = "select new WaitingTask(id,name,createDate,beginDate,expireDate,process,content,taskType) "
                + "from WaitingTask w where w.user.id=:userId and w.createDate>:begindate and w.createDate<:enddate";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setDate("begindate", date);
        query.setDate("enddate", new Date());
        return query.list();
    }
}
