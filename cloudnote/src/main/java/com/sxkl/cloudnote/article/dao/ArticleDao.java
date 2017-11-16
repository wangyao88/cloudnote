package com.sxkl.cloudnote.article.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.common.dao.BaseDao;

@Repository
public class ArticleDao extends BaseDao {


	public void insertArticle(Article article) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(article);
	}

	@SuppressWarnings("unchecked")
	public List<Article> selectAllArticlesOrderByCreateTimeAndHitNum(int pageIndex, int pageSize) {
		String hql = "select new Article(id,title,hitNum) from Article a order by a.createTime desc,a.hitNum desc";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);	
	    query.setFirstResult(pageIndex*pageSize);
        query.setMaxResults(pageSize);
		return query.list();
	}
	
	public int selectAllArticlesOrderByCreateTimeAndHitNumCount() {
		String hql = "select count(1) from cn_article";
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(hql);
		BigInteger bInt = (BigInteger) query.uniqueResult();
	    return bInt.intValue();
	}

	public Article selectArticleById(String articleId) {
		String hql = "from Article a where a.id=:articleId";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    query.setString("articleId", articleId);
	    query.setCacheable(true);
		return (Article) query.uniqueResult();
	}

	public List selectAllFlagArticlesOrderByCreateTimeAndHitNum(String flagId, int pageIndex, int pageSize) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.id,a.title,a.hitNum from cn_flag_artile f left join cn_article a on f.article_id=a.id ")
		   .append("where f.flag_id = :flagId ")
		   .append("order by a.createTime desc, a.hitNum desc ");
//		   .append("limit :start, :end ");
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setString("flagId", flagId);
		query.setFirstResult(pageIndex*pageSize);
        query.setMaxResults(pageSize);
		return query.list();
	}
	
	public int selectAllFlagArticlesOrderByCreateTimeAndHitNumCount(String flagId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from cn_flag_artile f left join cn_article a on f.article_id=a.id ")
		   .append("where f.flag_id = :flagId ");
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setString("flagId", flagId);
		BigInteger bInt = (BigInteger) query.uniqueResult();
	    return bInt.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<Article> selectAllNoteArticlesOrderByCreateTimeAndHitNum(String noteId, int pageIndex, int pageSize) {
		String hql = "select new Article(id,title,hitNum) from Article a where a.note.id=:nId  order by a.createTime desc,a.hitNum desc";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    query.setString("nId", noteId);
	    query.setFirstResult(pageIndex*pageSize);
        query.setMaxResults(pageSize);
		return query.list();
	}
	
	public int selectAllNoteArticlesOrderByCreateTimeAndHitNumCount(String noteId) {
		String sql = "select count(1) from cn_article where nId = :nId";
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setString("nId", noteId);
		BigInteger bInt = (BigInteger) query.uniqueResult();
	    return bInt.intValue();
	}

	public void updateArticle(Article article) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(article);
		session.flush();
	}

	public void saveOrUpdateArticle(Article article) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.saveOrUpdate(article);
		session.flush();
	}

	public void deleteArticle(Article article) {
		String hql = "delete from Article a where a.id=:articleId";
		Session session = this.getSessionFactory().getCurrentSession();
	    Query query = session.createQuery(hql);
	    query.setString("articleId", article.getId());
	    query.executeUpdate();
		session.flush();
	}

}
