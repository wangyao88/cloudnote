package com.sxkl.cloudnote.main.service;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.flag.service.FlagServie;
import com.sxkl.cloudnote.main.entity.TreeNode;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.note.service.NoteService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;

@Service
public class MainService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private NoteService noteService;
	@Autowired
	private FlagServie flagService;
	@Autowired
	private UserService userService;
	

//	@Cacheable(value={Constant.TREE_MENU_VALUE_IN_REDIS},key="caches[0].name")
	public String getTree(HttpServletRequest request) {
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
		
		Flag f = new Flag();
		f.setName("标签1");
		f.setId("dddddddd");
		
		Flag c = new Flag();
		c.setName("标签2");
		c.setId("dsfsdfsd");
		
		Flag m = new Flag();
		m.setName("标签3");
		m.setId("dsfsdfssssssssd");
		
		f.getChildren().add(c);
		c.setParent(f);
		
		
		Set<Flag> flas = new HashSet<Flag>();
		flas.add(f);
		flas.add(c);
		flas.add(m);
		
		for(Flag flag : flas){
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
