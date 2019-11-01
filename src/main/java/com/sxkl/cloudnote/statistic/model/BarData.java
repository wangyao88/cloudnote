package com.sxkl.cloudnote.statistic.model;

import lombok.Data;

import java.util.List;

@Data
public class BarData {

    private List<String> months;
    private List<String> datas;
}
