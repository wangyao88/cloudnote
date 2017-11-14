package com.sxkl.cloudnote.note.dao;

import java.util.List;

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

	public Note findById(Note note) {
		List<Note> notes = this.getHibernateTemplate().findByExample(note);
		if(notes != null && notes.size() > 0){
			return notes.get(0);
		}
		return null;
	}

	public void deleteNote(Note note) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(note);
	}


}
