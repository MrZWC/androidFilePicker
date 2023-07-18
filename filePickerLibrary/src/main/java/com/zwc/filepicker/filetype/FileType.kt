package com.zwc.filepicker.filetype

/**
 * ClassName FileType
 * User: zuoweichen
 * Date: 2022/5/20 16:28
 * Description: 描述
 */
abstract class FileType {
    abstract fun getFileFilter(): Array<String>

    /**
     * filetab 显示的名称 如： 图片、视频
     * @return String
     */
    abstract fun getFileTypeTitle(): String
    open fun verify(fileName: String): Boolean {
        val suffix = getFileSuffix(fileName)
        if (getFileFilter().contains(suffix)) {
            return true
        }
        return false
    }

    /**
     * 获取文件后缀 并统一转为小写
     * @param fileName String
     * @return String
     */
    fun getFileSuffix(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf(".") + 1).lowercase()
    }
}