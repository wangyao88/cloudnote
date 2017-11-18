package com.sxkl.cloudnote.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginHandlerIntercepter implements HandlerInterceptor {

	private static final String LOGIN_URL = "/login/login";

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String requestURI = request.getRequestURI();
		if (requestURI.indexOf("editClientIfo.action") > 0) {
			// 说明处在编辑的页面
			HttpSession session = request.getSession();
			String username = (String) session.getAttribute("username");
			if (username != null) {
				// 登陆成功的用户
				return true;
			} else {
				// 没有登陆，转向登陆界面
				// request.getRequestDispatcher("/login.jsp").forward(request,arg1);
				response.sendRedirect(request.getContextPath() + LOGIN_URL);
				return false;
			}
		} else {
			return true;
		}
	}
}