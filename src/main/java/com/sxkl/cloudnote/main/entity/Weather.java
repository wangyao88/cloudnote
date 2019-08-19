package com.sxkl.cloudnote.main.entity;

import lombok.Data;

/**
 * @author: wangyao
 * @date:2018年1月10日 下午12:57:53
 */
@Data
public class Weather {

    private String city;
    private String date;
    private String status;
    private String temprature;
    private String wind;
}
