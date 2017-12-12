package com.sxkl.cloudnote.waitingtask.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.user.entity.User;


@Entity
@Table(name="cn_waitingtask")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class WaitingTask implements Serializable{

	private static final long serialVersionUID = 7835646751798477826L;

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="name",nullable=false)
	private String name;
	
	@Column(name="createDate",nullable=false)
	private Date createDate;
	
	@Column(name="beginDate",nullable=false)
	private Date beginDate;
	
	@Column(name="expireDate",nullable=false)
	private Date expireDate;
	
	@Column(name="process",nullable=false)
	private double process;
	
	@Column(name="content",nullable=false)
	private String content;
	
	@Column(name="taskType",nullable=false)
	private TaskType taskType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uId")
	private User user;
	

	public WaitingTask() {
		super();
	}

	public WaitingTask(String id, String name, Date createDate, Date beginDate, Date expireDate, double process, String content, TaskType taskType) {
		this.id = id;
		this.name = name;
		this.createDate = createDate;
		this.beginDate = beginDate;
		this.expireDate = expireDate;
		this.process = process;
		this.content = content;
		this.taskType = taskType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public double getProcess() {
		return process;
	}

	public void setProcess(double process) {
		this.process = process;
	}

	public String getTaskType() {
		return taskType.toString();
	}

	public void setTaskType(String taskType) {
		this.taskType = TaskType.valueOf(taskType);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
