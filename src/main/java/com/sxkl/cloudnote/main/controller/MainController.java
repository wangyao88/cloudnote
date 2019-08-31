package com.sxkl.cloudnote.main.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sxkl.cloudnote.log.annotation.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.main.entity.Skin;
import com.sxkl.cloudnote.main.entity.Weather;
import com.sxkl.cloudnote.main.service.MainService;
import com.sxkl.cloudnote.utils.PropertyUtil;
import com.sxkl.cloudnote.utils.StringUtils;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Logger(message = "跳转到主页")
    @RequestMapping(value = "/main", method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public ModelAndView login(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView(StringUtils.appendJoinEmpty("main/main", "_", PropertyUtil.getMode()));
        return modelAndView;
    }

    @Logger(message = "获取主页菜单树")
    @RequestMapping(value = "/main/getTree", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getTree(HttpServletRequest request) {
        return mainService.getTree(request);
    }

    @Logger(message = "获取天气信息")
    @RequestMapping(value = "/main/getWeather", method = RequestMethod.GET)
    @ResponseBody
    public List<Weather> getWeather(HttpServletRequest request) throws Exception {
        return mainService.getWeather(request);
    }

    @Logger(message = "获取所有皮肤信息")
    @RequestMapping(value = "/main/getAllSkinsFromCombo", method = RequestMethod.GET)
    @ResponseBody
    public List<Skin> getAllSkinsFromCombo() throws Exception {
        return mainService.getAllSkinsFromCombo();
    }
}