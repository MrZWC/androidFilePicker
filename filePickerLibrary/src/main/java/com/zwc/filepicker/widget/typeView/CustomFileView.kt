package com.zwc.filepicker.widget.typeView

import android.content.Context
import com.zwc.filepicker.filetype.FileType

/**
 * ClassName CustomFileView
 * User: zuoweichen
 * Date: 2022/6/1 10:22
 * Description: 自定义view
 */
internal class CustomFileView(context: Context, val fileType: FileType) : CurrentFileView(context) {

    override fun initFileType(): FileType {
        return fileType
    }
}
