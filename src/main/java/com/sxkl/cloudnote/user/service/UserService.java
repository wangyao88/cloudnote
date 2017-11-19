package com.sxkl.cloudnote.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.sxkl.cloudnote.cache.service.RedisCacheService;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisCacheService redisCacheService;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	public ModelAndView login(HttpServletRequest request, RedirectAttributesModelMap modelMap){
		ModelAndView mv = new ModelAndView();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        if(chackeLoginParams(userName,password)){
        	User user = validateLogin(userName,password);
        	if(user != null){
        		mv.setViewName("redirect:/main");
                HttpSession session = request.getSession();
                session.setAttribute(Constant.USER_IN_SESSION_KEY, user);
                Constant.onLine(user.getId(), session);
        	}else{
        		mv.setViewName("login/login");
                mv.addObject("error","用户名或密码错误！");
        	}
        }else {
            mv.setViewName("login/login");
            mv.addObject("error","用户名与密码不能为空！");
        }
        return mv;
	}
	
	public void logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constant.USER_IN_SESSION_KEY);
        Constant.outLine(user.getId());
        session.invalidate();
	}

	private User validateLogin(String userName, String password) {
		List<User> users = userDao.getUserByNameAndPass(userName,password);
		if(users != null && users.size() > 0){
			return users.get(0);
		}
		return null;
	}

	private boolean chackeLoginParams(String userName, String password) {
		if(StringUtils.isEmpty(userName)){
			return false;
		}
		if(StringUtils.isEmpty(password)){
			return false;
		}
		return true;
	}

	public User selectUser(User sessionUser) {
		return userDao.selectUser(sessionUser);
	}

	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

}