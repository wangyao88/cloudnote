package com.sxkl.cloudnote.main.entity;

import lombok.Data;

@Data
public class TreeNode {
	
	private String id;
	private String text;
	private String pid;
	private boolean isleaf;
	private String imgPath;
	private String iconCls;
//	private boolean expandOnLoad = true;
}
