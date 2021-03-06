package com.sxkl.cloudnote.flag.controller;

import javax.servlet.http.HttpServletRequest;

import com.sxkl.cloudnote.log.annotation.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.eventdriven.manager.PublishManager;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.flag.service.FlagService;
import com.sxkl.cloudnote.spider.entity.SearchComplete;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.utils.UserUtil;


@RestController
@RequestMapping("/flag")
public class FlagController {

    @Autowired
    private FlagService flagService;

    @Logger(message = "添加标签")
    @RequestMapping(value = "/addFlag", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String addFlag(HttpServletRequest request) {
        try {
            flagService.addFlag(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "更新标签")
    @RequestMapping(value = "/updateFlag", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String updateFlag(HttpServletRequest request) {
        try {
            flagService.updateFlag(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "删除标签")
    @RequestMapping(value = "/deleteFlag", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String deleteFlag(HttpServletRequest request) {
        try {
            flagService.deleteFlag(request);
            return OperateResultService.configurateSuccessResult();
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取标签树")
    @RequestMapping(value = "/getCheckFlagTree", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    public String getCheckFlagTree(HttpServletRequest request) {
        try {
            return flagService.getCheckFlagTree(request);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "获取笔记的标签")
    @RequestMapping(value = "/getFlagByArticleId", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String getFlagByArticleId(HttpServletRequest request) {
        try {
            Flag flag = flagService.getFlagByArticleId(request);
            return OperateResultService.configurateSuccessResult(flag);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @Logger(message = "缓存添加笔记页面中的标签树")
    @RequestMapping(value = "/cacheAddArticleTreeMenu", method = RequestMethod.GET)
    public void cacheAddArticleTreeMenu(HttpServletRequest request) {
        User sessionUser = UserUtil.getSessionUser(request);
        PublishManager.getPublishManager().getFlagPublisher().cacheAddArticleTreeMenu(sessionUser.getId());
    }

    @Logger(message = "搜索标签")
    @RequestMapping(value = "/searchFlag", method = RequestMethod.GET)
    @ResponseBody
    public SearchComplete searchFlag(String query, HttpServletRequest request) {
        User user = UserUtil.getSessionUser(request);
        return flagService.getAllByName(query, user.getId());
    }

    @Logger(message = "获取标签主键")
    @RequestMapping(value = "/getFlagId", method = RequestMethod.POST)
    @ResponseBody
    public String getFlagId(String flagName) {
        return flagService.getFlagId(flagName);
    }
}