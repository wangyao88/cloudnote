package com.sxkl.cloudnote.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: wangyao
 * @date:2018年1月9日 下午3:17:10
 * 解决登陆前后会话标识未更新
 */
public class LoginFilter implements Filter{
	
    public static final String NEW_SESSION_INDICATOR = "com.sxkl.cloudnote.filter.LoginFilter";  

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String url = httpRequest.getRequestURI();
			if (httpRequest.getSession() != null) {
				HttpSession session = httpRequest.getSession();
				Map<String, Object> old = new HashMap<String, Object>();
				Enumeration<String> keys = session.getAttributeNames();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					if (!NEW_SESSION_INDICATOR.equals(key)) {
						old.put(key, session.getAttribute(key));
						session.removeAttribute(key);
					}
				}
				if (httpRequest.getMethod().equals("POST") && httpRequest.getSession() != null
						&& !httpRequest.getSession().isNew() && httpRequest.getRequestURI().endsWith(url)) {
					session.invalidate();
					session = httpRequest.getSession(true);
				}
				for (Iterator<Map.Entry<String, Object>> it = old.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					session.setAttribute(entry.getKey(), entry.getValue());
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}

}
