package com.sxkl.cloudnote.love.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.DateUtils;
import com.sxkl.cloudnote.utils.StringUtils;

/**
 * @author: wangyao
 * @date: 2018年5月18日 下午5:18:14
 * @description:
 */
@Service
public class LoveService {

    @Logger(message = "获取love页面名称")
    public String getLovePageName(HttpServletRequest request) {
        String key = request.getParameter("key");
        String year = DateUtils.getYear();
        if (StringUtils.isEmpty(key)) {
            key = DateUtils.getNowMonthDay(DateUtils.DATE_PATTON_5);
            if (key.startsWith("0")) {
                key = key.substring(1);
            }
            key = "520";
        }
        return StringUtils.appendJoinFolderSeparator("love", year, key, key);
    }
}
