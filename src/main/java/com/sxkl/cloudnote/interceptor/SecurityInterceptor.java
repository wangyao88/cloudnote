package com.sxkl.cloudnote.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sxkl.cloudnote.common.entity.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

    private static final String LOGIN_URL = "/login";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        
    	HttpSession session = req.getSession(true);

        // 从session 里面获取用户名的信息
        Object obj = session.getAttribute(Constant.USER_IN_SESSION_KEY);

        // 判断如果没有取到用户信息，就跳转到登陆页面，提示用户进行登陆
        if (obj == null || "".equals(obj.toString())) {
            if (req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                res.setHeader("sessionStatus", "timeout");
            } else {
            	  String redirectUrl = req.getContextPath() + LOGIN_URL;
                  res.sendRedirect(redirectUrl);
            }
            return false; // 不再往下执行
        }

        return super.preHandle(req, res, handler);
    }
}