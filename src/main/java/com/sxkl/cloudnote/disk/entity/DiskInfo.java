package com.sxkl.cloudnote.disk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.user.entity.User;

import lombok.Data;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午4:29:36
 */
@Data
@Entity
@Table(name="cn_disk")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class DiskInfo {

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="totalSize",nullable=false)
	private double totalSize;
	
	@Column(name="usedSize",nullable=false)
	private double usedSize;
	
	@Column(name="isLocked",nullable=false)
	private boolean isLocked;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uId")
	private User user;
}
