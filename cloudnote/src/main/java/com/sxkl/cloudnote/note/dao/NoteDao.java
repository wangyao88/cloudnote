package com.sxkl.cloudnote.note.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.sxkl.cloudnote.common.dao.BaseDao;
import com.sxkl.cloudnote.note.entity.Note;

@Repository
public class NoteDao extends BaseDao {

	public void saveOrUpdate(Note note) {
		this.getHibernateTemplate().saveOrUpdate(note);
	}

	public void save(Note note) {
		this.getHibernateTemplate().save(note);
	}

	public Note findById(String id) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.load(Note.class, id);
	}

	public void deleteNote(Note note) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(note);
	}

	public void updateNote(Note note) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(note);
	}

	@SuppressWarnings("unchecked")
	public List<Note> getAllNote(String uid) {
		Session session = this.getSessionFactory().getCurrentSession();
        String hql = "select new Note(n.id,n.name) from Note n where n.user.id=:uid";  
        Query query = session.createQuery(hql);
        query.setString("uid", uid);
        List<Note> notes = query.list();  
        return notes;  
	}

	public Note selectNoteById(String noteId) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.get(Note.class, noteId);
	}


}
