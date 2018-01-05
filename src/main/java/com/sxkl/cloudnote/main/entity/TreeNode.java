package com.sxkl.cloudnote.main.entity;

import lombok.Data;

@Data
public class TreeNode {
	
	private String id;
	private String text;
	private String pid;
	private boolean isleaf;
	private String iconCls;
	private String imgPath;
//	private boolean expandOnLoad = true;
}
