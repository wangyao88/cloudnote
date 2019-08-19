package com.sxkl.cloudnote.article.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Data
@AllArgsConstructor
@Entity
@Table(name = "cn_same_article")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class SameArticle implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "same_ids", nullable = false)
    private String sameIds;
}
