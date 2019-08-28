package com.sxkl.cloudnote.todo.entity;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fId")
    @NotFound(action = NotFoundAction.IGNORE)
    private Todo parent;

    @Transient
    private Date startDate;

    @Transient
    private Date endDate;

    @Transient
    private String nodeId;

    @Transient
    private String nodeText;

    public Todo(Date beginDateTime) {
        this.beginDateTime = beginDateTime;
    }
}