package com.sxkl.cloudnote.editor.define;

import java.util.Map;

import org.apache.curator.shaded.com.google.common.collect.Maps;

public class FileType {

    public static final String JPG = "JPG";

    private static final Map<String, String> types = Maps.newHashMap();

    static {
        types.put(JPG, JPG);
    }

    public static String getSuffix(String key) {
        return (String) types.get(key);
    }

    public static String getSuffixByFilename(String filename) {
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }
}
