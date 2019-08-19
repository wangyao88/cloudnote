package com.sxkl.cloudnote.accountsystem.category.entity;

import lombok.Data;

/**
 * @author: wangyao
 * @date: 2018年5月9日 下午1:16:56
 * @description:
 */
@Data
public class CategoryFront {

    private String name;
    private String type;
    private String parentId;
    private String accountBookId;
    private int level;
}
