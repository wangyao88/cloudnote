package com.sxkl.cloudnote.spider.manager;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class UrlFactory {

	private static List<String> urls;
	
	@PostConstruct
	public void initUrl(){
		urls = Lists.newArrayList();
		urls.add("http://blog.csdn.net/nav/arch");
		urls.add("http://blog.csdn.net/nav/lang");
		urls.add("http://blog.csdn.net/nav/db");
		urls.add("http://blog.csdn.net/nav/fund");
		urls.add("http://blog.csdn.net/nav/ops");
	}
	
	public String getUrl(){
		Random random = new Random();
		int index = random.nextInt(urls.size());
		return urls.get(index);
	}
}
