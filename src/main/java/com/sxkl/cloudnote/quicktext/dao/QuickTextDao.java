package com.sxkl.cloudnote.quicktext.dao;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.quicktext.entity.QuickText;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 *  * @author wangyao
 *  * @date 2018年1月14日 下午3:26:58
 *  * @description:
 *  
 */
@Repository
public class QuickTextDao extends BaseDao<String, QuickText> {

    @Logger(message = "获取所有的QuickText")
    public List<QuickText> findAll(String userId) {
        Session session = this.getSessionFactory().getCurrentSession();
        String hql = "select new QuickText(id, title) from QuickText n where n.userId=:userId order by n.updateDateTime desc, createDateTime desc";
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        return query.list();
    }

    @Logger(message = "获取QuickText的总量")
    public int count(String userId) {
        String hql = "select count(1) from cn_quicktext a where a.userId=:userId";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }
}