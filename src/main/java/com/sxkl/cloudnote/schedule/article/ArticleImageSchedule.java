package com.sxkl.cloudnote.schedule.article;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.image.entity.Image;
import com.sxkl.cloudnote.image.service.ImageService;
import com.sxkl.cloudnote.log.annotation.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ArticleImageSchedule {
	
	private static final int PAGE_SIZE = 100;
	
	@Autowired
	private ImageService imageService;
	@Autowired
	private ArticleService articleService;
	private ListeningExecutorService executorService;
	private AtomicInteger size = new AtomicInteger(0);//删除图片条数
	private AtomicInteger num = new AtomicInteger(0);//执行数
	
	@Logger(message="定时删除失效笔记图片")
	@Scheduled(cron="0 0 4 * * ?")
    public void deleteExpireImage(){
		int total = imageService.getTotal();
		if(total == 0){
			return;
		}
		int pageNum = getPageNum(total);
		initExecutorService();
		for(int pageIndex = 0; pageIndex < pageNum; pageIndex++){
			DeleteImageTask task = new DeleteImageTask(pageIndex);
			final ListenableFuture<Integer> future = executorService.submit(task);
	    	Futures.addCallback(future, new FutureCallback<Integer>(){
	    		@Override
	    		public void onSuccess(Integer delSize) {
	    			add(size,delSize);
	    			add(num,1);
	    		}
	    		@Override
	    		public void onFailure(Throwable t) {
	    			add(num,1);
	    			log.error("删除无效图片失败！错误信息：{}", t.getMessage());
	    		}
	    	});
		}
		while(get(num) < pageNum){}
		destroyExecutorService();
		log.info("成功删除{}条无效图片信息",get(size));
    }
	
	private int getPageNum(int total){
		int result = (total/PAGE_SIZE);
		if(result*PAGE_SIZE < total){
			result += 1;
		}
		return result;
	}
	
	private void initExecutorService(){
		int cpuCore = Runtime.getRuntime().availableProcessors();
		executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(cpuCore*2));
	}
	
	private void destroyExecutorService(){
		if(executorService != null && !executorService.isShutdown()){
			executorService.shutdownNow();
		}
		executorService = null;
	}
	
	private synchronized void add(AtomicInteger source, int num){
		source.addAndGet(num);
	}
	
	private synchronized int get(AtomicInteger source){
		return source.get();
	}

	private class DeleteImageTask implements Callable<Integer>{
		
		private int pageIndex;
		
		public DeleteImageTask(int pageIndex) {
			super();
			this.pageIndex = pageIndex;
		}
		
		@Override
		public Integer call() throws Exception {
			List<Image> delImages = Lists.newArrayList();
			List<Image> updateImages = Lists.newArrayList();
			List<Image> images = imageService.findPage(pageIndex, PAGE_SIZE);
			for(Image image : images){
				Article article = articleService.getArticleByImageName(image.getName());
				if(Objects.isNull(article)){
					delImages.add(image);
					continue;
				}
				if(StringUtils.isEmpty(image.getAId())){
					image.setAId(article.getId());
					updateImages.add(image);
				}
			}
			imageService.deleteAll(delImages);
			imageService.updateAll(updateImages);
			return delImages.size();
		}
	}
}