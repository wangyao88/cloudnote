package com.sxkl.cloudnote.user.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.user.entity.User;

@Repository
public class UserDao extends BaseDao {

	public List<User> getUserByNameAndPass(String userName, String password) {
		User user = new User();
		user.setName(userName);
		user.setPassword(password);
		return this.getHibernateTemplate().findByExample(user);
	}

	public void updateUser(User user) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(user);
	}

	public void insertUser(User user) {
		this.getHibernateTemplate().save(user);
		this.getHibernateTemplate().flush();
	}

	public void deleteUser(User user) {
		this.getHibernateTemplate().delete(user);
		this.getHibernateTemplate().flush();
	}

	public User selectUser(User sessionUser) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.load(sessionUser.getClass(), sessionUser.getId());
	}

}
