package com.sxkl.cloudnote.quicktext.dao;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.quicktext.entity.Quicktext;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  * @author wangyao
 *  * @date 2018年1月14日 下午3:26:58
 *  * @description:
 *  
 */
@Repository
public class QuicktextDao extends BaseDao<String, Quicktext> {

    public List<Quicktext> findAll(String userId) {
        Session session = this.getSessionFactory().getCurrentSession();
        String hql = "from Quicktext n where n.userId=:userId";
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        return query.list();
    }
}