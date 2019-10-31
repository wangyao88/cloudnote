package com.sxkl.cloudnote.statistic.model;

import lombok.Data;

import java.util.List;

@Data
public class StatisticData {

    private TopData topData;
    private List<HitData> hitDatas;
    private List<RecentData> recentDatas;
    private List<FlagData> flagDatas;
    private List<SearchData> searchDatas;
}
