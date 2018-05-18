package com.sxkl.cloudnote.accountsystem.accountbook.entity;

import java.util.Date;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

import com.sxkl.cloudnote.accountsystem.category.entity.Category;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.DateUtils;

/**
 * @author: wangyao
 * @date: 2018年5月8日 上午11:29:14
 * @description:
 */
@Entity
@Table(name = "cn_account_book")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class AccountBook {

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name = "id", unique = true, nullable = false)
	private String id;

	@Column(name = "name", unique = false, nullable = false)
	private String name;

	@Column(name = "mark", unique = false, nullable = true)
	private String mark;

	@Column(name = "create_date", unique = false, nullable = false)
	private Date createDate;

	@Transient
	private float income;

	@Transient
	private float outcome;
	
	@Transient
	private float remainingSum;
	
	@Transient
	private String createDateStr;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accountBook")
	@Cascade(value = { CascadeType.ALL })
	private Set<Category> categories;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	public AccountBook() {
		
	}
	
	public AccountBook(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public AccountBook(String id, String name, String mark, Date createDate) {
		this.id = id;
		this.name = name;
		this.mark = mark;
		this.createDate = createDate;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public float getIncome() {
		return income;
	}

	public void setIncome(float income) {
		this.income = income;
	}

	public float getOutcome() {
		return outcome;
	}

	public void setOutcome(float outcome) {
		this.outcome = outcome;
	}

	public float getRemainingSum() {
		return remainingSum;
	}

	public void setRemainingSum(float remainingSum) {
		this.remainingSum = remainingSum;
	}

	public String getCreateDateStr() {
		return DateUtils.formatDate2Str(this.getCreateDate());
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = DateUtils.formatDate2Str(this.getCreateDate());
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
