package com.sxkl.cloudnote.statistic.service;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.log.entity.LogLevel;
import com.sxkl.cloudnote.log.service.LogService;
import com.sxkl.cloudnote.note.service.NoteService;
import com.sxkl.cloudnote.searcher.service.SearchService;
import com.sxkl.cloudnote.statistic.model.*;
import com.sxkl.cloudnote.todo.service.TodoService;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatisticService {

    @Autowired
    private NoteService noteService;
    @Autowired
    private FlagService flagService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private LogService logService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private TodoService todoService;
    private static final List<String> MONTHS = Lists.newArrayList();

    @PostConstruct
    private void init() {
        for (int i = 1; i < 13; i++) {
            if(i < 10) {
                MONTHS.add("0"+i);
                continue;
            }
            MONTHS.add(i+"");
        }
    }

    @Logger(message = "获取统计信息 topData tableData")
    public StatisticData getTopAndTableData(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        String userId = sessionUser.getId();
        StatisticData statisticData = new StatisticData();
        TopData topData = getTopData(userId);
        statisticData.setTopData(topData);
        List<HitData> hitData = getHitDatas(userId);
        statisticData.setHitDatas(hitData);
        List<RecentData> recentData = getRecentDatas(userId);
        statisticData.setRecentDatas(recentData);
        List<FlagData> flagData = getFlagDatas(userId);
        statisticData.setFlagDatas(flagData);
        List<SearchData> searchData = getSearchDatas(userId);
        statisticData.setSearchDatas(searchData);
        return statisticData;
    }

    private TopData getTopData(String userId) {
        TopData topData = new TopData();
        int noteNum = noteService.getNoteNum(userId);
        topData.setNoteNum(noteNum);

        int flagNum = flagService.getFlagNum(userId);
        topData.setFlagNum(flagNum);

        int articleNum = articleService.getArticleNum(userId);
        topData.setArticleNum(articleNum);

        int blogNum = articleService.getBlogNum(userId);
        topData.setBlogNum(blogNum);

        int todayArticleNum = articleService.getTodayArticleNum(userId);
        topData.setTodayArticleNum(todayArticleNum);

        int logNum = logService.getLogNum(userId);
        topData.setLogNum(logNum);
        return topData;
    }

    private List<HitData> getHitDatas(String userId) {
        List<Article> articles = articleService.getHitDatas(userId);
        List<HitData> hitDatas = Lists.newArrayListWithCapacity(articles.size());
        for (int i = 0; i < articles.size(); i++) {
            hitDatas.add(HitData.configureHitData(i+1, articles.get(i)));
        }
        return hitDatas;
    }

    private List<RecentData> getRecentDatas(String userId) {
        List<Article> articles = articleService.getRecentDatas(userId);
        List<RecentData> recentDatas = Lists.newArrayListWithCapacity(articles.size());
        for (int i = 0; i < articles.size(); i++) {
            recentDatas.add(RecentData.configureHitData(i+1, articles.get(i)));
        }
        return recentDatas;
    }

    private List<FlagData> getFlagDatas(String userId) {
        return flagService.getFlagDatas(userId);
    }

    private List<SearchData> getSearchDatas(String userId) {
        Set<ZSetOperations.TypedTuple<Object>> hotLabelWithScore = searchService.getHotLabelWithScore();
        List<SearchData> searchDatas = Lists.newArrayListWithCapacity(hotLabelWithScore.size());
        for (ZSetOperations.TypedTuple<Object> objectTypedTuple : hotLabelWithScore) {
            SearchData searchData = new SearchData();
            searchData.setKey(objectTypedTuple.getValue().toString());
            searchData.setNum(objectTypedTuple.getScore());
            searchDatas.add(searchData);
        }
        List<SearchData> result = searchDatas.stream().sorted(Comparator.comparing(SearchData::getNum).reversed()).collect(Collectors.toList());
        for (int i = 0; i < result.size(); i++) {
            SearchData searchData = result.get(i);
            String key = searchData.getKey();
            searchData.setKey(key.replaceAll("\"", ""));
            searchData.setIndex(i+1);
        }
        return result;
    }

    @Logger(message = "getPieData")
    public PieData getPieData(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        String userId = sessionUser.getId();
        List<KeyValue> keyValues = noteService.getPieData(userId);
        PieData pieData = new PieData();
        List<String> names = keyValues.stream().map(KeyValue::getName).collect(Collectors.toList());
        pieData.setOptionData(names);
        pieData.setSeriesData(keyValues);
        return pieData;
    }

    @Logger(message = "getBarPercentData")
    public BarPercentData getBarPercentData(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        String userId = sessionUser.getId();
        DateRange firstDateRange = DateUtils.getFirstQuarter();
        Map<String, String> firstQuarter = logService.getBarPercentData(userId, firstDateRange);

        DateRange secondDateRange = DateUtils.getSecondQuarter();
        Map<String, String> secondQuarter = logService.getBarPercentData(userId, secondDateRange);

        DateRange thirdDateRange = DateUtils.getThirdQuarter();
        Map<String, String> thirdQuarter = logService.getBarPercentData(userId, thirdDateRange);

        DateRange fouthDateRange = DateUtils.getFouthQuarter();
        Map<String, String> fouthQuarter = logService.getBarPercentData(userId, fouthDateRange);

        BarPercentData barPercentData = new BarPercentData();

        List<String> infoDatas = Lists.newArrayListWithCapacity(4);
        infoDatas.add(firstQuarter.getOrDefault(LogLevel.INFO.name(), "0"));
        infoDatas.add(secondQuarter.getOrDefault(LogLevel.INFO.name(), "0"));
        infoDatas.add(thirdQuarter.getOrDefault(LogLevel.INFO.name(), "0"));
        infoDatas.add(fouthQuarter.getOrDefault(LogLevel.INFO.name(), "0"));
        barPercentData.setInfoDatas(infoDatas);

        List<String> warnDatas = Lists.newArrayListWithCapacity(4);
        warnDatas.add(firstQuarter.getOrDefault(LogLevel.WARN.name(), "0"));
        warnDatas.add(secondQuarter.getOrDefault(LogLevel.WARN.name(), "0"));
        warnDatas.add(thirdQuarter.getOrDefault(LogLevel.WARN.name(), "0"));
        warnDatas.add(fouthQuarter.getOrDefault(LogLevel.WARN.name(), "0"));
        barPercentData.setWarnDatas(warnDatas);

        List<String> errorDatas = Lists.newArrayListWithCapacity(4);
        errorDatas.add(firstQuarter.getOrDefault(LogLevel.ERROR.name(), "0"));
        errorDatas.add(secondQuarter.getOrDefault(LogLevel.ERROR.name(), "0"));
        errorDatas.add(thirdQuarter.getOrDefault(LogLevel.ERROR.name(), "0"));
        errorDatas.add(fouthQuarter.getOrDefault(LogLevel.ERROR.name(), "0"));
        barPercentData.setErrorDatas(errorDatas);

        List<String> debugDatas = Lists.newArrayListWithCapacity(4);
        debugDatas.add(firstQuarter.getOrDefault(LogLevel.DEBUG.name(), "0"));
        debugDatas.add(secondQuarter.getOrDefault(LogLevel.DEBUG.name(), "0"));
        debugDatas.add(thirdQuarter.getOrDefault(LogLevel.DEBUG.name(), "0"));
        debugDatas.add(fouthQuarter.getOrDefault(LogLevel.DEBUG.name(), "0"));
        barPercentData.setDebugDatas(debugDatas);
        return barPercentData;
    }

    public LineData getLineData(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        String userId = sessionUser.getId();
        List<KeyValue> keyValues = todoService.getLineData(userId);
        Map<String, String> map = keyValues.stream().collect(Collectors.toMap(KeyValue::getName, KeyValue::getValue));
        LineData lineData = new LineData();
        lineData.setMonths(MONTHS);
        List<String> values = Lists.newArrayListWithCapacity(12);
        for (String month : MONTHS) {
            String value = map.getOrDefault(month, "0");
            values.add(value);
        }
        lineData.setDatas(values);
        return lineData;
    }

    @Logger(message = "getBarData")
    public BarData getBarData(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        String userId = sessionUser.getId();
        List<KeyValue> keyValues = articleService.getBarData(userId);
        Map<String, String> map = keyValues.stream().collect(Collectors.toMap(KeyValue::getName, KeyValue::getValue));
        BarData barData = new BarData();
        barData.setMonths(MONTHS);
        List<String> values = Lists.newArrayListWithCapacity(12);
        for (String month : MONTHS) {
            String value = map.getOrDefault(month, "0");
            values.add(value);
        }
        barData.setDatas(values);
        return barData;
    }
}
