package com.zwc.filepicker.filetype

/**
 * ClassName VideoFileType
 * User: zuoweichen
 * Date: 2022/5/20 16:32
 * Description: 适配过滤器
 */
object VideoFileType : FileType() {
    override fun getFileFilter(): Array<String> {
        return arrayOf(
            "mkv",
            "mp4",
            "avi",
            "swf",
            "mov",
            "wmv",
            "rmvb",
            "mov",
            "mpg",
            "mpeg",
            "3gp",
            "flv",
            "f4v",
        )
    }

    override fun getFileTypeTitle(): String {
        return "视频"
    }
}