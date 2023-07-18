package com.zwc.filepicker

import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

/**
 * ClassName YNFilePicker
 * User: zuoweichen
 * Date: 2022/5/27 17:23
 * Description: 描述
 */
class YNFilePicker {
    private val mContext: WeakReference<FragmentActivity>

    constructor(activity: FragmentActivity) {
        mContext = WeakReference<FragmentActivity>(activity)
    }

    companion object {
        fun from(activity: FragmentActivity): YNFilePicker {
            return YNFilePicker(activity)
        }
    }
    fun build():SelectionCreator{
        return SelectionCreator(this)
    }


    fun getActivity(): FragmentActivity? {
        return mContext.get()
    }
}