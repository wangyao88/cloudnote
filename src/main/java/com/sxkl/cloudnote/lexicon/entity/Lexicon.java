package com.sxkl.cloudnote.lexicon.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.user.entity.User;

import lombok.Data;

/**
 * @author wangyao
 * @date 2018年1月13日 下午12:12:17
 * @description: 词库
 */
@Data
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Lexicon {

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="id",unique=false,nullable=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="uId")
	private User user;
}
