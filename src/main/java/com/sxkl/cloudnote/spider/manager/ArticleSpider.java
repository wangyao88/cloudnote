package com.sxkl.cloudnote.spider.manager;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.spider.entity.NetArticle;
import com.sxkl.cloudnote.utils.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleSpider {
	
	@Autowired
	private UrlFactory urlFactory;
	@Autowired
	private TitleFilter titleFilter;
	@Autowired
	private ContentFilter contentFilter;
	
	@Logger(message="爬取CSDN文章")
	public List<NetArticle> spider() throws IOException{
		List<NetArticle> articles = Lists.newArrayList();
		Map<String, String> cookies = getCookies();
		Document document = Jsoup.connect(urlFactory.getUrl()).cookies(cookies).get();
		Elements elements = document.getElementsByClass("list_con");
		for(Element element : elements){
			NetArticle article = new NetArticle();
			String titleStr = element.getElementsByTag("h2").html();
			if(!titleFilter.validateTitle(titleStr)){
				continue;
			}
			Document titleDoc = Jsoup.parse(titleStr);
			Elements titleEle = titleDoc.getElementsByTag("a");
			String href = titleEle.get(0).attr("href");
			String title = titleEle.get(0).html();
			if(!titleFilter.validateTitle(title)){
				continue;
			}
			title = titleFilter.subTitle(title);
			String content = "";
			try {
				content = Jsoup.connect(href).get().html();
				content = contentFilter.filter(content);
			} catch (Exception e) {
				continue;
			}
			String time = element.getElementsByClass("time").html();
			article.setId(UUIDUtil.getUUID());
			article.setTitle(title);
			article.setUrl(href);
			article.setContent(content);
			article.setDate(time);
			String tagStr = element.getElementsByClass("tag").html();
			if(!StringUtils.isEmpty(tagStr)){
				Document tagDoc = Jsoup.parse(tagStr);
				Elements tagEle = tagDoc.getElementsByTag("a");
				String tag = tagEle.get(0).html();
				article.setCategory(tag);
			}
			articles.add(article);
		}
		log.info("爬取了{}篇文章",articles.size());
		return articles;
	}
	
	private static Map<String, String> getCookies() {
		String str = "uuid_tt_dd=10_16972385510-1513519269797-401969; __message_sys_msg_id=0; __message_gu_msg_id=0; __message_cnel_msg_id=0; __message_in_school=0; TY_SESSION_ID=d74284b0-1be7-4d41-9a30-b144c953b4da; uuid=a82114e4-8754-4681-bdc0-d9a0e6d43f1e; ADHOC_MEMBERSHIP_CLIENT_ID1.0=bb04ca10-d9d5-7771-07d3-ecf9e7670720; dc_tos=p1gb12; dc_session_id=10_1514038447015.330218; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1513519270,1513519362,1514038449; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1514094806";
		String[] strs = str.split(";");
		Map<String,String> cookies = new HashMap<String,String>();
		for(String cookie : strs){
			String[] temp = cookie.split("=");
			cookies.put(temp[0],temp[1]);
		}
		cookies.put("Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac", new Date().getTime()+"");
		cookies.put("dc_tos", "p1gbsy");
		return cookies;
	}
}
