package com.zwc.filepicker.widget

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zwc.filepicker.widget.typeView.BaseFileView

/**
 * ClassName FilePageAdapter
 * User: zuoweichen
 * Date: 2022/5/25 15:14
 * Description: 描述
 */
internal class FilePageAdapter : RecyclerView.Adapter<FilePageHolder> {
    private val mData: ArrayList<BaseFileView>

    constructor(mData: ArrayList<BaseFileView>) : super() {
        this.mData = mData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilePageHolder {
        return FilePageHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FilePageHolder, position: Int) {
        holder.getContainer().removeAllViews()
        holder.getContainer().addView(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}