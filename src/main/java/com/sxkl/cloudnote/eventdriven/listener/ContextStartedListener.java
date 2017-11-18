package com.sxkl.cloudnote.eventdriven.listener;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.main.service.MainService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContextStartedListener implements ApplicationListener<ContextRefreshedEvent>{
	
	@Autowired
	private UserService userService;
	@Autowired
	private MainService mainService;
	@Autowired
    private RedisTemplate<Object, Object> redisTemplate;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			configurateTreeMenuCache();
		}
	}

	private void configurateTreeMenuCache() {
		List<User> users = userService.getAllUsers();
		for(User user : users){
			String key = Constant.TREE_MENU_KEY_IN_REDIS+user.getId();
			long dateTime = 10L;
			boolean hasCached = redisTemplate.hasKey(key);
			if(!hasCached){
				String treeMenu = mainService.getTree(user);
				redisTemplate.opsForValue().set(key, treeMenu);
				if(dateTime != -1L && dateTime > 0L){
					long baseKeyTime = redisTemplate.getExpire(key);
					if (baseKeyTime == -1) {
						redisTemplate.expire(key, dateTime, TimeUnit.MINUTES);
					}
				}
				log.info("缓存用户["+user.getName()+"]的菜单树");
			}
		}
	}


}
