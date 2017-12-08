package com.sxkl.cloudnote.main.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.note.service.NoteService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MainService {
	
	@Autowired
	private NoteService noteService;
	@Autowired
	private FlagService flagService;
	@Autowired
	private UserService userService;
	
	@Logger(message="获取主页菜单树")
	@RedisCachable(key=Constant.TREE_MENU_KEY_IN_REDIS,dateTime=20)
	public String getTree(HttpServletRequest request) {
		log.info("缓存中没有菜单树，从数据库获取数据，生成菜单树");
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute(Constant.USER_IN_SESSION_KEY);
		User user = userService.selectUser(sessionUser);
		return getTree(user);
	}
	
	@Logger(message="获取主页菜单树")
	public String getTree(User user) {
		String noteTreeMenu = noteService.getNoteTreeMenu(user.getId());
		String flagTreeMenu = flagService.getFlagTreeMenu(user.getId());
		StringBuilder treeJson = new StringBuilder();
		treeJson.append(Constant.TREE_MENU_PREFIX);
		treeJson.append(noteTreeMenu);
		treeJson.append(Constant.COMMA);
		treeJson.append(flagTreeMenu);
		validateJson(treeJson);
		treeJson.append(Constant.TREE_MENU_SUFFIX);
		return treeJson.toString();
	}

	private void validateJson(StringBuilder treeJson) {
		String treeStr = treeJson.toString();
		if(treeStr.substring(treeStr.length()-1, treeStr.length()).equals(Constant.COMMA)){
			treeJson.deleteCharAt(treeJson.length()-1);
		}
	}
	
}
