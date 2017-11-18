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

	public TreeNode getRootTreeNode() {
		TreeNode note = new TreeNode();
		note.setId(Constant.TREE_MENU_NOTE_ID_PREFIX+UUIDUtil.getUUID());
		note.setText("笔记本");
		note.setIsleaf(false);
		return note;
	}
	
	public TreeNode convertToTreeNode(Note note){
		TreeNode treeNode = new TreeNode();
		treeNode.setId(Constant.TREE_MENU_NOTE_ID_PREFIX+note.getId());
		treeNode.setText(note.getName()+"("+note.getArticles().size()+")");
		treeNode.setIsleaf(true);
		return treeNode;
	}

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

	public String getNoteDataFromCombo(HttpServletRequest request) {
		User user = UserUtil.getSessionUser(request);
		List<Note> notes = noteDao.getAllNote(user.getId());
		Gson gson = new Gson();
		return gson.toJson(notes);
	}

	public Note selectNoteById(String noteId) {
		return noteDao.selectNoteById(noteId);
	}

	public Note getNoteByArticleId(HttpServletRequest request) {
		String articleId = request.getParameter("articleId");
		Article article = articleDao.selectArticleById(articleId);
		Note note = article.getNote();
		Note result = new Note();
		result.setId(note.getId());
		result.setName(note.getName());;
		return result;
	}

	public List<Note> getAllNoteByUserId(String userId) {
		return noteDao.getAllNote(userId);
	}

}
