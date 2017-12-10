package com.sxkl.cloudnote.eventdriven.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherEvent;
import com.sxkl.cloudnote.eventdriven.entity.DutyType;
import com.sxkl.cloudnote.log.annotation.Logger;

@Service
public class ArticlePublisher {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Logger(message="建立笔记和图片的关系")
	public void establishLinkagesBetweenArticleAndImage(Article article){
		ArticlePublisherBean bean = new ArticlePublisherBean();
		bean.setArticleId(article.getId());
		bean.setArticleContent(article.getContent());
		bean.setDomain(Constant.DOMAIN);
		bean.setDutype(DutyType.LINK_ARTICLE_IMAGE);
        applicationContext.publishEvent(new ArticlePublisherEvent(bean));
        
    }

	@Logger(message="增加笔记点击量")
	public void increaseArticleHitNum(String id) {
		ArticlePublisherBean bean = new ArticlePublisherBean();
		bean.setArticleId(id);
		bean.setDutype(DutyType.INCREASE_HITNUM);
        applicationContext.publishEvent(new ArticlePublisherEvent(bean));
	}

}
