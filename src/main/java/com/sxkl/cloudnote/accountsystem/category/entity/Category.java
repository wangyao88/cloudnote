package com.sxkl.cloudnote.accountsystem.category.entity;

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

import com.sxkl.cloudnote.accountsystem.accountbook.entity.AccountBook;
import com.sxkl.cloudnote.accountsystem.tally.entity.Tally;

import lombok.Data;

/**
 * @author: wangyao
 * @date: 2018年5月8日 上午11:33:38
 * @description:
 */
@Data
@Entity
@Table(name = "cn_category")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Category {

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@Column(name = "type", unique = false, nullable = false)
	private String type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_book_id")
	private AccountBook accountBook;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	@Cascade(value = { CascadeType.ALL })
	private Set<Tally> tallies;
}
