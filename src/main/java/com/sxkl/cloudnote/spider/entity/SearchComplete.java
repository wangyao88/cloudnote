package com.sxkl.cloudnote.spider.entity;

import lombok.Data;

@Data
public class SearchComplete {

    private String query;
    private String[] suggestions;
    private String[] data;

    public SearchComplete(int size) {
        suggestions = new String[size];
        data = new String[size];
    }
}
