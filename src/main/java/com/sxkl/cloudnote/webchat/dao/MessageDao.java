package com.sxkl.cloudnote.webchat.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.webchat.entity.Message;

@Repository
public class MessageDao extends BaseDao {

	public void saveMessage(Message message){
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(message);
	}

	@SuppressWarnings("unchecked")
	public List<Message> getHistory(String userFrom, String userTo) {
		String hql = "from Message a where (a.from=:userFrom and a.to=:userTo) or (a.from=:userTo and a.to=:userFrom) order by a.date desc";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    query.setString("userFrom", userFrom);
	    query.setString("userTo", userTo);
	    query.setFirstResult(0);
        query.setMaxResults(10);
		return query.list();
	}

}
