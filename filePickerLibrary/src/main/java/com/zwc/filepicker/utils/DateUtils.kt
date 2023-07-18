package com.zwc.filepicker.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * ClassName DateUtils
 * User: zuoweichen
 * Date: 2022/5/27 10:21
 * Description: 描述
 */
internal object DateUtils {
    /**
     * "yyyy-MM-dd HH:mm:ss.SSS" 24小时制,含有毫秒
     */
    const val FORMAT_DATETIME_24_mic = "yyyy-MM-dd HH:mm:ss"
    fun getDateByMillisecond(millionSeconds: Long, dateFormat: String): String {
        val sDateFormat = SimpleDateFormat(dateFormat, Locale.CHINA)
        val curDateTime = Date(millionSeconds)
        return sDateFormat.format(curDateTime)
    }
}