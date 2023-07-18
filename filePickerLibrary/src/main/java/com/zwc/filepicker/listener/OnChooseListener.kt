package com.zwc.filepicker.listener

import com.zwc.filepicker.entity.LocalMedia

/**
 * ClassName OnChooseListener
 * User: zuoweichen
 * Date: 2022/5/30 16:05
 * Description: 描述
 */
interface OnChooseListener {
    fun onChoose(data: ArrayList<LocalMedia>)
}