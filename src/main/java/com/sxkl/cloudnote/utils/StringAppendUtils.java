package com.sxkl.cloudnote.utils;

/**
 * Created by wangyao
 * Date 2017/12/6.
 */
public class StringAppendUtils {

    public static String append(Object... objs) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : objs) {
            builder.append(String.valueOf(obj));
        }
        return builder.toString();
    }
}
