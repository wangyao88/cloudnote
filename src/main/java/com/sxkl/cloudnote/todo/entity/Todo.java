package com.sxkl.cloudnote.todo.entity;

import com.google.common.collect.Lists;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "cn_current_todo")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Todo {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "content")
    private String content;

    @Column(name = "beginDateTime", nullable = false)
    private Date beginDateTime;

    @Column(name = "endDateTime", nullable = false)
    private Date endDateTime;

    /**
     * 0 未完成
     * 1 已完成
     */
    @Column(name = "status")
    private String status;

    @Column(name = "createDateTime", nullable = false)
    private Date createDateTime;

    @Column(name = "updateDateTime")
    private Date updateDateTime;

    @Column(name = "userId", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fId")
    private Todo parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    private List<Todo> children = Lists.newArrayList();

    @Transient
    private Date startDate;

    @Transient
    private Date endDate;
}