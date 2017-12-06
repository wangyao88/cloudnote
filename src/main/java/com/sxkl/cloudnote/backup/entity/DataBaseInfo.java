package com.sxkl.cloudnote.backup.entity;

import lombok.Data;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
@Data
public class DataBaseInfo {

    private String driver;
    private String ip;
    private String username;
    private String password;
    private String path;
    private String filename;
    private String schema;
}
