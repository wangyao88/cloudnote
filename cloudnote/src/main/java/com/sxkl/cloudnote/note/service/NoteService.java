package com.sxkl.cloudnote.note.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
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

	public void insertNote(HttpServletRequest request) {
		User user = UserUtil.getSessionUser(request);
		String name = request.getParameter("name");
		Note note = new Note();
		note.setName(name);
		note.setUser(user);
		noteDao.save(note);
		user.getNotes().add(note);
	}

	public void deleteNote(HttpServletRequest request) {
		String id = getNoteIdFromFront(request);
		Note note = noteDao.findById(id);
		User sessionUser = UserUtil.getSessionUser(request);
		User user = userDao.selectUser(sessionUser);
		user.getNotes().remove(note);
		userDao.updateUser(user);
		noteDao.deleteNote(note);
	}
	
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

}
