package com.sxkl.cloudnote.article.entity;

import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.note.entity.Note;

import lombok.Data;

@Data
public class ArticleForEdit {
	
	private Note note;
	private Flag flag;
    private String content;
    private boolean isShared;
    
	public ArticleForEdit() {
		super();
	}
	
	public ArticleForEdit(Note note, Flag flag, String content) {
		this.note = note;
		this.flag = flag;
		this.content = content;
	}

	public ArticleForEdit(Note note, Flag flag, String content, boolean isShared) {
		super();
		this.note = note;
		this.flag = flag;
		this.content = content;
		this.isShared = isShared;
	}
}
