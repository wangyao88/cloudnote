package com.sxkl.cloudnote.webchat.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Message {

	//发送者
	public String from;
	//发送者名称
	public String fromName;
	//接收者
	public String to;
	//发送的文本
	public String text;
	//发送日期
	public Date date;

}
