package com.sxkl.cloudnote.log.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
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
	
	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		setLevel(logLevel.name());
	}
}
