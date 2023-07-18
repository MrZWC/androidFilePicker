package com.zwc.filepicker.listener

import com.zwc.filepicker.entity.LocalMedia

/**
 * ClassName OnSelectListener
 * User: zuoweichen
 * Date: 2022/5/27 14:16
 * Description: item选中接口
 */
interface OnItemSelectListener {
    fun onSelect(select: Boolean, data: LocalMedia, position: Int)
}