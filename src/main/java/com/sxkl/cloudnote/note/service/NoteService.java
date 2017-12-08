package com.sxkl.cloudnote.note.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.cache.annotation.RedisDisCachable;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.main.entity.TreeNode;
import com.sxkl.cloudnote.note.dao.NoteDao;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.user.dao.UserDao;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UUIDUtil;
import com.sxkl.cloudnote.utils.UserUtil;

@Service
public class NoteService {
	
	@Autowired
	private NoteDao noteDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ArticleDao articleDao;

	@Logger(message="获取菜单树笔记本根节点")
	public TreeNode getRootTreeNode() {
		TreeNode note = new TreeNode();
		note.setId(Constant.TREE_MENU_NOTE_ID_PREFIX+UUIDUtil.getUUID());
		note.setText("笔记本");
		note.setIsleaf(false);
		return note;
	}
	
	@Logger(message="将笔记本转换为菜单树节点")
	public TreeNode convertToTreeNode(Note note){
		TreeNode treeNode = new TreeNode();
		treeNode.setId(Constant.TREE_MENU_NOTE_ID_PREFIX+note.getId());
		treeNode.setText(note.getName());
		treeNode.setIsleaf(true);
		return treeNode;
	}

	@Logger(message="保存笔记本")
	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void insertNote(HttpServletRequest request) {
		User sessionUser = UserUtil.getSessionUser(request);
		User user = userDao.selectUser(sessionUser);
		String name = request.getParameter("name");
		Note note = new Note();
		note.setName(name);
		note.setUser(user);
		noteDao.save(note);
		user.getNotes().add(note);
	}

	@Logger(message="删除笔记本")
	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void deleteNote(HttpServletRequest request) {
		String id = getNoteIdFromFront(request);
		Note note = noteDao.findById(id);
		User sessionUser = UserUtil.getSessionUser(request);
		User user = userDao.selectUser(sessionUser);
		user.getNotes().remove(note);
		userDao.updateUser(user);
		note.setArticles(null);
		noteDao.deleteNote(note);
	}
	
	@Logger(message="更新笔记本")
	@RedisDisCachable(key={Constant.TREE_MENU_KEY_IN_REDIS,Constant.TREE_FOR_ARTICLE_KEY_IN_REDIS,})
	public void updateNote(HttpServletRequest request) {
		String id = getNoteIdFromFront(request);
		String name = request.getParameter("name");
		Note note = noteDao.findById(id);
		note.setName(name);
		noteDao.updateNote(note);
	}

	private String getNoteIdFromFront(HttpServletRequest request) {
		String frontId = request.getParameter("id");
		return frontId.substring(Constant.TREE_MENU_NOTE_ID_PREFIX.length());
	}

	@Logger(message="获取下拉框笔记本")
	public String getNoteDataFromCombo(HttpServletRequest request) {
		User user = UserUtil.getSessionUser(request);
		List<Note> notes = noteDao.getAllNote(user.getId());
		Gson gson = new Gson();
		return gson.toJson(notes);
	}

	@Logger(message="根据主键获取笔记本")
	public Note selectNoteById(String noteId) {
		return noteDao.selectNoteById(noteId);
	}

	@Logger(message="根据文章主键获取笔记本")
	public Note getNoteByArticleId(HttpServletRequest request) {
		String articleId = request.getParameter("articleId");
		Article article = articleDao.selectArticleById(articleId);
		Note note = article.getNote();
		Note result = new Note();
		result.setId(note.getId());
		result.setName(note.getName());;
		return result;
	}

	@Logger(message="用户关联的笔记本")
	public List<Note> getAllNoteByUserId(String userId) {
		return noteDao.getAllNote(userId);
	}
	
	@Logger(message="获取笔记本菜单树")
	public String getNoteTreeMenu(String userId) {
		TreeNode rootNote = getRootTreeNode();
		Gson gson = new Gson();
		StringBuilder treeJson = new StringBuilder();
		treeJson.append(gson.toJson(rootNote));
		treeJson.append(Constant.COMMA);
		
		List<Note> notes = getAllNoteByUserId(userId);
		for(Note note : notes){
			TreeNode treeNode = convertToTreeNode(note);
			treeNode.setPid(rootNote.getId());
			treeJson.append(gson.toJson(treeNode));
			treeJson.append(Constant.COMMA);
		}
		validateJson(treeJson);
		return treeJson.toString();
	}
	
	private void validateJson(StringBuilder treeJson) {
		String treeStr = treeJson.toString();
		if(treeStr.substring(treeStr.length()-1, treeStr.length()).equals(Constant.COMMA)){
			treeJson.deleteCharAt(treeJson.length()-1);
		}
	}

}
