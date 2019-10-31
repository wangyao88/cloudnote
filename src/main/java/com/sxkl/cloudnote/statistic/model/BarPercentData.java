package com.sxkl.cloudnote.statistic.model;

import lombok.Data;

import java.util.List;

@Data
public class BarPercentData {

    private List<String> infoDatas;
    private List<String> warnDatas;
    private List<String> errorDatas;
    private List<String> debugDatas;
}
