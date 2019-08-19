package com.sxkl.cloudnote.love.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sxkl.cloudnote.love.service.LoveService;

/**
 * @author: wangyao
 * @date: 2018年5月18日 下午5:14:14
 * @description:
 */
@Controller
@RequestMapping("/love")
public class LoveController {

    @Autowired
    private LoveService loveService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String index(HttpServletRequest request) {
        return loveService.getLovePageName(request);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ModelAndView page(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName(request.getParameter("path"));
        return mv;
    }
}
