package com.sxkl.cloudnote.log.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.log.entity.LogLevel;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.StringUtils;

import net.sf.json.JSONObject;

@Service
public class LogService{
	
	@Autowired
	private AmqpTemplate amqpTemplate;

	public void showLogInConsole(Log log){
		StringBuilder consoleLog = new StringBuilder();
		consoleLog.append(DateUtils.formatDate2Str(log.getDate()))
				  .append(" ")
				  .append(log.getLogLevel().toString())
				  .append(" ")
				  .append(log.getClassName())
				  .append(" ")
				  .append(log.getMethodName())
				  .append(" ")
				  .append(log.getMessage())
				  .append(" ");
		 if(LogLevel.ERROR.equals(log.getLogLevel())){
			 consoleLog.append(log.getErrorMsg())
			           .append(" ");
		 }
		 consoleLog.append(log.getCostTime())
				   .append(" ")
				   .append(log.getUserName())
				   .append(" ")
				   .append(log.getIp());
		System.out.println(consoleLog.toString());
//		save(logger);
		String routeKey = getRouteKey(log.getLevel());
		JSONObject json = JSONObject.fromObject(log);
		Message message = new Message(json.toString().getBytes(Charsets.UTF_8),new MessageProperties());
		sendQueue("log_exchange", routeKey, message);
	}
	
	private String getRouteKey(String level) {
		if(StringUtils.isEmpty(level)){
			return "other_log_queue";
		}
		return StringUtils.appendJoinEmpty(level.toLowerCase(),"_log_queue");
	}

	private void sendQueue(String exchange_key, String route_key, Object object) {
		amqpTemplate.convertAndSend(exchange_key, route_key, object);
	}
}
