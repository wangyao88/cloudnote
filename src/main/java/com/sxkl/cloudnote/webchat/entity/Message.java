package com.sxkl.cloudnote.webchat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.sxkl.cloudnote.utils.DateUtils;

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
	private String from;
	
	//发送者名称
	@Column(name="fromName",unique=false,nullable=false)
	private String fromName;
	
	//接收者
	@Column(name="toId",unique=false,nullable=false)
	private String to;
	
	//发送的文本
	@Column(name="text",unique=false,nullable=false)
	private String text;
	
	//发送日期
	@DateTimeFormat(pattern=DateUtils.DATE_TIME_PATTON_1)
	@Column(name="date",unique=false,nullable=false)
	private Date date;
	
	@Transient
	private String dateStr;
	
	@Transient
	private MsgType msgType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDateStr() {
		return DateUtils.formatDate2Str(date);
	}

	public void setDateStr(String dateStr) {
		this.dateStr = DateUtils.formatDate2Str(date);
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}
}
