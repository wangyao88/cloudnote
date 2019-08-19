package com.sxkl.cloudnote.quicktext.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cn_quicktext")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Quicktext {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "content")
    private String content;

    @Column(name = "createDateTime", nullable = false)
    private Date createDateTime;

    @Column(name = "updateDateTime")
    private Date updateDateTime;

    @Column(name = "userId", nullable = false)
    private String userId;
}