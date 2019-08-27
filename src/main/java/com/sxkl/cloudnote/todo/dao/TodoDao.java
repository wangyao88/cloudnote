package com.sxkl.cloudnote.todo.dao;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.common.entity.Page;
import com.sxkl.cloudnote.common.entity.PageResult;
import com.sxkl.cloudnote.todo.entity.Todo;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoDao extends BaseDao<String, Todo> {

    @Override
    public PageResult<Todo> findPage(Page page, Todo todo) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(Todo.class);
        if(StringUtils.isNotEmpty(todo.getContent())) {
            criteria.add(Restrictions.like("content", todo.getContent(), MatchMode.ANYWHERE));
        }
        if(StringUtils.isNotEmpty(todo.getStatus())) {
            criteria.add(Restrictions.eq("status", todo.getStatus()));
        }
        if(StringUtils.isNotEmpty(todo.getUserId())) {
            criteria.add(Restrictions.eq("userId", todo.getUserId()));
        }
        if(ObjectUtils.isNotNull(todo.getStartDate())) {
            criteria.add(Restrictions.ge("beginDateTime", todo.getStartDate()));
        }
        if(ObjectUtils.isNotNull(todo.getEndDate())) {
            criteria.add(Restrictions.le("endDateTime", todo.getEndDate()));
        }
        int count = (int)criteria.setProjection(Projections.rowCount()).uniqueResult();
        criteria.setProjection(null);
        criteria.setFirstResult(page.getIndex() * page.getSize());
        criteria.setMaxResults(page.getSize());
        List<Todo> result = criteria.list();
        return PageResult.<Todo>builder().total(count).datas(result).build();
    }

    public Todo findEarlestTodo() {
        return null;
    }
}