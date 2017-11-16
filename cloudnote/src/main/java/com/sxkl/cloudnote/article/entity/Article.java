package com.sxkl.cloudnote.article.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.user.entity.User;

import lombok.Data;


@Data
@Entity
@Table(name="cn_article")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Article implements Serializable{

	private static final long serialVersionUID = 1007956249838468112L;

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="title",nullable=false)
	private String title;
	
	@Column(name="content",nullable=false)
	private String content;
	
	@Column(name="createTime",nullable=false)
	private Date createTime;
	
	@Column(name="hitNum",nullable=false)
	private Integer hitNum;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uId")
	private User user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="nId")
	private Note note;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.SAVE_UPDATE})
	@JoinTable(name="cn_flag_artile",joinColumns={@JoinColumn(name="article_id")},inverseJoinColumns={@JoinColumn(name="flag_id")})
    private Set<Flag> flags;
	
	public Article() {
		super();
	}

	public Article(String id, String title, Integer hitNum) {
		this.id = id;
		this.title = title;
		this.hitNum = hitNum;
	}
}
