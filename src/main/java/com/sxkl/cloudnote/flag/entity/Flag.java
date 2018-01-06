package com.sxkl.cloudnote.flag.entity;

import java.io.Serializable;
import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.user.entity.User;

@Entity
@Table(name="cn_flag")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Flag implements Serializable{

	private static final long serialVersionUID = 595415798731869756L;

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="name",unique=true,nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fId")
	private Flag parent;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent")
	@Cascade(value={CascadeType.ALL, CascadeType.DELETE, CascadeType.SAVE_UPDATE})
	private Set<Flag> children = new HashSet<Flag>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uId")
	private User user;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@Cascade(value={CascadeType.ALL})
	@JoinTable(name="cn_flag_artile",joinColumns={@JoinColumn(name="flag_id")},inverseJoinColumns={@JoinColumn(name="article_id")})
    private Set<Article> articles = new HashSet<Article>();
	
	@Transient
	private boolean isLeaf;
	
	public Flag() {
	}

	public Flag(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Flag getParent() {
		return parent;
	}

	public void setParent(Flag parent) {
		this.parent = parent;
	}

	public Set<Flag> getChildren() {
		return children;
	}

	public void setChildren(Set<Flag> children) {
		this.children = children;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flag other = (Flag) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
