package com.sxkl.cloudnote.article.entity;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.function.Function;

@Data
public class KeyValue {

    private String key;
    private String value;

    public KeyValue(String key, String value, Function<String, String> handler) {
        this.key = key;
        if(handler == null) {
            Document doc = Jsoup.parse(value);
            this.value = doc.text();
            return;
        }
        this.value = handler.apply(value);
    }
}
