package com.zwc.filepicker.filetype

/**
 * ClassName VideoFileType
 * User: zuoweichen
 * Date: 2022/5/20 16:32
 * Description: 适配过滤器
 */
object ImageFileType : FileType() {
    override fun getFileFilter(): Array<String> {
        return arrayOf(
            "jpg",
            "jpeg",
            "png",
            "gif",
            "bmp",
            "psd",
            "tif",
            "jfif",
        )
    }

    override fun getFileTypeTitle(): String {
        return "图片"
    }
}