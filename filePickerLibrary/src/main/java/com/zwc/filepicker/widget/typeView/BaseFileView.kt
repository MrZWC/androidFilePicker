package com.zwc.filepicker.widget.typeView

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.listener.OnPageFileSelectListener

/**
 * ClassName BaseFileView
 * User: zuoweichen
 * Date: 2022/5/23 17:42
 * Description: fileview 基类
 */
internal abstract class BaseFileView : FrameLayout {
    /**
     * 是否加载过 防止多次加载
     */
    private var isLoad = false
    abstract var emptyView: View?
    /*  */
    /**
     *当个界面的 选中的数据集合
     *//*
    private val selectDataList = ArrayList<FileData>()*/
    private var onPageFileSelectListener: OnPageFileSelectListener? = null

    constructor(context: Context) : super(context) {
        this.initView(context)
        this.initData()

    }

    abstract fun initView(context: Context)
    abstract fun initData()
    abstract fun loadData()

    /**
     * 通知数据显示更新
     */
    abstract fun notifyData(data: LocalMedia)

    fun load() {
        if (isLoad) {
            return
        }
        isLoad = true
        loadData()
    }

    /**
     * 失败后调用此方法
     */
    open fun loadFail() {
        isLoad = false
    }

    /**
     * 添加选中数据
     * @param localMedia FileData
     * @return Boolean 当次选中是否生效
     */
    protected fun addSelectData(localMedia: LocalMedia): Boolean {
        val select = onPageFileSelectListener?.onSelect(localMedia) ?: true
        /*  if (select) {
              selectDataList.add(fileData)
          }*/
        return select
    }

    /**
     * 移除选中数据
     * @param localMedia FileData
     */
    protected fun removeSelectData(localMedia: LocalMedia) {
        // selectDataList.remove(fileData)
        onPageFileSelectListener?.onUnSelect(localMedia)
    }

    fun setOnPageFileSelectListener(onPageFileSelectListener: OnPageFileSelectListener) {
        this.onPageFileSelectListener = onPageFileSelectListener
    }

    fun isLoad(): Boolean {
        return isLoad
    }

    fun showEmpty(isVisibility: Boolean) {
        emptyView?.visibility = if (isVisibility) View.VISIBLE else View.GONE
    }
}