package com.sxkl.cloudnote.quicktext.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cn_quicktext")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class QuickText {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "createDateTime", nullable = false)
    private Date createDateTime;

    @Column(name = "updateDateTime")
    private Date updateDateTime;

    @Column(name = "userId", nullable = false)
    private String userId;

    public QuickText(String id, String title) {
        this.id = id;
        this.title = title;
    }
}