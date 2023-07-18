package com.zwc.filepicker.widget.typeView

import android.content.Context
import com.zwc.filepicker.filetype.DocumentFileType
import com.zwc.filepicker.filetype.FileType

/**
 * ClassName ImageFileView
 * User: zuoweichen
 * Date: 2022/5/23 17:53
 * Description: 描述
 */
internal class DocumentFileView(context: Context) : CurrentFileView(context) {
    override fun initFileType(): FileType {
        return DocumentFileType
    }
}