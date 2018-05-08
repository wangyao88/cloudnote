package com.sxkl.cloudnote.accountsystem.tally.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.accountsystem.accountbook.entity.AccountBook;
import com.sxkl.cloudnote.accountsystem.category.entity.Category;

import lombok.Data;

/**
 * @author: wangyao
 * @date: 2018年5月8日 下午12:39:16
 * @description: 
 */
@Data
@Entity
@Table(name = "cn_tally")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Tally {

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name = "money", unique = false, nullable = false)
	private float money;
	
	@Column(name = "mark", unique = false, nullable = true)
	private String mark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
}
