package com.sxkl.cloudnote.todo.dao;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.todo.entity.Todo;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class TodoDao extends BaseDao<String, Todo> {

    @Logger(message = "根据调价查询todo列表")
    public List<Todo> findAllByExample(Todo todo) {
        Session session = this.getSessionFactory().getCurrentSession();
        String sql = getSql(todo);
        SQLQuery sqlQuery = session.createSQLQuery(sql);
        List<Todo> todos = Lists.newArrayList();
        List<Object[]> list = sqlQuery.list();
        for (Object[] objs : list) {
            todos.add(convertTodo(objs));
        }
        return todos;
//        Criteria criteria = session.createCriteria(Todo.class);
//        List<Criterion> expressions = Lists.newArrayList();
//        if(StringUtils.isNotEmpty(todo.getContent())) {
//            expressions.add(Restrictions.like("content", todo.getContent(), MatchMode.ANYWHERE));
//        }
//        if(StringUtils.isNotEmpty(todo.getStatus())) {
//            expressions.add(Restrictions.eq("status", todo.getStatus()));
//        }
//        if(StringUtils.isNotEmpty(todo.getUserId())) {
//            expressions.add(Restrictions.eq("userId", todo.getUserId()));
//        }
//        if(ObjectUtils.isNotNull(todo.getStartDate())) {
//            expressions.add(Restrictions.ge("beginDateTime", todo.getStartDate()));
//        }
//        if(ObjectUtils.isNotNull(todo.getEndDate())) {
//            LogicalExpression or = Restrictions.or(Restrictions.isNotNull("parent"), Restrictions.and(Restrictions.isNull("parent"), Restrictions.le("endDateTime", todo.getEndDate())));
//            expressions.add(or);
//        }
//        if(!expressions.isEmpty()) {
//            Criterion[] criterias = new Criterion[expressions.size()];
//            Criterion[] array = expressions.toArray(criterias);
//            criteria.add(Restrictions.and(array));
//        }
//        criteria.addOrder(Order.desc("beginDateTime"));
//        int count = (int)criteria.setProjection(Projections.rowCount()).uniqueResult();
//        criteria.setProjection(null);
//        criteria.setFirstResult(page.getIndex() * page.getSize());
//        criteria.setMaxResults(page.getSize());
//        List<Todo> result = criteria.list();
//        return PageResult.<Todo>builder().total(count).datas(result).build();
//        return criteria.list();
    }

    private Todo convertTodo(Object[] objs) {
        Todo todo = new Todo();
        todo.setId(String.valueOf(objs[0]));
        todo.setContent(String.valueOf(objs[1]));
        todo.setBeginDateTime((Date) objs[2]);
        todo.setEndDateTime((Date) objs[3]);
        todo.setStatus(String.valueOf(objs[4]));
        Object obj = objs[5];
        if(ObjectUtils.isNotNull(obj)) {
            Todo parent = new Todo();
            parent.setId(String.valueOf(obj));
            todo.setParent(parent);
        }
        return todo;
    }

    private String getSql(Todo todo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id, content, beginDateTime, endDateTime, status, fId from cn_current_todo");
        StringBuilder where = new StringBuilder(" where 1=1");
        String content = todo.getContent();
        if(StringUtils.isNotEmpty(content)) {
            content = "'%'" + content + "'%' ";
            where.append(" and content like ").append(content);
        }
        if(StringUtils.isNotEmpty(todo.getStatus())) {
            where.append(" and status = '").append(todo.getStatus()).append("'");
        }
        if(StringUtils.isNotEmpty(todo.getUserId())) {
            where.append(" and userId = '").append(todo.getUserId()).append("'");
        }
        if(ObjectUtils.isNotNull(todo.getStartDate())) {
            where.append(" and beginDateTime >= '").append(DateUtils.formatDate2Str(todo.getStartDate())).append("'");
        }

        if(ObjectUtils.isNotNull(todo.getEndDate())) {
            where.append(" and beginDateTime <= '").append(DateUtils.formatDate2Str(todo.getEndDate())).append("'");
        }

        StringBuilder subSelect = new StringBuilder(" or (fId in (select id from cn_current_todo");
        subSelect.append(where.toString()).append("))");

        sql.append(where.toString()).append(subSelect.toString()).append(" order by beginDateTime desc, createDateTime desc");
        return sql.toString();
    }

    @Logger(message = "获取最早的todo")
    public Todo findEarlestTodo(String userId) {
        Session session = this.getSessionFactory().getCurrentSession();
        String hql = "select new Todo(beginDateTime) from Todo n where n.userId=:userId order by n.beginDateTime desc, createDateTime desc";
        Query query = session.createQuery(hql);
        query.setString("userId", userId);
        query.setFirstResult(0);
        query.setMaxResults(1);
        List list = query.list();
        return list.isEmpty() ? new Todo() : (Todo) list.get(0);
    }
}