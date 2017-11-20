package com.sxkl.cloudnote.webchat.dao;

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

}
