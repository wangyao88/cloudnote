package com.sxkl.cloudnote.main.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "/main", method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public ModelAndView login(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView(StringUtils.appendJoinEmpty("main/main", "_", PropertyUtil.getMode()));
        return modelAndView;
    }

    @RequestMapping(value = "/main/getTree", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getTree(HttpServletRequest request) {
        return mainService.getTree(request);
    }

    @RequestMapping(value = "/main/getWeather", method = RequestMethod.GET)
    @ResponseBody
    public List<Weather> getWeather(HttpServletRequest request) throws Exception {
        return mainService.getWeather(request);
    }

    @RequestMapping(value = "/main/getAllSkinsFromCombo", method = RequestMethod.GET)
    @ResponseBody
    public List<Skin> getAllSkinsFromCombo() throws Exception {
        return mainService.getAllSkinsFromCombo();
    }
}
