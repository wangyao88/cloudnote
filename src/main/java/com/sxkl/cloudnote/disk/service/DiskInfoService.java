package com.sxkl.cloudnote.disk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.disk.dao.DiskInfoDao;
import com.sxkl.cloudnote.disk.entity.DiskInfo;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.main.entity.TreeNode;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午5:04:25
 */
@Service
public class DiskInfoService {
	
	@Autowired
	private DiskInfoDao diskInfoDao;
	
	@Logger(message="保存云盘")
	public void save(DiskInfo diskInfo){
		diskInfoDao.save(diskInfo);
	}
	
	@Logger(message="删除云盘")
	public void delete(String id){
		diskInfoDao.delete(diskInfoDao.fetch(id));
	}
	
	@Logger(message="删除云盘")
	public void delete(DiskInfo diskInfo){
		diskInfoDao.delete(diskInfo);
	}
	
	@Logger(message="更新云盘")
	public void update(DiskInfo diskInfo){
		diskInfoDao.update(diskInfo);
	}
	
	@Logger(message="获取云盘")
	public DiskInfo fetch(String id){
		return diskInfoDao.fetch(id);
	}

	public List<TreeNode> getTree() {
		
		return null;
	}
}
