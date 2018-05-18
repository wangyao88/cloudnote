package com.sxkl.cloudnote.lexicon.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @author wangyao
 * @date 2018年1月13日 下午4:54:35
 * @description:
 */
@Data
@Entity
@Table(name="cn_lexicon")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="discriminator", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("lexicon")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Lexicon {

	@Id
	@GeneratedValue(generator = "uuid")
	@Column(name="id",unique=true,nullable=false)
	private String id;
	
	@Column(name="name",unique=false,nullable=false)
	private String name;
	
	@Column(name="uId",unique=false,nullable=false)
	private String userId;
}
