package com.sxkl.cloudnote.webchat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name="cn_message")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Message {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	//发送者
	@Column(name="fromId",unique=false,nullable=false)
	public String from;
	
	//发送者名称
	@Column(name="fromName",unique=false,nullable=false)
	public String fromName;
	
	//接收者
	@Column(name="toId",unique=false,nullable=false)
	public String to;
	
	//发送的文本
	@Column(name="text",unique=false,nullable=false)
	public String text;
	
	//发送日期
	@Column(name="date",unique=false,nullable=false)
	public Date date;

}
