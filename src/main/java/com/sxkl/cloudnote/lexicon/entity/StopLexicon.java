package com.sxkl.cloudnote.lexicon.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyao
 * @date 2018年1月13日 下午12:01:43
 * @description: 中文分词器之停用词
 */
@Entity
@DiscriminatorValue("stopLexicon")
public class StopLexicon extends Lexicon implements Serializable{

	private static final long serialVersionUID = 9175364095514385547L;
}
