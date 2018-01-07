package com.sxkl.cloudnote.spider.manager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.spider.entity.NetArticle;
import com.sxkl.cloudnote.spider.entity.News;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.StringAppendUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SearchSpider {
	
	private static final int NEW_BEGIN = 35;
	private static final int NEW_END = 40;
	
	@Logger(message="搜索文章")
	public List<NetArticle> spider(int page, String key) throws IOException{
		key = URLEncoder.encode(key, "utf-8");
		List<NetArticle> articles = Lists.newArrayList();
		Map<String, String> cookies = getCookies();
		String url = StringAppendUtils.append("http://so.csdn.net/so/search/s.do?p=",page,"&q=",key,"&t=&domain=&o=&s=&u=&l=&f=");
		Document document = Jsoup.connect(url).cookies(cookies).get();
		Elements elements = document.getElementsByClass("search-list");
		for(Element element : elements){
			NetArticle article = new NetArticle();
			Elements as = element.getElementsByTag("a");
			String title = as.get(0).text();
			if(title.length() > 30){
				title = title.substring(0, 30) + "...";
			}
			article.setTitle(title);
			if(as.size()>1){
				for(int i = 1; i < as.size(); i++){
					as.get(i).remove();
				}
			}
			Elements children = element.getElementsByTag("dd");
			for(Element ele : children){
				if(!ele.hasClass("search-detail")){
					ele.remove();
				}else{
					String text = ele.text();
					if(text.length() > 80){
						text = text.substring(0, 80) + "...";
						text.replaceAll("key", "<em>"+key+"</em>");
						ele.text(text);
					}
				}
			}
			article.setContent(element.html());
			articles.add(article);
		}
		return articles;
	}
	
	@Logger(message="搜索新闻排行榜")
	public String news() throws IOException{
		Map<String, String> cookies = getCookies();
		Document document = Jsoup.connect("http://news.sina.com.cn/hotnews/").cookies(cookies).get();
		Elements elements = document.getElementsByTag("table");
		Set<String> news = new HashSet<String>();
		getNews(elements,news);
		StringBuilder result = new StringBuilder();
		for(String _new : news){
			result.append(_new);
		}
		return result.toString();
	}

	private void getNews(Elements elements,Set<String> news) {
		if(news.size() >= 4){
			return;
		}
		int size = elements.size();
		Random random = new Random();
		int index = random.nextInt(size);
		if(index > NEW_BEGIN){
			Elements trs = elements.get(index).getElementsByTag("tr");
			for(Element tr : trs){
				Elements ths = tr.getElementsByTag("th");
				for(Element th : ths){
					th.remove();
				}
				Elements tds = tr.getElementsByTag("td");
				if(!tds.isEmpty() && tds.size() > 3){
					tds.get(0).remove();
					tds.get(1).addClass("new-width");
					tds.get(2).remove();
					tds.get(3).addClass("date-right");
				}
			}
			news.add(StringAppendUtils.append("<table>",elements.get(index).html(),"</table>"));
		}
		getNews(elements,news);
	}
	
	@Logger(message="获取最新一条新闻")
	public News oneNews(){
		Random random = new Random();
		List<News> news = new ArrayList<News>();
		try {
			String date = DateUtils.getNowMonthDay();
			Map<String, String> cookies = getCookies();
			Document document = Jsoup.connect("http://news.sina.com.cn/hotnews/").cookies(cookies).get();
			Elements elements = document.getElementsByTag("tr");
			for(Element tr : elements){
				Elements tds = tr.getElementsByTag("td");
				if(tds.size() >= 4){
					String newsDate = tds.get(3).text();
					if(date.contentEquals(newsDate.substring(0, 5))){
						String source = tds.get(2).text();
						if(StringUtils.isEmpty(source) || NumberUtils.isNumber(source.replaceAll(",", ""))){
							continue;
						}
						String content = tds.get(1).text();
						Elements as = tr.getElementsByTag("a");
						String href = as.get(0).attr("href");
						newsDate = newsDate.substring(0, newsDate.length()-1);
						Document documentContent = Jsoup.connect(href).cookies(cookies).get();
						Elements imgs = documentContent.getElementsByTag("img");
						List<String> srcs = new ArrayList<String>();
						for(Element img : imgs){
							String src = img.attr("src");
							if((src.startsWith("http")||src.startsWith("https")) && !src.contains("gif")){
								srcs.add(src);
							}
						}
						int index = random.nextInt(srcs.size());
						String src = srcs.get(index);
						News _news = new News();
						_news.setContent(content);
						_news.setSource(source);
						_news.setDate(newsDate);
						_news.setHref(href);
						_news.setImage(src);
						news.add(_news);
					}
				}
			}
		} catch (SocketTimeoutException e) {
			log.error("获取单条新闻访问超时");
		} catch (IOException e) {
			log.error("获取单条新闻访问IO异常");
		}
		int index = random.nextInt(news.size());
		return news.get(index);
	}
	
	public static void main(String[] args)  {
		System.out.println("点点滴滴的多多点点滴滴多多多点点滴滴的多多点点滴滴".length());
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
