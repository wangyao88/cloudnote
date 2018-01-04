package com.sxkl.cloudnote.disk.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.disk.entity.DiskInfo;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午4:57:04
 */
@Repository
public class DiskInfoDao extends BaseDao{

	public void save(DiskInfo diskInfo){
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(diskInfo);
	}
	
	public void delete(DiskInfo diskInfo){
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(diskInfo);
	}
	
	public void update(DiskInfo diskInfo){
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(diskInfo);
	}
	
	public DiskInfo fetch(String id){
		Session session = this.getSessionFactory().getCurrentSession();
		return session.load(DiskInfo.class, id);
	}
}
