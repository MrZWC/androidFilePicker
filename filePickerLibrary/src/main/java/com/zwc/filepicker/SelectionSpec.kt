package com.zwc.filepicker

import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.filetype.FileType

/**
 * ClassName SelectionSpec
 * User: zuoweichen
 * Date: 2022/5/27 17:33
 * Description: 选择器 配置管理
 */
class SelectionSpec {
    var mimeTypeList: ArrayList<FileType>? = null
    var maxSelectable = 1

    /**
     * 当为-1时不限制
     */
    var maxSize = -1L

    /**
     * 选中的数据
     */
    val selectDataList = ArrayList<LocalMedia>()

    companion object {
        fun getInstance(): SelectionSpec {
            return Holder.instance
        }

        fun getCleanInstance(): SelectionSpec {
            val instance = getInstance()
            instance.reset()
            return instance
        }
    }

    private object Holder {
        val instance = SelectionSpec()
    }

    private fun reset() {
        mimeTypeList = null
        maxSelectable = 1
        selectDataList.clear()
    }
}