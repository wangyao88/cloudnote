package com.sxkl.cloudnote.image.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.sxkl.cloudnote.log.annotation.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.image.entity.Image;

import lombok.Cleanup;

@Repository
public class ImageDao extends BaseDao<String, Image> {

    @Logger(message = "获取图片，图片名模糊匹配")
    public Image getImageByName(String name) {
        String hql = "select new Image(id, name, aId, createDate) from Image where name = :name";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("name", name);
        return (Image) query.uniqueResult();
    }

    @Logger(message = "更新图片关联的笔记外键，图片名模糊匹配")
    public int updateImageArticleId(String articleId, String imageName) {
        String hql = "update Image i set i.aId = :aId where i.name = :name";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("aId", articleId);
        query.setString("name", imageName);
        return query.executeUpdate();
    }

    @Logger(message = "根据笔记主键删除图片")
    public int deleteImageByArticleId(String articleId) {
        String hql = "delete from Image i where i.aId = :articleId";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("articleId", articleId);
        return query.executeUpdate();
    }

    @Logger(message = "获取图片总数")
    public int getTotal() {
        String hql = "select count(1) from cn_image";
        Session session = this.getSessionFactory().getCurrentSession();
        SQLQuery query = session.createSQLQuery(hql);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @Logger(message = "删除指定图片")
    public void deleteAll(List<Image> results) {
        Session session = this.getSession();
        for (Image image : results) {
            session.delete(image);
        }
        session.flush();
    }

    @Logger(message = "更新指定图片集合")
    public void updateAll(List<Image> results) {
        if (results.isEmpty()) {
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
            for (int i = 0; i < size; i++) {
                Image image = results.get(i);
                stmt.setString(1, image.getAId());
                stmt.setString(2, image.getId());
                stmt.addBatch();
                if (i % 50 == 0) {
                    stmt.executeBatch();
                }
            }
            stmt.executeBatch();
            tx.commit();
        } catch (SQLException e) {
            tx.rollback();
        }
    }

    @Logger(message = "获取所有图片")
    public List<Image> getAll() {
        Session session = this.getSessionFactory().getCurrentSession();
        String hql = "select new Image(id, name, aId) from Image";
        Query query = session.createQuery(hql);
        List<Image> images = query.list();
        return images;
    }

    @Logger(message = "获取指定图片")
    public Image getOne(String id) {
        String hql = "from Image i where i.id = :id";
        Session session = this.getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("id", id);
        return (Image) query.uniqueResult();
    }
}