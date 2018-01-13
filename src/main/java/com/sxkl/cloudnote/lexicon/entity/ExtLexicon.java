package com.sxkl.cloudnote.lexicon.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author wangyao
 * @date 2018年1月13日 下午12:01:23
 * @description: 中文分词器之扩展词
 */
@Entity
@Table(name="cn_extLexicon")
public class ExtLexicon extends Lexicon implements Serializable{

	private static final long serialVersionUID = -1800781578048060593L;

}
