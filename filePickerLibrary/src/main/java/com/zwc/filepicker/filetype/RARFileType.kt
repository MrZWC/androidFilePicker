package com.zwc.filepicker.filetype

/**
 * ClassName VideoFileType
 * User: zuoweichen
 * Date: 2022/5/20 16:32
 * Description: 适配过滤器
 */
object RARFileType : FileType() {
    override fun getFileFilter(): Array<String> {
        return arrayOf(
            "rar", "zip", "7z", "gz", "arj", "z"
        )
    }

    override fun getFileTypeTitle(): String {
        return "压缩包"
    }
}