package com.sxkl.cloudnote.log.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Log {
	
	private LogLevel logLevel;
	private String className;
	private String methodName;
	private String message;
	private String errorMsg;
	private Date date;
	private long costTime;
	private String prettyTime;
	private String ip;
	private String userId;
	private String userName;

}
