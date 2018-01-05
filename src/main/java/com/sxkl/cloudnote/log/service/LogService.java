package com.sxkl.cloudnote.log.service;

import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.log.entity.Log;
import com.sxkl.cloudnote.log.entity.LogLevel;
import com.sxkl.cloudnote.utils.DateUtils;

@Service
public class LogService {
	
	public void showLogInConsole(Log logger){
		StringBuilder consoleLog = new StringBuilder();
		consoleLog.append(DateUtils.formatDate2Str(logger.getDate()))
				  .append(" ")
				  .append(logger.getLogLevel().toString())
				  .append(" ")
				  .append(logger.getClassName())
				  .append(" ")
				  .append(logger.getMethodName())
				  .append(" ")
				  .append(logger.getMessage())
				  .append(" ");
		 if(LogLevel.ERROR.equals(logger.getLogLevel())){
			 consoleLog.append(logger.getErrorMsg())
			           .append(" ");
		 }
		 consoleLog.append(logger.getCostTime())
				   .append(" ")
				   .append(logger.getUserName())
				   .append(" ")
				   .append(logger.getIp());
//		System.out.println(consoleLog.toString());
	}
}
