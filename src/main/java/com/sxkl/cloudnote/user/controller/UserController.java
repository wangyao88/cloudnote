package com.sxkl.cloudnote.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/onLineNum", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String onLineNum(HttpServletRequest request) {
        try {
            int onLineNum = Constant.onLineNum();
            return OperateResultService.configurateSuccessResult(onLineNum);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }

    @RequestMapping(value = "/getAllFriendsFromCombo", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String getAllFriendsFromCombo(HttpServletRequest request) {
        try {
            return userService.getAllFriendsFromCombo(request);
        } catch (Exception e) {
            return OperateResultService.configurateFailureResult(e.getMessage());
        }
    }
}
