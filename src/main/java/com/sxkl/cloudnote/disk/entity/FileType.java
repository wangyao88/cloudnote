package com.sxkl.cloudnote.disk.entity;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.entity.ArraySet;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午5:11:59
 */
@Service
public class FileType {
	
	private static Set<String> imageType;
	private static Set<String> vidoeType;
	private static Set<String> zipType;
	private static Set<String> wordType;
	private static Set<String> excelType;
	private static Set<String> pptType;
	private static Set<String> pdfType;
	private static Set<String> txtType;
	
	@PostConstruct
	public void init(){
		imageType = new ArraySet<String>("png", "jpg", "jpeg", "gif", "bmp");
		vidoeType = new ArraySet<String>("flv", "swf", "mkv", "avi", "rm", "rmvb", "mpeg", "mpg","ogg", "ogv", "mov", "wmv", "mp4", "webm", "mp3", "wav", "mid");
		zipType = new ArraySet<String>("rar", "zip", "tar", "gz", "7z", "bz2", "cab", "iso");
		wordType = new ArraySet<String>("doc", "docx");
		excelType = new ArraySet<String>("xls", "xlsx");
		pptType = new ArraySet<String>("ppt", "pptx");
		pdfType = new ArraySet<String>("pdf");
		txtType = new ArraySet<String>("txt", "md", "xml","html", "jsp");
	}
	
//	public String getIcon(String type){
//		
//	}
}
