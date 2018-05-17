package com.sxkl.cloudnote.main.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.main.entity.Skin;
import com.sxkl.cloudnote.main.entity.Weather;
import com.sxkl.cloudnote.note.service.NoteService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
import com.sxkl.cloudnote.utils.IPUtils;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringAppendUtils;

@Service
public class MainService {
	
	@Autowired
	private NoteService noteService;
	@Autowired
	private FlagService flagService;
	@Autowired
	private UserService userService;
	
	@Logger(message="获取主页菜单树")
	@RedisCachable(key=Constant.TREE_MENU_KEY_IN_REDIS,dateTime=20)
	public String getTree(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute(Constant.USER_IN_SESSION_KEY);
		User user = userService.selectUser(sessionUser);
		return getTree(user);
	}
	
	@Logger(message="获取主页菜单树")
	public String getTree(User user) {
		String noteTreeMenu = noteService.getNoteTreeMenu(user.getId());
		String flagTreeMenu = flagService.getFlagTreeMenu(user.getId());
		StringBuilder treeJson = new StringBuilder();
		treeJson.append(Constant.TREE_MENU_PREFIX);
		treeJson.append(noteTreeMenu);
		treeJson.append(Constant.COMMA);
		treeJson.append(flagTreeMenu);
		validateJson(treeJson);
		treeJson.append(Constant.TREE_MENU_SUFFIX);
		return treeJson.toString();
	}

	private void validateJson(StringBuilder treeJson) {
		String treeStr = treeJson.toString();
		if(treeStr.substring(treeStr.length()-1, treeStr.length()).equals(Constant.COMMA)){
			treeJson.deleteCharAt(treeJson.length()-1);
		}
	}
	
	@Logger(message="获取本地天气信息")
	public List<Weather> getWeather(HttpServletRequest request) throws Exception{
		List<Weather> weathers = Lists.newArrayList();
		getWeatherCycle(request,weathers);
        return weathers;
	}
	
	private void getWeatherCycle(HttpServletRequest request,List<Weather> weathers) throws Exception{
		if(!weathers.isEmpty()){
			return;
		}
		String city = StringUtils.EMPTY;
		try {
			city = IPUtils.getCityForWeather(request);
		} catch (Exception e) {
			throw new Exception("获取IP归属地失败!错误信息:{}"+e.getMessage());
		}
		try {
			Map<String,String> map = PropertyUtil.getPropertiesAllValue("cityCode.properties");
			String cityCode = map.get(city);
			String url = StringAppendUtils.append("http://www.weather.com.cn/weather/", cityCode,".shtml"); 
	        Document doc = Jsoup.connect(url).timeout(100000).get();
	        Elements content = doc.getElementsByAttributeValue("id", "7d");
	        for (Element e : content) {  
	            Document conDoc = Jsoup.parse(e.toString());
	            Elements uls = conDoc.getElementsByTag("ul");
	            Elements lis = uls.get(0).getElementsByTag("li");
	            for (Element wt : lis) {
	            	Elements h1s = wt.getElementsByTag("h1");
	            	String date = h1s.get(0).text();
	            	Elements weas = wt.getElementsByClass("wea");
	            	String wea = weas.get(0).text();
	            	Elements tems = wt.getElementsByClass("tem");
	            	String temprature = tems.get(0).text();
	            	Elements winds = wt.getElementsByClass("win");
	            	String wind = winds.get(0).text();
	            	Weather weather = new Weather();
	            	weather.setCity(city);
	            	weather.setDate(date);
	            	weather.setStatus(wea);
	            	weather.setTemprature(temprature);
	            	weather.setWind(wind);
	                weathers.add(weather);
	            }  
	        }
		} catch (Exception e) {
			getWeatherCycle(request,weathers);
		}
	}

	@Logger(message="获取皮肤列表")
	public List<Skin> getAllSkinsFromCombo() {
		List<Skin> skins = Lists.newArrayList();
		skins.add(getSkin("default"));
		skins.add(getSkin("blue2003"));
		skins.add(getSkin("blue2010"));
		skins.add(getSkin("bootstrap"));
		skins.add(getSkin("gray"));
		skins.add(getSkin("jqueryui-cupertino"));
		skins.add(getSkin("jqueryui-smoothness"));
		skins.add(getSkin("metro"));
		skins.add(getSkin("metro-green"));
		skins.add(getSkin("metro-orange"));
		skins.add(getSkin("olive2003"));
		skins.add(getSkin("pure"));
		return skins;
	}
	
	private Skin getSkin(String name){
		Skin skin = new Skin();
		skin.setId(name);
		skin.setName(name);
		skin.setPath(name);
		return skin;
	}
	
}
