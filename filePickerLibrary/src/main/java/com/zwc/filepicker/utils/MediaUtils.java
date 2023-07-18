package com.zwc.filepicker.utils;


import com.zwc.filepicker.entity.LocalMedia;

/**
 * Created by Android Studio.
 * User: zuoweichen
 * Date: 2020/8/29
 * Time: 15:32
 */
public class MediaUtils {
    /**
     * 是否是长图
     *
     * @param media
     * @return true 是 or false 不是
     */
    public static boolean isLongImg(LocalMedia media) {
        if (null != media) {
            int width = media.getWidth();
            int height = media.getHeight();
            int newHeight = width * 3;
            return height > newHeight;
        }
        return false;
    }
}
