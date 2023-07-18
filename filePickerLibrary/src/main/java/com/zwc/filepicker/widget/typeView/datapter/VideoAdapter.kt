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

/**
 * ClassName ImageAdapter
 * User: zuoweichen
 * Date: 2022/5/26 14:40
 * Description: 描述
 */
class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VH> {
    private val mContext: Context
    private val mData: ArrayList<LocalMedia>

    constructor(context: Context, data: ArrayList<LocalMedia>) : super() {
        mContext = context
        this.mData = data
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImage: ImageView

        init {
            mImage = itemView.findViewById(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(mContext).inflate(R.layout.item_video_file_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Glide.with(mContext).load(mData[position].path).centerCrop().into(holder.mImage)
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}