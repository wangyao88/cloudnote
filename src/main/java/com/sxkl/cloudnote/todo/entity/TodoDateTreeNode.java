package com.sxkl.cloudnote.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoDateTreeNode {

    public static final String ROOT_NODE_ID = "0";
    public static final String ROOT_NODE_TEXT = "日期";
    public static final String ROOT_NODE_PID = "-1";

    private String id;
    private String text;
    private String parent;
    private boolean isleaf = false;
	private boolean expand = false;

	public static TodoDateTreeNode getRootNode() {
	    return new TodoDateTreeNode(ROOT_NODE_ID, ROOT_NODE_TEXT, ROOT_NODE_PID, false, true);
    }

    public static TodoDateTreeNode getYearNode(int year) {
        return new TodoDateTreeNode(String.valueOf(year), year+"年", ROOT_NODE_ID, false, false);
    }

    public static TodoDateTreeNode getMonthNode(int year, int month) {
        return new TodoDateTreeNode(String.valueOf(year) + String.valueOf(month), month+"月", String.valueOf(year), true, false);
    }
}