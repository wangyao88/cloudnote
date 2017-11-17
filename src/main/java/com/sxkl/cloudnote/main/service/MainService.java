package com.sxkl.cloudnote.main.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sxkl.cloudnote.cache.annotation.RedisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.main.entity.TreeNode;
import com.sxkl.cloudnote.note.entity.Note;
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
	

	@RedisCachable(key=Constant.TREE_MENU_KEY_IN_REDIS,dateTime=20)
	public String getTree(HttpServletRequest request) {
		log.info("缓存中没有菜单树，从数据库获取数据，生成菜单树");
		HttpSession session = request.getSession();
		User sessionUser = (User) session.getAttribute(Constant.USER_IN_SESSION_KEY);
		User user = userService.selectUser(sessionUser);
		
		Set<Note> notes = user.getNotes();
		Set<Flag> flags = user.getFlags();
		TreeNode rootNote = noteService.getRootTreeNode();
		TreeNode rootFlag = flagService.getRootTreeNode();
		
		Gson gson = new Gson();
		StringBuilder treeJson = new StringBuilder();
		treeJson.append(Constant.TREE_MENU_PREFIX);
		treeJson.append(gson.toJson(rootNote));
		treeJson.append(Constant.COMMA);
		treeJson.append(gson.toJson(rootFlag));
		treeJson.append(Constant.COMMA);
		
		for(Note note : notes){
			TreeNode treeNode = noteService.convertToTreeNode(note);
			treeNode.setPid(rootNote.getId());
			treeJson.append(gson.toJson(treeNode));
			treeJson.append(Constant.COMMA);
		}
		
		for(Flag flag : flags){
			TreeNode treeNode = flagService.convertToTreeNode(rootFlag,flag);
			treeJson.append(gson.toJson(treeNode));
			treeJson.append(Constant.COMMA);
		}
		
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
