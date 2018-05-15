package com.sxkl.cloudnote.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.sxkl.cloudnote.common.entity.Constant;

import lombok.Cleanup;

public class PropertyUtil {

	public static Map<String,String> getPropertiesAllValue(String fileName){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Properties properties = new Properties();
			String path = PropertyUtil.class.getClassLoader().getResource(fileName).getPath(); 
			@Cleanup
			InputStream is = new FileInputStream(path);
			properties.load(is);
			Enumeration<Object> keys = properties.keys();
		    while(keys.hasMoreElements()){
				String key = (String) keys.nextElement();
				map.put(key, new String(properties.getProperty(key).getBytes("ISO8859-1"),"UTF-8"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	    return map;
	} 
	
	public static String getCachePages(){
		Map<String,String> map = PropertyUtil.getPropertiesAllValue("init.properties");
		String cachePages = map.get(Constant.CACHE_PAGE_KEYS);
		return cachePages;
	}
	
	public static String getDomain(){
		Map<String,String> map = PropertyUtil.getPropertiesAllValue("init.properties");
		return map.get("cloudnote_domain");
	}
	
	public static String getMode(){
		Map<String,String> map = PropertyUtil.getPropertiesAllValue("init.properties");
		return map.get("mode");
	}
	
}