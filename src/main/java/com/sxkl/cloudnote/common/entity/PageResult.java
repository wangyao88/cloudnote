package com.sxkl.cloudnote.common.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PageResult<E> {

    private List<E> datas;
    private int total;
}
