package com.sxkl.cloudnote.eventdriven.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.common.service.DomainService;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticlePublisher {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private DomainService domainService;
	
	public void establishLinkagesBetweenArticleAndImage(Article article){
		ArticlePublisherBean bean = new ArticlePublisherBean();
		bean.setArticleId(article.getId());
		bean.setArticleContent(article.getContent());
		bean.setDomain(domainService.getDomain());
        applicationContext.publishEvent(new ArticlePublisherEvent(bean));
        log.info("ArticlePublisher--establishLinkagesBetweenArticleAndImage--["+article.getTitle()+"]");
        
  }

}
