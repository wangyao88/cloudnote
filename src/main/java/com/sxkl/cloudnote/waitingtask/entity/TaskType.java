package com.sxkl.cloudnote.waitingtask.entity;

public enum TaskType {

    WORK("工作"), STUDY("学习"), LIFE("生活");

    private String content;

    private TaskType(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
