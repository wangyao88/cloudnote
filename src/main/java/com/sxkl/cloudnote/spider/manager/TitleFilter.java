package com.sxkl.cloudnote.spider.manager;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class TitleFilter {
	
	private static Set<String> unValidateTitles;
	
	@PostConstruct
	private void init(){
		unValidateTitles = new HashSet<String>();
		unValidateTitles.add("C#");
		unValidateTitles.add("C++");
		unValidateTitles.add("php");
		unValidateTitles.add("更多专栏");
	}
	
	public String subTitle(String title){
		if(title.length() > 25){
			title = title.substring(0, 25) + "...";
		}
		return title;
	}
	
	public boolean validateTitle(String title){
		for(String unValidateTitle : unValidateTitles){
			if(title.contains(unValidateTitle.toUpperCase()) || title.contains(unValidateTitle.toLowerCase())){
				return false;
			}
		}
		return true;
	}

}
