package com.zwc.filepicker.filetype

/**
 * ClassName VideoFileType
 * User: zuoweichen
 * Date: 2022/5/20 16:32
 * Description: 适配过滤器
 */
object AudioFileType : FileType() {
    override fun getFileFilter(): Array<String> {
        return arrayOf(
            "mp3",
            "flac",
            "ape",
            "wma",
            "wav",
            "aac",
            "m4a",
            "au",
            "ram",
            "mmf",
            "aif",
        )
    }

    override fun getFileTypeTitle(): String {
        return "音频"
    }
}