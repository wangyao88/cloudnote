package com.sxkl.cloudnote.utils;

import javax.servlet.http.HttpServletRequest;

public class ServletHelp {

	public static String getRealPath(HttpServletRequest request, String virtualPath) {
		return request.getSession().getServletContext().getRealPath(virtualPath);
	}

}
