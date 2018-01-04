package com.sxkl.cloudnote.disk.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午4:40:56
 */
@Data
@Entity
@Table(name="cn_fileInfo")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class FileInfo {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="name",nullable=false)
	private String name;
	
	@Column(name="size",nullable=false)
	private double size;
	
	@Column(name="type",nullable=false)
	private String type;
	
	@Column(name="icon",nullable=false)
	private String icon;
	
	@Column(name="path",nullable=false)
	private String path;
	
	@Column(name="createDate",nullable=false)
	private Date createDate;
	
	@Column(name="isshare",nullable=false)
	private boolean isshare;
	
	@Column(name="password",nullable=true)
	private String password;
	
	@Column(name="shareurl",nullable=true)
	private String shareurl;
	
	@Column(name="description",nullable=true)
	private String description;
	
	@Column(name="md5",nullable=true)
	private String md5;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fId")
	private FileInfo parent;
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="parent")
	@Cascade(value={CascadeType.ALL, CascadeType.DELETE, CascadeType.SAVE_UPDATE})
	private Set<FileInfo> children = new HashSet<FileInfo>();
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dId")
	private DiskInfo diskInfo;
}
