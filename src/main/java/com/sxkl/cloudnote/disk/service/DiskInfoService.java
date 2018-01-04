package com.sxkl.cloudnote.disk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.disk.dao.DiskInfoDao;
import com.sxkl.cloudnote.disk.entity.DiskInfo;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午5:04:25
 */
@Service
public class DiskInfoService {
	
	@Autowired
	private DiskInfoDao diskInfoDao;
	
	public void save(DiskInfo diskInfo){
		diskInfoDao.save(diskInfo);
	}
	
	public void delete(String id){
		diskInfoDao.delete(diskInfoDao.fetch(id));
	}
	
	public void delete(DiskInfo diskInfo){
		diskInfoDao.delete(diskInfo);
	}
	
	public void update(DiskInfo diskInfo){
		diskInfoDao.update(diskInfo);
	}
	
	public DiskInfo fetch(String id){
		return diskInfoDao.fetch(id);
	}
}
