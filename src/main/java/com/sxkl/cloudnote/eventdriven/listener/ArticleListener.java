package com.sxkl.cloudnote.eventdriven.listener;

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
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.IndexManager;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherEvent;
import com.sxkl.cloudnote.image.service.ImageService;
import com.sxkl.cloudnote.log.annotation.Logger;

@Service
public class ArticleListener implements ApplicationListener<ApplicationEvent>{
	
	@Autowired
	private ImageService imageService;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private IndexManager indexManager;

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
	        case UPDATE_INDEX_BY_ADD_OPERATION:
	        	indexManager.updateIndexByAdd(convertArticle(article),article.getUserId());
				break;
	        case UPDATE_INDEX_BY_UPDATE_OPERATION:
	        	indexManager.updateIndexByUpdate(convertArticle(article),article.getUserId());
				break;
	        case UPDATE_INDEX_BY_DELETE_OPERATION:
	        	indexManager.updateIndexByDelete(convertArticle(article),article.getUserId());
				break;
		}
	}
	
	@Logger(message="增加笔记点击量")
	public void increaseHitNum(ArticlePublisherBean bean) {
		articleDao.increaseHitNum(bean.getArticleId());
	}

	@Logger(message="建立笔记和图片的关系")
	public void linkArticleImage(ArticlePublisherBean article) {
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
	}

	private boolean isNotDuty(ApplicationEvent event){
		return !event.getClass().toString().equals(ArticlePublisherEvent.class.toString());
	}
	
	private Article convertArticle(ArticlePublisherBean articlePublisherBean){
		Article article = new Article();
		article.setId(articlePublisherBean.getArticleId());
		article.setTitle(articlePublisherBean.getArticleTitle());
		article.setContent(articlePublisherBean.getArticleContent());
		article.setHitNum(articlePublisherBean.getHitNum());
		return article;
	}
	
	public void cacheAddArticleTreeMenu(HttpServletRequest request) {
		
	}

}
