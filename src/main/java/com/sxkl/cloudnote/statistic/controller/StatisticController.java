package com.sxkl.cloudnote.statistic.controller;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.statistic.model.*;
import com.sxkl.cloudnote.statistic.service.StatisticService;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @Logger(message = "跳转到统计页面")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
        return getIndex();
    }

    @Logger(message = "获取顶部和底部统计数据")
    @RequestMapping(value = "/getTopAndTableData", method = RequestMethod.GET)
    @ResponseBody
    public StatisticData getTopAndTableData(HttpServletRequest request) {
        return statisticService.getTopAndTableData(request);
    }

    @Logger(message = "获取笔记本关联笔记统计饼图数据")
    @RequestMapping(value = "/getPieData", method = RequestMethod.GET)
    @ResponseBody
    public PieData getPieData(HttpServletRequest request) {
        return statisticService.getPieData(request);
    }

    @Logger(message = "获取季度日志状态统计柱状占比图数据")
    @RequestMapping(value = "/getBarPercentData", method = RequestMethod.GET)
    @ResponseBody
    public BarPercentData getBarPercentData(HttpServletRequest request) {
        return statisticService.getBarPercentData(request);
    }

    @Logger(message = "获取todo月度统计折线图数据")
    @RequestMapping(value = "/getLineData", method = RequestMethod.GET)
    @ResponseBody
    public LineData getLineData(HttpServletRequest request) {
        return statisticService.getLineData(request);
    }

    @Logger(message = "获取笔记数量月度统计柱状图数据")
    @RequestMapping(value = "/getBarData", method = RequestMethod.GET)
    @ResponseBody
    public BarData getBarData(HttpServletRequest request) {
        return statisticService.getBarData(request);
    }

    @Logger(message = "获取实时日志监控数据")
    @RequestMapping(value = "/getLogTableData", method = RequestMethod.GET)
    @ResponseBody
    public List<LogData> getLogTableData(int pageIndex, HttpServletRequest request) {
        return statisticService.getLogTableData(pageIndex, request);
    }

    private String getIndex() {
        return StringUtils.appendJoinEmpty("statistic/index", "_", PropertyUtil.getMode());
    }
}
