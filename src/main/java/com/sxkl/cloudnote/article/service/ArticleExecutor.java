package com.sxkl.cloudnote.article.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleExecutor {
	
	@Autowired
	private ArticleDao articleDao;

	private ExecutorService threadPool;
	
	@PostConstruct
	public void initThreadPool(){
		threadPool = Executors.newCachedThreadPool();
	}
	
	public void increaseHitNum(String articleId){
		threadPool.execute(new IncreaseHitNum(articleId));
	}
	
	class IncreaseHitNum implements Runnable{
		
		private String articleId;
		
		public IncreaseHitNum(String articleId) {
			super();
			this.articleId = articleId;
		}

		@Override
		public void run() {
			Article article = articleDao.findOne(articleId);
		    article.setHitNum(article.getHitNum()+1);
			articleDao.update(article);
			log.info("更新笔记阅读次数");
		}
	}
}
