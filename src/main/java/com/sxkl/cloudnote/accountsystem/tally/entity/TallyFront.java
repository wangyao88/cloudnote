package com.sxkl.cloudnote.accountsystem.tally.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author: wangyao
 * @date: 2018年5月10日 下午2:25:50
 * @description: 
 */
@Data
public class TallyFront {

	private String id;
	private float money;
	private String mark;
	private Date createDate;
	private String category;
	private String accountBook;
	private String type;
}
