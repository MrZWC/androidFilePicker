package com.zwc.filepicker.listener

import com.zwc.filepicker.entity.LocalMedia

/**
 * ClassName OnSelectListener
 * User: zuoweichen
 * Date: 2022/5/27 14:16
 * Description: 单个page选中接口
 */
interface OnPageFileSelectListener {
    /**
     *
     * @param data FileData
     * @return Boolean 当次选中是否生效
     */
    fun onSelect(data: LocalMedia): Boolean
    fun onUnSelect(data: LocalMedia)
}