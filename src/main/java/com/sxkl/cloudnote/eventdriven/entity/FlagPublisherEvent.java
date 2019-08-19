package com.sxkl.cloudnote.eventdriven.entity;

import org.springframework.context.ApplicationEvent;

public class FlagPublisherEvent extends ApplicationEvent {

    private static final long serialVersionUID = -6962020321160964101L;

    public FlagPublisherEvent(FlagPublisherBean bean) {
        super(bean);
    }

}
