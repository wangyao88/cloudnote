package com.sxkl.cloudnote.disk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.disk.dao.FileInfoDao;
import com.sxkl.cloudnote.disk.entity.FileInfo;
import com.sxkl.cloudnote.log.annotation.Logger;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午5:04:25
 */
@Service
public class FileInfoService {
	
	@Autowired
	private FileInfoDao fileInfoDao;
	
	@Logger(message="保存云盘上传文件")
	public void save(FileInfo fileInfo){
		fileInfoDao.save(fileInfo);
	}
	
	@Logger(message="删除云盘文件")
	public void delete(String id){
		fileInfoDao.delete(fileInfoDao.fetch(id));
	}
	
	@Logger(message="删除云盘文件")
	public void delete(FileInfo fileInfo){
		fileInfoDao.delete(fileInfo);
	}
	
	@Logger(message="更新云盘文件")
	public void update(FileInfo fileInfo){
		fileInfoDao.update(fileInfo);
	}
	
	@Logger(message="获取云盘文件")
	public FileInfo fetch(String id){
		return fileInfoDao.fetch(id);
	}
	
	@Logger(message="获取云盘文件夹下所有文件")
	public List<FileInfo> findAllSubFiles(String id){
		FileInfo fileInfo = fetch(id);
		List<FileInfo> fileInfos = Lists.newArrayList(fileInfo.getChildren());
		//TODO sort
		return fileInfos;
	}
}
