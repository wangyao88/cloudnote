package com.sxkl.cloudnote.quicktext.controller;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.quicktext.entity.QuickText;
import com.sxkl.cloudnote.quicktext.service.QuickTextService;
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
@RequestMapping("/quickText")
public class QuickTextController {

    @Autowired
    private QuickTextService quickTextService;

    @Logger(message = "跳转到quickText主页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return StringUtils.appendJoinEmpty("quickText/index", "_", PropertyUtil.getMode());
    }

    @Logger(message = "添加QuickText")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String add(HttpServletRequest request) {
        try {
            String id = quickTextService.insert(request);
            return OperateResultService.configurateSuccessResult(id);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "删除QuickText")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(HttpServletRequest request) {
        try {
            quickTextService.delete(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "更新QuickText")
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String update(HttpServletRequest request) {
        try {
            String id = quickTextService.update(request);
            return OperateResultService.configurateSuccessResult(id);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取QuickText")
    @RequestMapping(value = "/findOne", method = RequestMethod.GET)
    @ResponseBody
    public String findOne(HttpServletRequest request) {
        try {
            QuickText quickText = quickTextService.findOne(request);
            return OperateResultService.configurateSuccessResult(quickText);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取所有QuickText")
    @RequestMapping(value = "/findAll", method = RequestMethod.POST)
    @ResponseBody
    public String findAll(HttpServletRequest request) {
        try {
            List<QuickText> quickTexts = quickTextService.findAll(request);
            return OperateResultService.configurateSuccessDataGridResult(quickTexts, quickTexts.size());
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取QuickText的总量")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    @ResponseBody
    public String count(HttpServletRequest request) {
        try {
            boolean status = quickTextService.count(request);
            if(status) {
                return OperateResultService.configurateSuccessResult();
            }
            return OperateResultService.configurateFailureResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}