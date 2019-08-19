package com.sxkl.cloudnote.disk.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.AbstractBaseDao;
import com.sxkl.cloudnote.disk.entity.FileInfo;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午4:57:04
 */
@Repository
public class FileInfoDao extends AbstractBaseDao {

    public void save(FileInfo fileInfo) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(fileInfo);
    }

    public void delete(FileInfo fileInfo) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(fileInfo);
    }

    public void update(FileInfo fileInfo) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.update(fileInfo);
    }

    public FileInfo fetch(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return session.load(FileInfo.class, id);
    }
}
