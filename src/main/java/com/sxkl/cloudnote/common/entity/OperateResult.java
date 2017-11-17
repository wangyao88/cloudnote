package com.sxkl.cloudnote.common.entity;

import lombok.Data;

@Data
public class OperateResult {
	
	private String msg;
	private boolean status;
	private Object data;
	private Integer total;

}
