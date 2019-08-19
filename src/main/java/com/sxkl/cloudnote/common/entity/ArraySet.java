package com.sxkl.cloudnote.common.entity;

import java.util.HashSet;

/**
 * @author: wangyao
 * @date:2018年1月4日 下午5:43:58
 */
public class ArraySet<V> extends HashSet<V> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public ArraySet(V... params) {
        for (V v : params) {
            this.add(v);
        }
    }
}
