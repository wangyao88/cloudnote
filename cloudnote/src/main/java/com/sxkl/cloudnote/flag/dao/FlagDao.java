package com.sxkl.cloudnote.flag.dao;

import java.util.List;

import org.hibernate.Query;
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

}
