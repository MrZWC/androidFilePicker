package com.zwc.filepicker.utils;

import java.util.regex.Pattern;

/**
 * Created by Android Studio.
 * User: zuoweichen
 * Date: 2020/8/31
 * Time: 14:47
 */
public class StringUtils {
    /**
     * 匹配数值
     *
     * @param str
     * @return
     */
    public static int stringToInt(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches() ? Integer.valueOf(str) : 0;
    }
}
