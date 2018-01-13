package com.sxkl.cloudnote.lexicon.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyao
 * @date 2018年1月13日 下午12:01:23
 * @description: 中文分词器之分词结果
 */
@Entity
@DiscriminatorValue("keyLexicon")
public class KeyLexicon extends Lexicon implements Serializable{

	private static final long serialVersionUID = 2746789978116826779L;

}
