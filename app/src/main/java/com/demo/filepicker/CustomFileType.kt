package com.demo.filepicker

import com.zwc.filepicker.filetype.FileType

/**
 * ClassName CustomFileType
 * User: zuoweichen
 * Date: 2022/6/1 10:13
 * Description: 描述
 */
class CustomFileType : FileType() {
    override fun getFileFilter(): Array<String> {
        return arrayOf(
            /* "mp4", "rar", "docx"*/
            /*"mp4","mp3","zip","rar","doc","docx","txt","pdf"*/
            "docx","pdf"
        )
    }

    override fun getFileTypeTitle(): String {
        return "更多"
    }
}