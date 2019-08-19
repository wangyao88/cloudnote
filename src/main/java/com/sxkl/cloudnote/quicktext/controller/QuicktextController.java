package com.sxkl.cloudnote.quicktext.controller;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.quicktext.entity.Quicktext;
import com.sxkl.cloudnote.quicktext.service.QuicktextService;
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
@RequestMapping("/quicktext")
public class QuicktextController {

    @Autowired
    private QuicktextService quicktextService;

    @Logger(message = "跳转到quicktext主页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return StringUtils.appendJoinEmpty("quicktext/index", "_", PropertyUtil.getMode());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(HttpServletRequest request, Quicktext quicktext) {
        try {
            quicktextService.insert(request, quicktext);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(HttpServletRequest request) {
        try {
            quicktextService.delete(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(HttpServletRequest request, Quicktext quicktext) {
        try {
            quicktextService.update(request, quicktext);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/findOne", method = RequestMethod.GET)
    @ResponseBody
    public String findOne(HttpServletRequest request) {
        try {
            Quicktext quicktext = quicktextService.findOne(request);
            return OperateResultService.configurateSuccessResult(quicktext);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public String findAll(HttpServletRequest request) {
        try {
            List<Quicktext> quicktexts = quicktextService.findAll(request);
            return OperateResultService.configurateSuccessDataGridResult(quicktexts, quicktexts.size());
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}