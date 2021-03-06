package com.sxkl.cloudnote.eventdriven.listener;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.eventdriven.entity.FlagPublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.FlagPublisherEvent;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.log.annotation.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class FlagListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private FlagService flagService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (isNotDuty(event)) {
            return;
        }
        FlagPublisherBean flag = (FlagPublisherBean) event.getSource();
        cacheAddArticleTreeMenu(flag.getUserId());
    }


    private boolean isNotDuty(ApplicationEvent event) {
        return !event.getClass().toString().equals(FlagPublisherEvent.class.toString());
    }

    @Logger(message = "缓存增加笔记时的标签树")
    public void cacheAddArticleTreeMenu(String userId) {
        String key = Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS + userId;
        boolean hasCached = redisTemplate.hasKey(key);
        if (hasCached) {
            return;
        }
        String flagTreeMenu = flagService.getFlagTreeMenu(userId);
        StringBuilder treeJson = new StringBuilder();
        treeJson.append(Constant.TREE_MENU_PREFIX);
        treeJson.append(flagTreeMenu);
        treeJson.append(Constant.TREE_MENU_SUFFIX);
        redisTemplate.opsForValue().set(key, treeJson.toString());
        long baseKeyTime = redisTemplate.getExpire(key);
        if (baseKeyTime == -1) {
            redisTemplate.expire(key, 30, TimeUnit.MINUTES);
        }
    }
}
