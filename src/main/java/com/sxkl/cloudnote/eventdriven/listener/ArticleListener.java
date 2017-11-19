package com.sxkl.cloudnote.eventdriven.listener;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherEvent;
import com.sxkl.cloudnote.image.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleListener implements ApplicationListener<ApplicationEvent>{
	
	@Autowired
	private ImageService imageService;
	@Autowired
	private ArticleDao articleDao;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(isNotDuty(event)){
			return;
		}
		ArticlePublisherBean article = (ArticlePublisherBean) event.getSource();
		switch (article.getDutype()) {
		case LINK_ARTICLE_IMAGE:
			linkArticleImage(article);
			break;
        case INCREASE_HITNUM:
			increaseHitNum(article);
			break;
		}
	}
	
	private void increaseHitNum(ArticlePublisherBean bean) {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		articleDao.increaseHitNum(bean.getArticleId());
		log.info("ArticleListener:[increaseHitNum]处理完成！");
	}

	private void linkArticleImage(ArticlePublisherBean article) {
		Document doc = Jsoup.parse(article.getArticleContent());
		Elements imgs = doc.getElementsByTag("img");
		String regex = "^(http|https|ftp)+://.*$";
		for (int i = 0; i < imgs.size(); i++) {
			String imgUrl = imgs.get(i).attr("src");
			if ((Pattern.matches(regex, imgUrl)) && (imgUrl.startsWith(article.getDomain()))) {
				String imageName = imgUrl.substring(imgUrl.lastIndexOf("=")+1);
				imageService.establishLinkagesBetweenArticleAndImage(article.getArticleId(),imageName);
			}
		}
		log.info("ArticleListener:[linkArticleImage]处理完成！");
	}

	private boolean isNotDuty(ApplicationEvent event){
		return !event.getClass().toString().equals(ArticlePublisherEvent.class.toString());
	}
	
	public void cacheAddArticleTreeMenu(HttpServletRequest request) {
		
	}

}
