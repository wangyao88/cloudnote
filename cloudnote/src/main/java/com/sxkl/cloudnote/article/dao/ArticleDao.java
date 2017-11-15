package com.sxkl.cloudnote.article.dao;

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

	public Article selectArticleById(String id) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.get(Article.class, id);
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

	public void updateArticle(Article article) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(article);
		session.flush();
	}

}
