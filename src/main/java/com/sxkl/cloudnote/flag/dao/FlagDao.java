package com.sxkl.cloudnote.flag.dao;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.log.annotation.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class FlagDao extends BaseDao<String, Flag> {

    @Logger(message = "根据主键获取标签列表")
    @SuppressWarnings("unchecked")
    public List<Flag> selectFlagsByIds(String[] flags) {
        Session session = this.getSessionFactory().getCurrentSession();
        String hql = "from Flag f where f.id in (:flags)";
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

    @Logger(message = "获取指定用户的所有标签")
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

    @Logger(message = "获取指定用户的标签列表， 标签名模糊匹配")
    @SuppressWarnings("unchecked")
    public List<Flag> getAllByName(String name, String userId) {
        String hql = "select new Flag(id,name) from Flag a where a.name like :name and a.user.id=:userId";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("name", '%' + name + '%');
        query.setString("userId", userId);
        query.setFirstResult(0);
        query.setMaxResults(11);
        return query.list();
    }

    @Logger(message = "获取标签，标签名模糊匹配")
    public Flag getByName(String flagName) {
        String hql = "select new Flag(id,name) from Flag a where a.name = :name";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("name", flagName);
        return (Flag) query.uniqueResult();
    }

    @Logger(message = "获取指定用户拥有的标签数量")
    public int getFlagNum(String userId) {
        String hql = "select count(1) from cn_flag c where c.uId=:userId";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @Logger(message = "获取指定用户拥有的标签")
    public List<Flag> getFlagDatas(String userId) {
        String hql = "select b.name, a.num from (select flag_id, count(1) as num from cn_flag_artile group by flag_id) a left join cn_flag b on a.flag_id=b.id where b.uId=:userId order by a.num desc";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        query.setFirstResult(0);
        query.setMaxResults(5);
        List list = query.list();
        List<Flag> flags = Lists.newArrayListWithCapacity(list.size());
        for (Object obj : list) {
            Flag flag = new Flag();
            Object[] arr = (Object[]) obj;
            flag.setName(arr[0].toString());
            flag.setNum(Integer.parseInt(arr[1].toString()));
            flags.add(flag);
        }
        return flags;
    }

    @Logger(message = "获取标签，标签名精确匹配")
    public List<Flag> getFlagsByName(String name, String parentName) {
        String hql = "select new Flag(id,name) from Flag f where f.name = :name and f.parent.name = :parentName";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setParameter("name", name);
        query.setParameter("parentName", parentName);
        return query.list();
    }
}
