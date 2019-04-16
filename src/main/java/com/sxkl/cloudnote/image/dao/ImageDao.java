package com.sxkl.cloudnote.image.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.image.entity.Image;

import lombok.Cleanup;

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

	public int getTotal() {
		String hql = "select count(1) from cn_image";
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(hql);
		BigInteger bInt = (BigInteger) query.uniqueResult();
		return bInt.intValue();
	}

	public void deleteAll(List<Image> results) {
		Session session = this.getSession();
		for(Image image : results){
			session.delete(image);
		}
		session.flush();
	}

	public void updateAll(List<Image> results) {
		if(results.isEmpty()){
			return;
		}
		Transaction tx = null;
		try {
			String sql = "update cn_image set aId = ? where id = ?";
			@Cleanup
			Session session = this.getSession();
			tx = session.beginTransaction();
			@Cleanup
			Connection conn = this.getConnection();
			@Cleanup
			PreparedStatement stmt = conn.prepareStatement(sql);
			int size = results.size();
			for(int i = 0; i < size; i++){
				Image image = results.get(i);
				stmt.setString(1,image.getAId());
				stmt.setString(2,image.getId());
				stmt.addBatch();
				if(i%50 == 0){
					stmt.executeBatch();
				}
			}
			stmt.executeBatch();
			tx.commit();
		} catch (SQLException e) {
			tx.rollback();
		}
	}

	public List<Image> getAll() {
		Session session = this.getSessionFactory().getCurrentSession();
		String hql = "select new Image(id, name, aId) from Image";
		Query query = session.createQuery(hql);
		List<Image> images = query.list();
		return images;
	}

	public Image getOne(String id) {
		String hql = "from Image i where i.id = :id";
		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setString("id", id);
		return (Image) query.uniqueResult();
	}
}
