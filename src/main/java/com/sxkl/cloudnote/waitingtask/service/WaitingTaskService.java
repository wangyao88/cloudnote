package com.sxkl.cloudnote.waitingtask.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.StringAppendUtils;
import com.sxkl.cloudnote.waitingtask.dao.WaitingTaskDao;
import com.sxkl.cloudnote.waitingtask.entity.WaitingTask;
import com.sxkl.cloudnote.webchat.websocket.MyWebSocketHandler;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class WaitingTaskService {

    @Autowired
    private WaitingTaskDao waitingTaskDao;
    @Autowired
    private MyWebSocketHandler myWebSocketHandler;
    @Autowired
    private UserService userService;

    @Logger(message = "保存待办任务")
    public void insert(WaitingTask waitingTask) {
        waitingTaskDao.save(waitingTask);
    }

    @Logger(message = "更新待办任务")
    public void update(WaitingTask waitingTask) {
        waitingTaskDao.update(waitingTask);
    }

    @Logger(message = "查询待办任务")
    public WaitingTask find(String id) {
        return waitingTaskDao.findOne(id);
    }

    @Logger(message = "删除待办任务")
    public void delete(String id) {
        WaitingTask waitingTask = find(id);
        waitingTaskDao.delete(waitingTask);
    }

    @Logger(message = "分页查询待办任务")
    public String findPage(int pageIndex, int pageSize, String userId) {
        List<WaitingTask> waitingTasks = waitingTaskDao.findPage(pageIndex, pageSize, userId);
        int total = waitingTaskDao.findPageCount(userId);
        return OperateResultService.configurateSuccessDataGridResult(waitingTasks, total);
    }

    @Logger(message = "过滤前台传递的WaitingTask实体的Json字符串")
    public String filterWaitingTaskStr(String waitingTaskStr) {
        while (waitingTaskStr.lastIndexOf("Date") > -1) {
            int index = waitingTaskStr.lastIndexOf("Date");
            waitingTaskStr = StringAppendUtils.append(waitingTaskStr.substring(0, index), "DATE", waitingTaskStr.substring(index + 4, waitingTaskStr.length()));
            waitingTaskStr = StringAppendUtils.append(waitingTaskStr.substring(0, index + 17), " ", waitingTaskStr.substring(index + 18, waitingTaskStr.length()));
        }
        waitingTaskStr = waitingTaskStr.replaceAll("DATE", "Date");
        return waitingTaskStr;
    }

    @Logger(message = "给前台推送待办任务数量")
    public void sendToWaitingTask() {
        JSONObject json = null;
        Date date = DateUtils.getBeforeSevenDay();
        List<User> users = userService.getAllUsers();
        try {
            for (User user : users) {
                int count = waitingTaskDao.findAllUnDoCount(user.getId());
                double rate = calculateRate(date, user);
                json = new JSONObject();
                json.put("count", count);
                json.put("rate", rate);
                String sessionKey = StringAppendUtils.append(Constant.WAITING_TASK_PREFIX_FOR_WEBSOCKET, user.getId());
                myWebSocketHandler.sendMessageToUser(sessionKey, new TextMessage(json.toString().getBytes()));
            }
        } catch (Exception e) {
            log.error("给前台推送待办任务数量失败！错误信息：{}", e.getMessage());
        }
    }

    private double calculateRate(Date date, User user) {
        List<WaitingTask> waitingTasks = waitingTaskDao.findWeek(user.getId(), date);
        double sum = 0, rate = 0;
        if (!waitingTasks.isEmpty()) {
            for (WaitingTask waitingTask : waitingTasks) {
                sum += waitingTask.getProcess();
            }
            rate = sum / waitingTasks.size();
        }
        return rate;
    }
}
