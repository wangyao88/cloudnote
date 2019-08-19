package com.sxkl.cloudnote.image.entity;

import java.io.InputStream;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "cn_image")
@GenericGenerator(name = "uuid", strategy = "uuid")
public class Image implements Serializable {

    private static final long serialVersionUID = 3870353508669595171L;

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "alt", nullable = true)
    private String alt;

    @Column(name = "aId", nullable = true)
    private String aId;

    @Column(name = "content", nullable = true)
    private byte[] content;

    @Transient
    private InputStream imageStream;

    public Image() {
        super();
    }

    public Image(String name, String alt) {
        super();
        this.name = name;
        this.alt = alt;
    }

    public Image(String id, String name, String aId) {
        super();
        this.id = id;
        this.name = name;
        this.aId = aId;
    }
}
