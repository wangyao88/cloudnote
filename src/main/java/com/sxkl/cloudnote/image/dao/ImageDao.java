package com.sxkl.cloudnote.image.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.image.entity.Image;

@Repository
public class ImageDao extends BaseDao<String,Image> {

	public Image getImageByName(String name) {
		String hql = "from Image i where i.name = :name";
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("name", name);
		return (Image) query.uniqueResult();
	}

	public int updateImageArticleId(String articleId, String imageName) {
		String hql = "update Image i set i.aId = :aId where i.name = :name";
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("aId", articleId);
		query.setString("name", imageName);
		return query.executeUpdate();
	}

	public int deleteImageByArticleId(String articleId) {
		String hql = "delete from Image i where i.aId = :articleId";
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("articleId", articleId);
		return query.executeUpdate();
	}
}
