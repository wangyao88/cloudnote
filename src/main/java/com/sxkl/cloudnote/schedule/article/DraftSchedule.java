package com.sxkl.cloudnote.schedule.article;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DraftSchedule {
	
	@Autowired
	private ArticleService articleService;
	
	@Logger(message="定时删除失效笔记附件")
	@Scheduled(cron="0 0 2 * * ?")
    public void deleteExpireDraft(){
		if(StringUtils.isEmpty(Constant.REAL_DRAFT_PATH)){
			return;
		}
		File uploadPrefix = new File(Constant.REAL_DRAFT_PATH);
        File[] directories = uploadPrefix.listFiles();
        if(directories == null){
			return;
		}
        for(File file : directories){
        	File[] uploads = file.listFiles();
        	if(uploads == null){
        		continue;
        	}
        	for(File upload : uploads){
            	String fileName = upload.getName();
            	Article article = articleService.getArticleByDraftName(fileName);
            	if(article == null && upload.delete()){
            		log.info("删除失效笔记附件[{}]",fileName);
            	}
            }
        }
    }
}
