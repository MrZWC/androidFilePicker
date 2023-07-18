package com.zwc.filepicker.widget.typeView.datapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zwc.filepicker.R
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.listener.OnImageItemSelectListener

/**
 * ClassName ImageAdapter
 * User: zuoweichen
 * Date: 2022/5/26 14:40
 * Description: 描述
 */
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.VH> {
    private val mContext: Context
    private val mData: ArrayList<LocalMedia>

    constructor(context: Context, data: ArrayList<LocalMedia>) : super() {
        mContext = context
        this.mData = data
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImage: ImageView
        val selectBt: View
        val selectImage: ImageView
        init {
            mImage = itemView.findViewById(R.id.image)
            selectBt = itemView.findViewById(R.id.select_bt)
            selectImage = itemView.findViewById(R.id.select_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(mContext).inflate(R.layout.item_image_file_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = mData[position]
        Glide.with(mContext).load(data.path).centerCrop().into(holder.mImage)
        if (data.selected) {
            holder.selectImage.setImageResource(R.mipmap.filepick_icon_selected)
        } else {
            holder.selectImage.setImageResource(R.mipmap.filepick_icon_un_selected)
        }
        holder.selectBt.setOnClickListener {
            onSelectListener?.onSelect(!data.selected, data, position)
        }
        holder.itemView.setOnClickListener {
            onSelectListener?.onPreClick(data, position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    private var onSelectListener: OnImageItemSelectListener? = null

    fun setOnSelectListener(onSelectListener: OnImageItemSelectListener) {
        this.onSelectListener = onSelectListener
    }
}