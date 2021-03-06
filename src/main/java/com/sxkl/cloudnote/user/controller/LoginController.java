package com.sxkl.cloudnote.user.controller;

import com.sxkl.cloudnote.listener.RsaKeyManager;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;
import com.sxkl.cloudnote.utils.DESUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Logger(message = "拦截请求地址/，跳转到登陆页面")
    @RequestMapping("/")
    public String all() {
        return "login/login";
    }

    @Logger(message = "获取Todo的状态信息")
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String login(HttpServletRequest request) {
        return "login/login";
    }

    /**
     * 登录
     *
     * @param httpServletRequest
     * @param modelMap
     * @return
     * @throws Exception
     */
    @Logger(message = "执行登陆逻辑")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpServletRequest httpServletRequest, RedirectAttributesModelMap modelMap) throws Exception {
        ModelAndView mv = userService.login(httpServletRequest, modelMap);
        return mv.getViewName();
    }

    @Logger(message = "退出")
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.HEAD})
    @ResponseBody
    public boolean logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.logout(request);
//		ModelAndView modelAndView = new ModelAndView("redirect:/login");
        return true;
    }

    @Logger(message = "跳转到注册页面")
    @RequestMapping(value = "/login/registe", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String registeIndex() {
        return "login/registe";
    }

    @Logger(message = "检查用户名是否存在")
    @RequestMapping(value = "/login/checkName", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkName(String name) {
        User user = userService.getUserByName(name);
        if (Objects.isNull(user)) {
            return true;
        }
        return false;
    }

    @Logger(message = "注册用户")
    @RequestMapping(value = "/login/registe", method = RequestMethod.POST)
    @ResponseBody
    public boolean registe(String name, String password, String repassword) {
        if (checkName(name) || checkPassword(password, repassword)) {
            DESUtil desUtil = new DESUtil();
            User user = new User();
            user.setName(name);
            user.setPassword(desUtil.encrypt(password));
            userService.registe(user);
            return true;
        }
        return false;
    }

    private boolean checkPassword(String password, String repassword) {
        return !StringUtils.isEmpty(password) && !StringUtils.isEmpty(repassword) && password.equals(repassword);
    }

    @Logger(message = "获取公钥")
    @RequestMapping(value = "/login/getPublicKey", method = RequestMethod.POST)
    @ResponseBody
    public String getPublicKey(String name) {
        return RsaKeyManager.getPublickey();
    }
}