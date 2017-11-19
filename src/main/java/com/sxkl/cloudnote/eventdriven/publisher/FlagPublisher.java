package com.sxkl.cloudnote.eventdriven.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.eventdriven.entity.FlagPublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.FlagPublisherEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FlagPublisher {

	@Autowired
	private ApplicationContext applicationContext;
	
	public void cacheAddArticleTreeMenu(String userId){
		FlagPublisherBean bean = new FlagPublisherBean();
		bean.setUserId(userId);
        applicationContext.publishEvent(new FlagPublisherEvent(bean));
        log.info("FlagPublisher--cacheAddArticleTreeMenu");
        
    }

}
