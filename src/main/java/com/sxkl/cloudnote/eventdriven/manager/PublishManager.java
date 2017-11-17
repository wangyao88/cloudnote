package com.sxkl.cloudnote.eventdriven.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.eventdriven.publisher.ArticlePublisher;
import com.sxkl.cloudnote.utils.SpringContextUtil;

import lombok.Data;

@Data
@Service
public class PublishManager {
	
	@Autowired
	private ArticlePublisher articlePublisher;
	
	public static PublishManager getPublishManager(){
		try {
			return (PublishManager) SpringContextUtil.getBean("publishManager");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
