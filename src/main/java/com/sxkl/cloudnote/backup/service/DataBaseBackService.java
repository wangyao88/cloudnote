package com.sxkl.cloudnote.backup.service;

import com.sxkl.cloudnote.backup.entity.DataBaseInfo;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
public interface DataBaseBackService {

    String backup(DataBaseInfo dataBaseInfo);
}
