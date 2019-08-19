package com.sxkl.cloudnote.eventdriven.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.eventdriven.entity.FlagPublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.FlagPublisherEvent;
import com.sxkl.cloudnote.log.annotation.Logger;

@Service
public class FlagPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    @Logger(message = "缓存增加笔记时的标签树")
    public void cacheAddArticleTreeMenu(String userId) {
        FlagPublisherBean bean = new FlagPublisherBean();
        bean.setUserId(userId);
        applicationContext.publishEvent(new FlagPublisherEvent(bean));

    }

}
