package com.sxkl.cloudnote.article.dao;

import com.google.common.base.Joiner;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.entity.SameArticle;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.log.annotation.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class ArticleDao extends BaseDao<String, Article> {

    @Logger(message = "分页查询指定用户的笔记列表，并且按照创建时间和阅读次数倒序排序")
    @SuppressWarnings("unchecked")
    public List<Article> selectAllArticlesOrderByCreateTimeAndHitNum(int pageIndex, int pageSize, String userId) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.user.id=:userId order by a.createTime desc,a.hitNum desc";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "查询指定用户的所有笔记的数量")
    public int selectAllArticlesOrderByCreateTimeAndHitNumCount(String userId) {
        String hql = "select count(1) from cn_article c where c.uId=:userId";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @Logger(message = "分页查询指定用户笔记列表，标题模糊查询，按阅读次数数量倒序排序")
    @SuppressWarnings("unchecked")
    public List<Article> selectAllArticlesByNameOrderByHitNum(String articleTitle, int pageIndex, int pageSize, String userId) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.title like :title and a.user.id=:userId order by a.hitNum desc";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("title", '%' + articleTitle + '%');
        query.setString("userId", userId);
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "查询指定用户模糊匹配标题的笔记数量")
    public int selectAllArticlesByNameOrderByCreateTimeAndHitNumCount(String articleTitle, String userId) {
        String hql = "select count(1) from cn_article a where a.title like :title and a.uId=:userId";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("title", '%' + articleTitle + '%');
        query.setString("userId", userId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @Logger(message = "分页查询指定用户笔记列表，标签精确匹配，按阅读次数量倒序排序")
    @SuppressWarnings("rawtypes")
    public List selectAllFlagArticlesOrderByCreateTimeAndHitNum(String flagId, int pageIndex, int pageSize) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id,a.title,a.hitNum from cn_flag_artile f left join cn_article a on f.article_id=a.id ")
                .append("where f.flag_id = :flagId ")
                .append("order by a.createTime desc, a.hitNum desc ");
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(sql.toString());
        query.setString("flagId", flagId);
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "查询指定用户笔记数量，标签精确匹配")
    public int selectAllFlagArticlesOrderByCreateTimeAndHitNumCount(String flagId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) from cn_flag_artile f left join cn_article a on f.article_id=a.id ")
                .append("where f.flag_id = :flagId ");
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(sql.toString());
        query.setString("flagId", flagId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @Logger(message = "分页查询指定用户笔记列表，笔记本精确匹配，按月度数量倒序排序")
    @SuppressWarnings("unchecked")
    public List<Article> selectAllNoteArticlesOrderByCreateTimeAndHitNum(String noteId, int pageIndex, int pageSize) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.note.id=:nId  order by a.createTime desc,a.hitNum desc";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("nId", noteId);
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "查询指定用户笔记数量，笔记本精确匹配")
    public int selectAllNoteArticlesOrderByCreateTimeAndHitNumCount(String noteId) {
        String sql = "select count(1) from cn_article where nId = :nId";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(sql);
        query.setString("nId", noteId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

//	public void updateArticle(Article article) {
//		Session session = this.getSession();
//		session.update(article);
//		session.flush();
//	}

//	public void saveOrUpdateArticle(Article article) {
//		Session session = this.getSession();
//		session.saveOrUpdate(article);
//		session.flush();
//	}

//	public void deleteArticle(Article article) {
//		String hql = "delete from Article a where a.id=:articleId";
//		Session session = this.getSession();
//	    Query query = session.createQuery(hql);
//	    query.setString("articleId", article.getId());
//	    query.executeUpdate();
//		session.flush();
//	}

    @Logger(message = "分页查询指定用户笔记列表，按阅读次数倒序排序")
    @SuppressWarnings("unchecked")
    public List<Article> selectAllArticlesOrderByHitNum(int pageIndex, int pageSize, String userId) {
        String hql = "select new Article(id,content) from Article a where a.user.id=:userId order by a.hitNum desc";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "笔记阅读次数+1")
    public void increaseHitNum(String articleId) {
        String hql = "update Article a set a.hitNum=a.hitNum+1 where a.id=:articleId";
        Session session = this.getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        query.setString("articleId", articleId);
        query.executeUpdate();
        session.flush();
        session.close();
    }

    @Logger(message = "查询笔记，附件名模糊匹配")
    public Article getArticleByDraftName(String fileName) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.content like :content";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("content", '%' + fileName + '%');
        return (Article) query.uniqueResult();
    }

    @Logger(message = "分页查询指定用户笔记列表，标题模糊匹配")
    @SuppressWarnings("unchecked")
    public List<Article> selectAllArticlesByTitle(String title, int pageIndex, int pageSize, String userId) {
        String hql = "select new Article(id,title,content,hitNum) from Article a where a.title like :title and a.user.id=:userId";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("title", '%' + title + '%');
        query.setString("userId", userId);
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "分页查询指定用户笔记列表，内容模糊匹配")
    @SuppressWarnings("unchecked")
    public List<Article> selectAllArticlesByContent(String content, int pageIndex, int pageSize, String userId) {
        String hql = "select new Article(id,title,content,hitNum) from Article a where a.content like :content and a.user.id=:userId";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("content", '%' + content + '%');
        query.setString("userId", userId);
        query.setFirstResult(pageIndex * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "查询所有笔记列表，标题模糊匹配")
    @SuppressWarnings("unchecked")
    public List<Article> getAllByTitle(String title) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.title like :title order by a.hitNum desc";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("title", '%' + title + '%');
        query.setFirstResult(0);
        query.setMaxResults(11);
        return query.list();
    }

    @Logger(message = "查询指定用户所有笔记列表，标题模糊匹配")
    @SuppressWarnings("unchecked")
    public List<Article> getAllByTitle(String title, String userId) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.title=:title and uId=:uId";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("title", title);
        query.setString("uId", userId);
        return query.list();
    }

    @Logger(message = "查询指定用户所有笔记列表")
    @SuppressWarnings("unchecked")
    public List<Article> getAllArticles(String userId) {
        String hql = "select new Article(id,title,content,hitNum) from Article a where uId=:uId";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("uId", userId);
        return query.list();
    }

    @Logger(message = "查询指定用户笔记列表，主键集合精确匹配")
    @SuppressWarnings("unchecked")
    public List<Article> getArticlesByIds(List<String> ids, String userId) {
        String hql = "select new Article(id,title,hitNum) from Article a where uId=:uId and a.id in (:ids)";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("uId", userId);
        query.setParameterList("ids", ids);
        return query.list();
    }

    @Logger(message = "分页查询指定用户笔记列表")
    @SuppressWarnings("unchecked")
    public List<Article> findPage(int currentPage, int pageSize, String userId) {
        String hql = "select new Article(id,title,content,hitNum) from Article a where a.user.id=:userId order by a.id";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setFirstResult(currentPage * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "查询推荐笔记列表")
    @SuppressWarnings("unchecked")
    public List<Article> getRecommend(Article article) {
        String hql = "from Article a where a.isShared=:isShared order by a.hitNum desc";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setBoolean("isShared", article.isShared());
        query.setFirstResult(0);
        query.setMaxResults(5);
        return query.list();
    }

    @Logger(message = "查询笔记，主键精确匹配")
    public Article getArticleById(String id) {
        Session session = this.getSession();
        return session.load(Article.class, id);
    }

    @Logger(message = "查询最新笔记列表")
    @SuppressWarnings("unchecked")
    public List<Article> getRecent(Article article) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.isShared=:isShared order by a.createTime desc";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setBoolean("isShared", article.isShared());
        query.setFirstResult(0);
        query.setMaxResults(10);
        return query.list();
    }

    @Logger(message = "查询博客总数")
    public int getBlogTotal() {
        String sql = "select count(1) from cn_article where is_shared = 1";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(sql);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @Logger(message = "分页查询博客列表")
    @SuppressWarnings("unchecked")
    public List<Article> getBlogList(Article article, int page, int pageSize) {
        String sql = "from Article a where a.isShared=:isShared order by a.hitNum desc";
        Session session = this.getSession();
        Query query = session.createQuery(sql);
        query.setBoolean("isShared", article.isShared());
        query.setFirstResult(page * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    @Logger(message = "查询笔记，图片名称模糊匹配")
    public Article getArticleByImageName(String name) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.content like :name";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("name", Joiner.on(name).join("%", "%"));
        return (Article) query.uniqueResult();
    }

    @Logger(message = "查询相似笔记")
    public SameArticle getSameArticleIds(String id) {
        String hql = "select new SameArticle(id,sameIds) from SameArticle a where a.id = :id";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setString("id", id);
        return (SameArticle) query.uniqueResult();
    }

    @Logger(message = "查询相似笔记列表")
    public List<Article> getSameArticlesInIds(List<String> idList) {
        String hql = "select new Article(id,title,hitNum) from Article a where a.id in :idList";
        Session session = this.getSession();
        Query query = session.createQuery(hql);
        query.setParameterList("idList", idList);
        return query.list();
    }
}