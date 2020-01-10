package com.sxkl.cloudnote.note.dao;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.statistic.model.KeyValue;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class NoteDao extends BaseDao<String, Note> {

    @Logger(message = "获取指定用户的所有笔记本")
    @SuppressWarnings("unchecked")
    public List<Note> getAllNote(String uid) {
        Session session = this.getSessionFactory().getCurrentSession();
        String hql = "select new Note(n.id,n.name) from Note n where n.user.id=:uid";
        Query query = session.createQuery(hql);
        query.setString("uid", uid);
        List<Note> notes = query.list();
        return notes;
    }

    @Logger(message = "获取指定用户的笔记本总数量")
    public int getNoteNum(String userId) {
        String hql = "select count(1) from cn_note c where c.uId=:userId";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        BigInteger bInt = (BigInteger) query.uniqueResult();
        return bInt.intValue();
    }

    @Logger(message = "获取指定用户的饼图统计信息")
    public List<KeyValue> getPieData(String userId) {
        String hql = "select c.name, b.value from (select a.nId, count(1) as value from cn_article a where a.uId=:userId group by a.nId) b left join cn_note c on b.nId=c.id";
        Session session = this.getSession();
        SQLQuery query = session.createSQLQuery(hql);
        query.setString("userId", userId);
        List list = query.list();
        List<KeyValue> keyValues = Lists.newArrayListWithCapacity(list.size());
        for (Object obj : list) {
            Object[] arr = (Object[]) obj;
            if(arr.length == 2 && arr[0] != null && arr[1] != null) {
                KeyValue keyValue = new KeyValue();
                keyValue.setName(arr[0].toString());
                keyValue.setValue(arr[1].toString());
                keyValues.add(keyValue);
            }
        }
        return keyValues;
    }
}