package com.sxkl.cloudnote.waitingtask.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.waitingtask.dao.WaitingTaskDao;
import com.sxkl.cloudnote.waitingtask.entity.WaitingTask;

@Service
public class WaitingTaskService {
	
	@Autowired
	private WaitingTaskDao waitingTaskDao;

	@Logger(message="保存待办任务")
	public void insert(WaitingTask waitingTask){
		waitingTaskDao.insert(waitingTask);
	}
	
	@Logger(message="更新待办任务")
	public void update(WaitingTask waitingTask){
		waitingTaskDao.update(waitingTask);
	}
	
	@Logger(message="查询待办任务")
	public WaitingTask find(String id){
		return waitingTaskDao.find(id);
	}
	
	@Logger(message="删除待办任务")
	public void delete(String id){
		WaitingTask waitingTask = find(id);
		waitingTaskDao.delete(waitingTask);
	}
	
	@Logger(message="分页查询待办任务")
	public String findPage(int pageIndex, int pageSize,String userId){
		List<WaitingTask> waitingTasks = waitingTaskDao.findPage(pageIndex,pageSize,userId);
		int total = waitingTaskDao.findPageCount(userId);
		return OperateResultService.configurateSuccessDataGridResult(waitingTasks,total);
	}
}
