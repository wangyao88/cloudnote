package com.sxkl.cloudnote.user.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.note.entity.Note;
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

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		String hql = "from User u";
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<User> getFriends(String userId) {
		String hql = "select new User(u.id, u.name)from User u where u.id <> :userId";
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("userId", userId);
		return query.list();
	}

	public User getUserByName(String name) {
		String hql = "select new User(u.id, u.name)from User u where u.name=:name";
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("name", name);
		return (User) query.uniqueResult();
	}

	public void insert(User user) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(user);
	}

}
