package com.sxkl.cloudnote.utils;

import org.apache.commons.lang3.StringUtils;

public class MessageUtils {

    public static String setParamMessage(String paramMsg, String[] values) {
        if ((StringUtils.isEmpty(paramMsg)) || (values == null) || (values.length < 1)) {
            return "";
        }
        for (int i = 0; i < values.length; i++) {
            paramMsg = paramMsg.replaceFirst("\\{" + i + "\\}", values[i]);
        }
        return paramMsg;
    }
}