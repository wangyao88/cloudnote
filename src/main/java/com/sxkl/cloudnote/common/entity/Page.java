package com.sxkl.cloudnote.common.entity;

/**
 *  * @author wangyao
 *  * @date 2018年1月13日 下午6:20:24
 *  * @description: 分页信息
 *  
 */
public class Page {

    private int index;
    private int size;
    private String userId;
    private String hql;
    private boolean useDefaultHql;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHql() {
        return hql;
    }

    public void setHql(String hql) {
        this.hql = hql;
        setUseDefaultHql(false);
    }

    public boolean isUseDefaultHql() {
        return useDefaultHql;
    }

    public void setUseDefaultHql(boolean useDefaultHql) {
        this.useDefaultHql = useDefaultHql;
    }
}
