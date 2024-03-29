package com.sxkl.cloudnote.user.dao;

import java.util.List;

import com.sxkl.cloudnote.log.annotation.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.user.entity.User;

@Repository
public class UserDao extends BaseDao<String, User> {

    @Logger(message = "根据用户名和密码获取用户")
    public List<User> getUserByNameAndPass(String userName, String password) {
        User user = new User();
        user.setName(userName);
        user.setPassword(password);
        return this.getHibernateTemplate().findByExample(user);
    }

    @Logger(message = "获取用户")
    public User selectUser(User sessionUser) {
        Session session = this.getSessionFactory().getCurrentSession();
        return session.load(sessionUser.getClass(), sessionUser.getId());
    }

    @Logger(message = "获取用户好友")
    @SuppressWarnings("unchecked")
    public List<User> getFriends(String userId) {
        String hql = "select new User(u.id, u.name)from User u where u.id <> :userId";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        return query.list();
    }

    @Logger(message = "根据用户名获取用户")
    public User getUserByName(String name) {
        String hql = "select new User(u.id, u.name)from User u where u.name=:name";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("name", name);
        return (User) query.uniqueResult();
    }
}