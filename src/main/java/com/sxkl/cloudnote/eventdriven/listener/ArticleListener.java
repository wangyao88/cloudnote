package com.sxkl.cloudnote.eventdriven.listener;

import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherEvent;
import com.sxkl.cloudnote.image.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleListener implements ApplicationListener<ApplicationEvent>{
	
	@Autowired
	private ImageService imageService;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(isNotDuty(event)){
			log.info("ArticleListener:\"不归我管！\""+event.toString());
			return;
		}
		ArticlePublisherBean article = (ArticlePublisherBean) event.getSource();
		Document doc = Jsoup.parse(article.getArticleContent());
		Elements imgs = doc.getElementsByTag("img");
		String regex = "^(http|https|ftp)+://.*$";
		for (int i = 0; i < imgs.size(); i++) {
			String imgUrl = imgs.get(i).attr("src");
			//http://127.0.0.1:8888/cloudnote/image/getImage?name=ac9cd356-c5c5-49f6-88df-c46962bfc28c
			if ((Pattern.matches(regex, imgUrl)) && (imgUrl.startsWith(article.getDomain()))) {
				String imageName = imgUrl.substring(imgUrl.lastIndexOf("=")+1);
				imageService.establishLinkagesBetweenArticleAndImage(article.getArticleId(),imageName);
			}
		}
		log.info("ArticleListener:\"处理完成！\"");
	}
	
	private boolean isNotDuty(ApplicationEvent event){
		return !event.getClass().toString().equals(ArticlePublisherEvent.class.toString());
	}

	public static void main(String[] args) {
		String str = "http://127.0.0.1:8888/cloudnote/image/getImage?name=ac9cd356-c5c5-49f6-88df-c46962bfc28c";
		System.out.println(str.substring(str.lastIndexOf("=")+1));
	}
}
