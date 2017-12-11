package com.sxkl.cloudnote.waitingtask.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public void insert(WaitingTask waitingTask){
		waitingTaskService.insert(waitingTask);
	}
	
	@RequestMapping("/update")
	public void update(WaitingTask waitingTask){
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
