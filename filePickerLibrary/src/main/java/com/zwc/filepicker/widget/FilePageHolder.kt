package com.zwc.filepicker.widget

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * ClassName FilePageHoler
 * User: zuoweichen
 * Date: 2022/5/25 15:40
 * Description: 描述
 */
class FilePageHolder : RecyclerView.ViewHolder {
    constructor(itemView: View) : super(itemView)

    companion object {
        fun create(parent: ViewGroup): FilePageHolder {
            val container = FrameLayout(parent.context)
            container.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            container.id = ViewCompat.generateViewId()
            container.isSaveEnabled = false
            return FilePageHolder(container)
        }

    }

    fun getContainer(): FrameLayout {
        return itemView as FrameLayout
    }
}