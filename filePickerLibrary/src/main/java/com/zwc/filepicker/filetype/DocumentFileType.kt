package com.zwc.filepicker.filetype

import androidx.annotation.DrawableRes
import com.zwc.filepicker.R

/**
 * ClassName VideoFileType
 * User: zuoweichen
 * Date: 2022/5/20 16:32
 * Description: 适配过滤器
 */
object DocumentFileType : FileType() {

    override fun getFileFilter(): Array<String> {
        return arrayOf(
            "doc",
            "docx",
            "txt",
            "xt",
            "pdf",
            "xls",
            "xlsx",
            "cvs",
            "xml",
            "ppt",
            "pptx",
            "wps",
            "htm",
            "html",
            "rtf",
            "hlp",
            "ofd"
        )
    }

    override fun getFileTypeTitle(): String {
        return "文档"
    }

    @DrawableRes
    fun getDocumentImage(fileName: String): Int {
        when (getFileSuffix(fileName)) {
            "docx", "doc" -> {
                return R.mipmap.filepick_word
            }
            "xls", "xlsx" -> {
                return R.mipmap.filepick_excel
            }
            "pdf" -> {
                return R.mipmap.filepick_pdf
            }
            "ppt", "pptx" -> {
                return R.mipmap.filepick_ppt
            }
            "txt" -> {
                return R.mipmap.filepick_txt
            }
            "ofd" -> {
                return R.mipmap.filepick_ofd
            }
            else -> {
                return R.mipmap.filepick_unknow_file
            }
        }
    }
}