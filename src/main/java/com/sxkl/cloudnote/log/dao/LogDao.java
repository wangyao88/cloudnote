package com.sxkl.cloudnote.log.dao;

import com.google.common.collect.Maps;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.sessionfactory.SessionFactory;
import com.sxkl.cloudnote.statistic.model.DateRange;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 *  * @author wangyao
 *  * @date 2018年1月14日 下午3:26:58
 *  * @description:
 *  
 */
@SessionFactory(Constant.CVM_SESSION_FACTORY)
@Repository
public class LogDao extends BaseDao<String, Log> {

    public int getLogNum(String userId) {
        String hql = "select count(1) from cn_log c where c.userId=:userId";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    public Map<String, String> getBarPercentData(String userId, DateRange dateRange) {
        String hql = "select level, count(1) from cn_log where date >= :start and date < :end and userId = :userId group by level";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setDate("start", dateRange.getStart());
        query.setDate("end", dateRange.getEnd());
        query.setString("userId", userId);
        List list = query.list();
        Map<String, String> result = Maps.newHashMap();
        for (Object obj : list) {
            Object[] arr = (Object[]) obj;
            result.put(arr[0].toString(), arr[1].toString());
        }
        return result;
    }

    public List<Log> getLogs(int pageIndex, int pageSize, String userId) {
        String hql = "from Log where userId=:userId";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setFirstResult((pageIndex-1)*pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }
}
