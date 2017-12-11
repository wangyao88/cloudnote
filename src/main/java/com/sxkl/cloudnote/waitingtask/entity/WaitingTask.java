package com.sxkl.cloudnote.waitingtask.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.user.entity.User;

import lombok.Data;


@Data
@Entity
@Table(name="cn_waitingtask")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class WaitingTask implements Serializable{

	private static final long serialVersionUID = 7835646751798477826L;

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="name",nullable=false)
	private String name;
	
	@Column(name="createDate",nullable=false)
	private Date createDate;
	
	@Column(name="expire",nullable=false)
	private Date expire;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uId")
	private User user;
}
