package com.sxkl.cloudnote.waitingtask.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;
import com.sxkl.cloudnote.waitingtask.entity.WaitingTask;
import com.sxkl.cloudnote.waitingtask.service.WaitingTaskService;

@RestController
@RequestMapping("/waitingtask")
public class WaitingTaskController {
	
	@Autowired
	private WaitingTaskService waitingTaskService;
	
	@RequestMapping("/insert")
	public void insert(String waitingTaskStr,HttpServletRequest request){
		waitingTaskStr = waitingTaskService.filterWaitingTaskStr(waitingTaskStr);
		User user = UserUtil.getSessionUser(request);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	    WaitingTask waitingTask = gson.fromJson(waitingTaskStr, WaitingTask.class);
	    waitingTask.setUser(user);
	    waitingTask.setCreateDate(new Date());
		waitingTaskService.insert(waitingTask);
	}
	
	@RequestMapping("/update")
	public void update(String waitingTaskStr,HttpServletRequest request){
		waitingTaskStr = waitingTaskService.filterWaitingTaskStr(waitingTaskStr);
		User user = UserUtil.getSessionUser(request);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	    WaitingTask waitingTask = gson.fromJson(waitingTaskStr, WaitingTask.class);
	    waitingTask.setUser(user);
		waitingTaskService.update(waitingTask);
	}
	
	@RequestMapping("/delete")
	public void delete(String id){
		waitingTaskService.delete(id);
	}
	
	@RequestMapping("/findPage")
	public String findPage(int pageIndex, int pageSize,HttpServletRequest request){
		User user = UserUtil.getSessionUser(request);
		return waitingTaskService.findPage(pageIndex, pageSize, user.getId());
	}

}
