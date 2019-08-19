package com.sxkl.cloudnote.utils;

import java.util.Date;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * @author: wangyao
 * @date: 2018年5月15日 上午11:25:35
 * @description:
 */
public class JSONObjectUtils {

    @SuppressWarnings("static-access")
    public static JSONObject toJson(Object obj) {
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
        JSONObject json = new JSONObject();
        return json.fromObject(obj, jsonConfig);
    }
}
