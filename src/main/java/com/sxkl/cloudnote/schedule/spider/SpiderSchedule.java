package com.sxkl.cloudnote.schedule.spider;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.spider.entity.NetArticle;
import com.sxkl.cloudnote.spider.manager.ArticleSpider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SpiderSchedule {

	@Autowired
	private ArticleSpider spider;
	@Autowired
    private RedisTemplate<Object, NetArticle> redisTemplate;
	
	@Logger(message="定时爬取文章")
	@Scheduled(cron="0 0/60 4-6 * * ?")
	public void spider(){
		try {
			List<NetArticle> articles = spider.spider();
			Map<String,NetArticle> whole = new HashMap<String,NetArticle>();
			Map<String,NetArticle> simple = new HashMap<String,NetArticle>();
			for(NetArticle article : articles){
				whole.put(article.getId(),article);
				NetArticle ar = new NetArticle();
				ar.setId(article.getId());
				ar.setTitle(article.getTitle());
				simple.put(article.getId(),ar);
			}
			redisTemplate.expire(Constant.NETARTICLE_KEY_IN_REDIS, 17, TimeUnit.HOURS);
			redisTemplate.expire(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS, 17, TimeUnit.HOURS);
			redisTemplate.opsForHash().putAll(Constant.NETARTICLE_KEY_IN_REDIS,whole);
			redisTemplate.opsForHash().putAll(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS,simple);
		} catch (IOException e) {
			log.error("爬取文章失败！",e.getMessage());
		}
	}
}
