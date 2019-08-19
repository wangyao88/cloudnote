package com.sxkl.cloudnote.eventdriven.entity;

import org.springframework.context.ApplicationEvent;

public class ArticlePublisherEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2564301610383846975L;

    public ArticlePublisherEvent(ArticlePublisherBean bean) {
        super(bean);
    }

}
