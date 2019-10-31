package com.sxkl.cloudnote.statistic.controller;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.statistic.model.BarPercentData;
import com.sxkl.cloudnote.statistic.model.LineData;
import com.sxkl.cloudnote.statistic.model.PieData;
import com.sxkl.cloudnote.statistic.model.StatisticData;
import com.sxkl.cloudnote.statistic.service.StatisticService;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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

    @Logger(message = "getTopAndTableData")
    @RequestMapping(value = "/getTopAndTableData", method = RequestMethod.GET)
    @ResponseBody
    public StatisticData getTopAndTableData(HttpServletRequest request) {
        return statisticService.getTopAndTableData(request);
    }

    @Logger(message = "getPieData")
    @RequestMapping(value = "/getPieData", method = RequestMethod.GET)
    @ResponseBody
    public PieData getPieData(HttpServletRequest request) {
        return statisticService.getPieData(request);
    }

    @Logger(message = "getBarPercentData")
    @RequestMapping(value = "/getBarPercentData", method = RequestMethod.GET)
    @ResponseBody
    public BarPercentData getBarPercentData(HttpServletRequest request) {
        return statisticService.getBarPercentData(request);
    }

    @Logger(message = "getLineData")
    @RequestMapping(value = "/getLineData", method = RequestMethod.GET)
    @ResponseBody
    public LineData getLineData(HttpServletRequest request) {
        return statisticService.getLineData(request);
    }

    private String getIndex() {
        return StringUtils.appendJoinEmpty("statistic/index", "_", PropertyUtil.getMode());
    }
}
