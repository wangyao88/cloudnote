package com.sxkl.cloudnote.news.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.news.entity.News;
import com.sxkl.cloudnote.news.entity.NewsTop;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.StringUtils;

import net.sf.json.JSONObject;

/**
 * @author: wangyao
 * @date: 2018年4月27日 下午1:14:55
 * @description: 
 */
@Service
public class NewsService {
	
	@Autowired
	private NewsTopConfigure newsTopConfigure;
	
	public String getRecentTopAll(){
		try {
			List<NewsTop> tops = getTopAll();
			JSONObject json = new JSONObject();
			Map<String,Integer> configuration = newsTopConfigure.getKeyIndexConfiguration();
			for(Map.Entry<String,Integer> entry : configuration.entrySet()){
				json.put(entry.getKey(),tops.get(entry.getValue()-1));
			}
			return OperateResultService.configurateSuccessResult(json);
		} catch (Exception e) {
			return OperateResultService.configurateFailureResult("获取时事新闻失败！错误信息："+e.getMessage());
		}
	}
	
	private List<NewsTop> getTopAll() throws Exception{
		List<NewsTop> tops = Lists.newArrayList();
		Document document = Jsoup.connect("http://news.sina.com.cn/hotnews/").cookies(getCookie()).get();
		Elements elements = document.getElementsByTag("table");
		for(Element element : elements){
			NewsTop top = new NewsTop();
			List<News> news = getNews(element);
			if(news.isEmpty()){
				continue;
			}
			String category = getCategory(element);
			top.setCategory(category);
			top.setNews(news);
			tops.add(top);
		}
		return tops;
	}
	
	private List<News> getNews(Element element) {
		List<News> newss = Lists.newArrayList();
		Elements trs = element.getElementsByTag("tr");
		for(Element tr : trs){
			News news = getOneNews(tr);
			if(ObjectUtils.isNotNull(news)){
				newss.add(news);
			}
		}
		return newss;
	}

	private News getOneNews(Element tr) {
		News news = null;
		Elements tds = tr.getElementsByTag("td");
		if(!tds.isEmpty() && tds.size() > 1){
			String title = getTitle(tds.get(1));
			String url = getUrl(tds.get(1));
			String source = getSource(tds.get(2));
			String date = getDate(tds.get(3));
			news = new News(title,source,date,url);
		}
		return news;
	}

	private String getDate(Element td) {
		return td.text();
	}

	private String getSource(Element td) {
		return td.text();
	}

	private String getUrl(Element td) {
		Elements as = td.getElementsByTag("a");
		if(!as.isEmpty()){
			return as.get(0).attr("href");
		}
		return StringUtils.EMPTY;
	}

	private String getTitle(Element td) {
		return td.text();
	}

	private String getCategory(Element element) {
		Elements h2s = element.getElementsByTag("h2");
		if(!h2s.isEmpty()){
			return h2s.get(0).text();
		}
		return StringUtils.EMPTY;
	}

	private Map<String,String> getCookie(){
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
	

	public static void main(String[] args) throws IOException {
//		String str = "Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1514210549,1514214067,1514216653,1514303680; uuid_tt_dd=-2938934644871967934_20170917; UN=qq_36265493; UE=\"\"; BT=1513261119195; __message_sys_msg_id=0; __message_gu_msg_id=0; __message_cnel_msg_id=0; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=1788*1*PC_VC; kd_user_id=213e4964-b6e4-4cb6-81e5-fa3052429418; __message_in_school=0; gr_user_id=d507363a-3052-49c1-8dbd-5a3109462f9c; __message_district_code=000000; dc_tos=p1ks9t; UM_distinctid=1608e332815402-012f9868972eb4-7c4f563c-15f900-1608e332816f17; dc_session_id=10_1514303677235.131538; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1514303777";
//		String[] strs = str.split(";");
//		Map<String,String> cookies = new HashMap<String,String>();
//		for(String cookie : strs){
//			String[] temp = cookie.split("=");
//			cookies.put(temp[0],temp[1]);
//		}
//		cookies.put("Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac", new Date().getTime()+"");
//		
//		Document document = Jsoup.connect("http://news.sina.com.cn/hotnews/").cookies(cookies).get();
//		Elements elements = document.getElementsByTag("tr");
////		Elements elements = document.getElementsByClass("loopblk");
//		File file = new File("C:/wangyao/desktop/data/test.html");
//		Files.write("<html><body>", file, Charsets.UTF_8);
//		for(Element element : elements){
//			Files.append(element.toString(), file, Charsets.UTF_8);
//		}
//		Files.append("</html></body>", file, Charsets.UTF_8);
		
//		NewsService service = new NewsService();
//		service.getTopAll();
	}
}
