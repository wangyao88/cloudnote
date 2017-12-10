package com.sxkl.cloudnote.user.service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.google.gson.Gson;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.DESUtil;
import com.sxkl.cloudnote.utils.StringAppendUtils;
import com.sxkl.cloudnote.utils.UserUtil;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Logger(message="跳转到登陆页面")
	public ModelAndView login(HttpServletRequest request, RedirectAttributesModelMap modelMap){
		ModelAndView mv = new ModelAndView();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        if(chackeLoginParams(userName,password)){
        	User user = validateLogin(userName,password);
        	if(user != null){
        		processLoginEvent(request, mv, user);
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

	private void processLoginEvent(HttpServletRequest request, ModelAndView mv, User user) {
		mv.setViewName("redirect:/main");
		HttpSession session = request.getSession();
		session.setAttribute(Constant.USER_IN_SESSION_KEY, user);
		Constant.onLine(user.getId(), session);
		Constant.REAL_DRAFT_PATH = StringAppendUtils.append(request.getSession().getServletContext().getRealPath(File.separator),Constant.DRAFT_PATH_PREFIX);
	}
	
	@Logger(message="退出系统")
	public void logout(HttpServletRequest request){
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constant.USER_IN_SESSION_KEY);
        Constant.outLine(user.getId());
        session.invalidate();
	}

	private User validateLogin(String userName, String password) {
		DESUtil tool = new DESUtil();
		List<User> users = userDao.getUserByNameAndPass(userName,tool.encrypt(password));
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

	@Logger(message="获取用户")
	public User selectUser(User sessionUser) {
		return userDao.selectUser(sessionUser);
	}

	@Logger(message="获取所有用户")
	public List<User> getAllUsers() {
		return userDao.getAllUsers();
	}

	@Logger(message="获取好友下拉框")
	public String getAllFriendsFromCombo(HttpServletRequest request) {
		User sessionUser = UserUtil.getSessionUser(request);
		List<User> users = userDao.getFriends(sessionUser.getId());
		Gson gson = new Gson();
		return gson.toJson(users);
	}

}