package com.sxkl.cloudnote.log.service;

import com.google.common.base.Charsets;
import com.mohan.project.easylogger.common.LoggerLevelEnum;
import com.mohan.project.easylogger.core.EasyLogger;
import com.mohan.project.easylogger.core.Logger;
import com.sxkl.cloudnote.log.dao.LogDao;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.statistic.model.DateRange;
import com.sxkl.cloudnote.statistic.model.LogData;
import com.sxkl.cloudnote.utils.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogService {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private LogDao logDao;

    public void showLogInConsole(Log log) {
        try {
            EasyLogger.log(adapte(log));
            String routeKey = getRouteKey(log.getLevel());
            JSONObject json = JSONObject.fromObject(log);
            Message message = new Message(json.toString().getBytes(Charsets.UTF_8), new MessageProperties());
            sendQueue("log_exchange", routeKey, message);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Logger adapte(Log log) {
        return Logger.builder()
                     .dateTime(LocalDateTime.now())
                     .level(getLevel(log.getLogLevel().toString()))
                     .threadName(Thread.currentThread().getName())
                     .userName(log.getUserName())
                     .ip(log.getIp())
                     .className(log.getClassName())
                     .methodName(log.getMethodName())
                     .message(log.getMessage())
                     .costTime(log.getCostTime())
                     .throwable(log.getThrowable())
                     .build();
    }

    private LoggerLevelEnum getLevel(String level) {
        return LoggerLevelEnum.valueOf(level);
    }

    private String getRouteKey(String level) {
        if (StringUtils.isEmpty(level)) {
            return "other_log_queue";
        }
        return StringUtils.appendJoinEmpty(level.toLowerCase(), "_log_queue");
    }

    private void sendQueue(String exchange_key, String route_key, Object object) {
        amqpTemplate.convertAndSend(exchange_key, route_key, object);
    }

    public int getLogNum(String userId) {
        return logDao.getLogNum(userId);
    }

    public Map<String, String> getBarPercentData(String userId, DateRange dateRange) {
        return logDao.getBarPercentData(userId, dateRange);
    }

    public List<LogData> getLogTableData(int pageIndex, int pageSize, String userId) {
        List<Log> logs = logDao.getLogs(pageIndex, pageSize, userId);
        List<LogData> logDatas = logs.stream().map(Log::convertToLogTableData).collect(Collectors.toList());
        int index = (pageIndex-1) * pageSize + 1;
        for (LogData logData : logDatas) {
            logData.setIndex(index);
            index++;
        }
        return logDatas;
    }
}