package com.sxkl.cloudnote.spider.manager;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.spider.entity.NetArticle;
import com.sxkl.cloudnote.utils.StringAppendUtils;

@Service
public class SearchSpider {
	
	@Logger(message="搜索文章")
	public List<NetArticle> spider(int page, String key) throws IOException{
		List<NetArticle> articles = Lists.newArrayList();
		Map<String, String> cookies = getCookies();
		String url = StringAppendUtils.append("http://so.csdn.net/so/search/s.do?p=",page,"&q=",key,"&t=&domain=&o=&s=&u=&l=&f=");
		Document document = Jsoup.connect(url).cookies(cookies).get();
		Elements elements = document.getElementsByClass("search-list");
		for(Element element : elements){
			System.out.println(element.html());
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
	
	@Logger(message="搜索新闻怕行榜")
	public String news() throws IOException{
		Map<String, String> cookies = getCookies();
		Document document = Jsoup.connect("http://news.sina.com.cn/hotnews/").cookies(cookies).get();
		Elements elements = document.getElementsByTag("table");
		return StringAppendUtils.append("<table>",elements.get(35).html(),"</table>");
	}
	
	public static void main(String[] args) throws IOException {
		Map<String, String> cookies = getCookies();
		Document document = Jsoup.connect("http://news.sina.com.cn/hotnews/").cookies(cookies).get();
		Elements elements = document.getElementsByTag("table");
//		System.out.println(elements.get(2));
		int count = 0;
		for(Element element : elements){
			count++;
			if(element.html().contains("108名高校学生联名上书后 最高权力机关有大动作")){
				System.out.println(element);
				System.out.println(count);
			}
		}
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
