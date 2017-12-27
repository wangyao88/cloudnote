package com.sxkl.cloudnote.spider.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.schedule.spider.SpiderSchedule;
import com.sxkl.cloudnote.spider.entity.NetArticle;
import com.sxkl.cloudnote.spider.manager.SearchSpider;

@Service
public class SpiderService {
	
	@Autowired
    private RedisTemplate<Object, NetArticle> redisTemplate;
	@Autowired
	private SpiderSchedule spiderSchedule;
	@Autowired
	private SearchSpider searchSpider;
	
	@Logger(message="获取所有订阅文章")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getArticles(){
		Set<Object> keys = redisTemplate.opsForHash().keys(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS);
		long size = keys.size();
		List articles = null;
		if(size > 25){
			Set<Object> keyTemps = new HashSet<Object>();
			configurateKeys(Lists.newArrayList(keys),keyTemps);
			articles = redisTemplate.opsForHash().multiGet(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS, keyTemps);
		}else{
			articles = redisTemplate.opsForHash().multiGet(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS, keys);
		}
		return OperateResultService.configurateSuccessDataGridResult(Lists.newArrayList(articles),articles.size());
	}
	
	private void configurateKeys(ArrayList<Object> keys,Set<Object> keyTemps){
		if(keyTemps.size() >= 25){
			return;
		}
		Random random = new Random();
		keyTemps.add(keys.get(random.nextInt(keys.size())));
		configurateKeys(keys,keyTemps);
	}
	
	@Logger(message="获取订阅文章")
	public String getArticleById(String id){
		NetArticle article = (NetArticle) redisTemplate.opsForHash().get(Constant.NETARTICLE_KEY_IN_REDIS, id);
		return article.getContent();
	}
	
	@Logger(message="删除订阅文章")
	public void delete(String id) {
		redisTemplate.opsForHash().delete(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS, id);
		redisTemplate.opsForHash().delete(Constant.NETARTICLE_KEY_IN_REDIS, id);
	}
	
	@Logger(message="删除所有订阅文章")
	public void deleteAll() {
		Set<Object> simpleKeys = redisTemplate.opsForHash().keys(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS);
		redisTemplate.opsForHash().delete(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS, simpleKeys.toArray());
		Set<Object> keys = redisTemplate.opsForHash().keys(Constant.NETARTICLE_KEY_IN_REDIS);
		redisTemplate.opsForHash().delete(Constant.NETARTICLE_KEY_IN_REDIS, keys.toArray());
	}

	public void spider() {
		spiderSchedule.spider();
	}

	public int getTotal() {
		Set<Object> keys = redisTemplate.opsForHash().keys(Constant.SIMPLE_NETARTICLE_KEY_IN_REDIS);
		return keys.size();
	}
}
