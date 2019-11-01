package com.sxkl.cloudnote.statistic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LogData {

    private Integer index;
    private String level;
    private String message;
    private Integer costTime;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH::mm:ss", timezone = "GMT+8")
    private Date date;

}
