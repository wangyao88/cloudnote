package com.sxkl.cloudnote.statistic.model;

import lombok.Data;

import java.util.List;

@Data
public class PieData {

    private List<String> optionData;
    private List<KeyValue> seriesData;
}
