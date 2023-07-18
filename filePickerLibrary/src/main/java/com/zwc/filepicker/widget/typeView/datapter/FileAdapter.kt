package com.zwc.filepicker.widget.typeView.datapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zwc.filepicker.R
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.filetype.*
import com.zwc.filepicker.listener.OnItemSelectListener
import com.zwc.filepicker.utils.DateUtils
import com.zwc.filepicker.utils.FileUtils

/**
 * ClassName ImageAdapter
 * User: zuoweichen
 * Date: 2022/5/26 14:40
 * Description: 描述
 */
class FileAdapter : RecyclerView.Adapter<FileAdapter.VH> {
    private val mContext: Context
    private val mData: ArrayList<LocalMedia>

    constructor(context: Context, data: ArrayList<LocalMedia>) : super() {
        mContext = context
        this.mData = data
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mImage: ImageView
        val title: TextView
        val tipsText: TextView
        val sizeText: TextView
        val selectBt: View
        val selectImage: ImageView


        init {
            mImage = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            tipsText = itemView.findViewById(R.id.tips_text)
            sizeText = itemView.findViewById(R.id.size_text)
            selectBt = itemView.findViewById(R.id.select_bt)
            selectImage = itemView.findViewById(R.id.select_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(mContext).inflate(R.layout.item_file_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = mData[position]
        val displayName = data.displayName
        holder.title.text = displayName
        holder.tipsText.text =
            DateUtils.getDateByMillisecond(data.addTime, DateUtils.FORMAT_DATETIME_24_mic)
        if (AudioFileType.verify(displayName)) {
            holder.mImage.setImageResource(R.mipmap.filepick_music)
        } else if (VideoFileType.verify(displayName)) {
            Glide.with(mContext).load(data.path).centerCrop()
                .placeholder(R.mipmap.filepick_video).into(holder.mImage)
        } else if (DocumentFileType.verify(displayName)) {
            holder.mImage.setImageResource(DocumentFileType.getDocumentImage(displayName))
        } else if(ImageFileType.verify(displayName)){
            Glide.with(mContext).load(data.path).centerCrop().into(holder.mImage)
        }else if(RARFileType.verify(displayName)){
            holder.mImage.setImageResource(R.mipmap.filepick_rar)
        }else {
            holder.mImage.setImageResource(R.mipmap.filepick_unknow_file)
        }
        holder.sizeText.text = FileUtils.FormatFileSize(data.size)
        if (data.selected) {
            holder.selectImage.setImageResource(R.mipmap.filepick_icon_selected)
        } else {
            holder.selectImage.setImageResource(R.mipmap.filepick_icon_un_selected)
        }
        holder.itemView.setOnClickListener {
            onSelectListener?.onSelect(!data.selected, data, position)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    private var onSelectListener: OnItemSelectListener? = null

    fun setOnSelectListener(onSelectListener: OnItemSelectListener) {
        this.onSelectListener = onSelectListener
    }
}