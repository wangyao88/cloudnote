package com.sxkl.cloudnote.schedule.waitingtask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.waitingtask.service.WaitingTaskService;

@Service
public class WaitingTaskSchedule {

	@Autowired
    private WaitingTaskService waitingTaskService;
	
	@Logger(message="定时更新页面待办任务状态，推送消息")
	@Scheduled(cron="0 * * */1 * ?")
	public void sendToWaitingTask(){
		waitingTaskService.sendToWaitingTask();
	}
}
