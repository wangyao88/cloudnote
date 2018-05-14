package com.sxkl.cloudnote.news.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.sxkl.cloudnote.utils.StringUtils;

/**
 * @author: wangyao
 * @date: 2018年4月27日 下午4:12:37
 * @description: 
 */
@Component
public class NewsTopConfigure {

	private static final Map<String,Map<String,Integer>> map = Maps.newHashMap();
	
	@PostConstruct
	private void initConfiguration(){
		Map<String,Integer> sum = getCommonConfiguration(getCommonKey(),new int[]{5,17,27});
		sum.put("video", 24);
		sum.put("image", 23);
		map.put("sum", sum);
		
		Map<String,Integer> domestic = getCommonConfiguration(getCommonKey(),new int[]{3,7,25});
		map.put("domestic", domestic);
		
		Map<String,Integer> international = getCommonConfiguration(getCommonKey(),new int[]{5,6,26});
		map.put("international", international);
		
		Map<String,Integer> social = getCommonConfiguration(getCommonKey(),new int[]{4,8,27});
		map.put("social", social);
		
		Map<String,Integer> sports = getCommonConfiguration(getCommonKey(),new int[]{2,17,28});
		map.put("sports", sports);
		
		Map<String,Integer> finance = getCommonConfiguration(getCommonKey(),new int[]{10,15,31});
		map.put("finance", finance);
		
		Map<String,Integer> entertainment = getCommonConfiguration(getCommonKey(),new int[]{2,14,30});
		map.put("entertainment", entertainment);
		
		Map<String,Integer> technology = getCommonConfiguration(getCommonKey(),new int[]{13,18,29});
		map.put("technology", technology);
		
		Map<String,Integer> military = getCommonConfiguration(getCommonKey(),new int[]{3,16,32});
		map.put("military", military);
	}
	
	private Map<String,Integer> getCommonConfiguration(String[] keys, int[] indexs){
		Map<String,Integer> map = Maps.newHashMap();
		map.put(keys[0],indexs[0]);
		map.put(keys[1],indexs[1]);
		map.put(keys[2],indexs[2]);
		return map;
	}
	
	private String[] getCommonKey(){
		return new String[]{"hit","diss","share"};
	}
	
	public Map<String,Integer> getKeyIndexConfiguration(){
		Map<String,Integer> result = Maps.newHashMap();
		for(Map.Entry<String, Map<String,Integer>> entry : map.entrySet()){
			String key = entry.getKey();
			Map<String,Integer> value = entry.getValue();
			for(Map.Entry<String,Integer> valueEntry : value.entrySet()){
				String subKey = valueEntry.getKey();
				Integer index = valueEntry.getValue();
				result.put(StringUtils.append("_", key,subKey), index);
			}
		}
		return result;
	}
}
