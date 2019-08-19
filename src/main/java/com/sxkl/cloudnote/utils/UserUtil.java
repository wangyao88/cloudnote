package com.sxkl.cloudnote.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.user.entity.User;

public class UserUtil {

    public static User getSessionUser(HttpServletRequest request) {
        if (request != null) {
            HttpSession session = request.getSession();
            Object obj = session.getAttribute(Constant.USER_IN_SESSION_KEY);
            if (obj != null) {
                return (User) obj;
            }
        }
        return null;
    }

}
