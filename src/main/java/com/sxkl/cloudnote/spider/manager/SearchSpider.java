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
public class SearchSpider {
	
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
	
	public static void main(String[] args) throws IOException {
		Map<String, String> cookies = getCookies();
		Document document = Jsoup.connect("http://so.csdn.net/so/search/s.do?q=java&t=&o=&s=&l=").cookies(cookies).get();
		Elements elements = document.getElementsByClass("search-list");
		System.out.println(elements.get(6));
	}
	
	private static Map<String, String> getCookies() {
		String str = "Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1514210549,1514214067,1514216653,1514303680; uuid_tt_dd=-2938934644871967934_20170917; UN=qq_36265493; UE=\"\"; BT=1513261119195; __message_sys_msg_id=0; __message_gu_msg_id=0; __message_cnel_msg_id=0; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=1788*1*PC_VC; kd_user_id=213e4964-b6e4-4cb6-81e5-fa3052429418; __message_in_school=0; gr_user_id=d507363a-3052-49c1-8dbd-5a3109462f9c; __message_district_code=000000; dc_tos=p1ks9t; UM_distinctid=1608e332815402-012f9868972eb4-7c4f563c-15f900-1608e332816f17; dc_session_id=10_1514303677235.131538; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1514303777";
		String[] strs = str.split(";");
		Map<String,String> cookies = new HashMap<String,String>();
		for(String cookie : strs){
			String[] temp = cookie.split("=");
			cookies.put(temp[0],temp[1]);
		}
		cookies.put("Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac", new Date().getTime()+"");
		return cookies;
	}
}
