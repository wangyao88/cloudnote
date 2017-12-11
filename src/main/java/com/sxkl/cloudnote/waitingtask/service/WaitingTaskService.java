package com.sxkl.cloudnote.waitingtask.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.waitingtask.dao.WaitingTaskDao;
import com.sxkl.cloudnote.waitingtask.entity.WaitingTask;

@Service
public class WaitingTaskService {
	
	@Autowired
	private WaitingTaskDao waitingTaskDao;

	public void insert(WaitingTask waitingTask){
		waitingTaskDao.insert(waitingTask);
	}
	
	public void update(WaitingTask waitingTask){
		waitingTaskDao.update(waitingTask);
	}
	
	public WaitingTask find(String id){
		return waitingTaskDao.find(id);
	}
	
	public void delete(String id){
		WaitingTask waitingTask = find(id);
		waitingTaskDao.delete(waitingTask);
	}
	
	public String findPage(int pageIndex, int pageSize,String userId){
		List<WaitingTask> waitingTasks = waitingTaskDao.findPage(pageIndex,pageSize,userId);
		int total = waitingTaskDao.findPageCount(userId);
		return OperateResultService.configurateSuccessDataGridResult(waitingTasks,total);
	}
}
