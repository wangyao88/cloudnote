package com.sxkl.cloudnote.user.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sxkl.cloudnote.utils.DESUtil;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.note.entity.Note;

@Entity
@Table(name="cn_user")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class User implements Serializable{
	
	private static final long serialVersionUID = -6480781567126316019L;

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="name",unique=true,nullable=false)
	private String name;
	
	@Column(name="password",nullable=false)
	private String password;
	
	@Column(name="email",unique=true,nullable=false)
	private String email;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="user")
	@Cascade(value={CascadeType.ALL})
	private Set<Note> notes = new HashSet<Note>();
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="user")
	@Cascade(value={CascadeType.ALL})
	private Set<Flag> flags = new HashSet<Flag>();
	
	public User() {
		super();
	}
	
	public User(String id, String name) {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		DESUtil desUtil = new DESUtil();
		this.password = desUtil.encrypt(password);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Note> getNotes() {
		return notes;
	}

	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}

	public Set<Flag> getFlags() {
		return flags;
	}

	public void setFlags(Set<Flag> flags) {
		this.flags = flags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		return true;
	}
	
}
