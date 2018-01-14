package com.sxkl.cloudnote.log.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="cn_log")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Log {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Transient
	private LogLevel logLevel;
	
	@Column(name="level",unique=false,nullable=false)
	private String level;
	
	@Column(name="className",unique=false,nullable=false)
	private String className;
	
	@Column(name="methodName",unique=false,nullable=false)
	private String methodName;
	
	@Column(name="message",unique=false,nullable=false)
	private String message;
	
	@Column(name="errorMsg",unique=false,nullable=true)
	private String errorMsg;
	
	@Column(name="date",unique=false,nullable=false)
	private Date date;
	
	@Column(name="costTime",unique=false,nullable=false)
	private int costTime;
	
	@Column(name="prettyTime",unique=false,nullable=true)
	private String prettyTime;
	
	@Column(name="ip",unique=false,nullable=true)
	private String ip;
	
	@Column(name="userId",unique=false,nullable=true)
	private String userId;
	
	@Column(name="userName",unique=false,nullable=true)
	private String userName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LogLevel getLogLevel() {
		return logLevel;
	}
	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		setLevel(logLevel.name());
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getCostTime() {
		return costTime;
	}
	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}
	public String getPrettyTime() {
		return prettyTime;
	}
	public void setPrettyTime(String prettyTime) {
		this.prettyTime = prettyTime;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
