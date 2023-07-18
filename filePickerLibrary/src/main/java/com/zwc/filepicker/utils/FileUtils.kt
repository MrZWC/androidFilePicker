package com.zwc.filepicker.utils

import java.text.DecimalFormat

/**
 * ClassName FileUtils
 * User: zuoweichen
 * Date: 2022/5/27 13:44
 * Description: 描述
 */
internal object FileUtils {
    fun FormatFileSize(fileSize: Long): String? {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileSize == 0L) {
            return wrongSize
        }
        fileSizeString = if (fileSize < 1024) {
            df.format(fileSize.toDouble()) + "B"
        } else if (fileSize < 1048576) {
            df.format(fileSize.toDouble() / 1024) + "KB"
        } else if (fileSize < 1073741824) {
            df.format(fileSize.toDouble() / 1048576) + "MB"
        } else {
            df.format(fileSize.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }
}