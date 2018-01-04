package com.sxkl.cloudnote.disk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.disk.dao.FileInfoDao;
import com.sxkl.cloudnote.disk.entity.FileInfo;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午5:04:25
 */
@Service
public class FileInfoService {
	
	@Autowired
	private FileInfoDao fileInfoDao;
	
	public void save(FileInfo fileInfo){
		fileInfoDao.save(fileInfo);
	}
	
	public void delete(String id){
		fileInfoDao.delete(fileInfoDao.fetch(id));
	}
	
	public void delete(FileInfo fileInfo){
		fileInfoDao.delete(fileInfo);
	}
	
	public void update(FileInfo fileInfo){
		fileInfoDao.update(fileInfo);
	}
	
	public FileInfo fetch(String id){
		return fileInfoDao.fetch(id);
	}
	
	public List<FileInfo> findAllSubFiles(String id){
		FileInfo fileInfo = fetch(id);
		List<FileInfo> fileInfos = Lists.newArrayList(fileInfo.getChildren());
		//TODO sort
		return fileInfos;
	}
}
