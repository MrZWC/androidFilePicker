package com.zwc.filepicker.listener

import com.zwc.filepicker.entity.LocalMedia

/**
 * ClassName OnSelectListener
 * User: zuoweichen
 * Date: 2022/5/27 14:16
 * Description: item选中接口
 */
interface OnImageItemSelectListener {
    /**
     * 选中点击
     * @param select Boolean
     * @param data FileData
     * @param position Int
     */
    fun onSelect(select: Boolean, data: LocalMedia, position: Int)

    /**
     * 预览点击
     * @param data FileData
     * @param position Int
     */
    fun onPreClick(data: LocalMedia, position: Int)
}