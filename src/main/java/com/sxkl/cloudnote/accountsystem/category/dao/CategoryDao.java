package com.sxkl.cloudnote.accountsystem.category.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.accountsystem.category.entity.Category;
import com.sxkl.cloudnote.common.dao.BaseDao;

/**
 * @author: wangyao
 * @date: 2018年5月8日 下午2:00:12
 * @description:
 */
@Repository
public class CategoryDao extends BaseDao<String, Category> {

    @SuppressWarnings("rawtypes")
    public List findAllByAccountBookId(String accountBookId, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id,f.name,f.parent_id from cn_category f ")
                .append("where f.account_book_id = :accountBookId and f.type=:type");
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(sql.toString());
        query.setString("accountBookId", accountBookId);
        query.setString("type", type);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Category> getCategory(String accountBookId, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select new Category(id,name) from Category c where 1=1");
        if (!StringUtils.isEmpty(accountBookId)) {
            sql.append(" and c.accountBook.id=:accountBookId");
        }
        if (!StringUtils.isEmpty(type)) {
            sql.append(" and c.type=:type");
        }
        Session session = this.getSession();
        Query query = session.createQuery(sql.toString());
        if (!StringUtils.isEmpty(accountBookId)) {
            query.setString("accountBookId", accountBookId);
        }
        if (!StringUtils.isEmpty(type)) {
            query.setString("type", type);
        }
        return query.list();
    }
}
