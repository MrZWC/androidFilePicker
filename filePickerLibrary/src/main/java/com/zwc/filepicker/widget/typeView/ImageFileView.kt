package com.zwc.filepicker.widget.typeView

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.zwc.filepicker.FileLoaderManager
import com.zwc.filepicker.R
import com.zwc.filepicker.config.PictureConfig
import com.zwc.filepicker.entity.LocalFileData
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.filetype.ImageFileType
import com.zwc.filepicker.listener.OnImageItemSelectListener
import com.zwc.filepicker.listener.OnRecyclerViewPreloadMoreListener
import com.zwc.filepicker.observable.ImagesObservable
import com.zwc.filepicker.utils.JumpUtils
import com.zwc.filepicker.widget.RecyclerPreloadView
import com.zwc.filepicker.widget.decoration.DividerGridItemDecoration
import com.zwc.filepicker.widget.typeView.datapter.ImageAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * ClassName ImageFileView
 * User: zuoweichen
 * Date: 2022/5/23 17:53
 * Description: 描述
 */
internal class ImageFileView(context: Context) : BaseFileView(context),
    OnRecyclerViewPreloadMoreListener {

    private var mRecyclerView: RecyclerPreloadView? = null
    private lateinit var mData: ArrayList<LocalMedia>
    private var page = 1
    private var pageSize = 30
    private var imageAdapter: ImageAdapter? = null
    override var emptyView: View? = null
    private val request_code = 10002
    override fun initView(context: Context) {
        inflate(context, R.layout.widget_file_layout, this)
        mRecyclerView = findViewById(R.id.recycler_view)
        emptyView = findViewById(R.id.empty_view)
    }

    override fun initData() {
        mData = ArrayList()
        imageAdapter = ImageAdapter(context, mData)
        mRecyclerView?.apply {
            layoutManager = GridLayoutManager(context, 4)
            itemAnimator = null
            setEnabledLoadMore(true)
            addItemDecoration(DividerGridItemDecoration(context,1f))
            setOnRecyclerViewPreloadListener(this@ImageFileView)
            adapter = imageAdapter
        }
        imageAdapter?.setOnSelectListener(object : OnImageItemSelectListener {
            override fun onSelect(select: Boolean, data: LocalMedia, position: Int) {
                if (select) {
                    if (addSelectData(data)) {
                        data.selected = select
                        imageAdapter?.notifyItemChanged(position)
                    }
                } else {
                    removeSelectData(data)
                    data.selected = false
                    imageAdapter?.notifyItemChanged(position)
                }
            }

            override fun onPreClick(data: LocalMedia, position: Int) {
                ImagesObservable.getInstance()
                    .savePreviewMediaData(ArrayList(mData))
                val bundle = Bundle()
                bundle.putInt(PictureConfig.EXTRA_POSITION, position)
                bundle.putInt(PictureConfig.EXTRA_PAGE, page)
                JumpUtils.startPicturePreviewActivity(
                    context,
                    bundle,
                    request_code
                )
            }

        })
    }

    override fun loadData() {
        Observable.just("")
            .map {
                return@map FileLoaderManager().load(
                    context,
                    arrayListOf(ImageFileType),
                    page,
                    pageSize,
                )
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (page == 1) {
                    if (it.getData().size == 0) {
                        showEmpty(true)
                        return@subscribe
                    } else {
                        showEmpty(false)
                    }
                }
                showEmpty(false)
                showData(it)
            }, {
                //加载失败后
                loadFail()
                if (page == 1) {
                    showEmpty(true)
                }
            })
    }

    private fun showData(localFileData: LocalFileData) {
        mData.addAll(localFileData.getData())
        if (!localFileData.isHasNextMore()) {
            mRecyclerView?.setEnabledLoadMore(false)
        }
        imageAdapter?.notifyDataSetChanged()
    }

    /**
     * 加载更多
     */
    override fun onRecyclerViewPreloadMore() {
        page++
        loadData()
    }

    override fun notifyData(data: LocalMedia) {
        //如果没有加载过 就直接返回
        if (!isLoad()) {
            return
        }
        if (mData.contains(data)) {
            val indexOf = mData.indexOf(data)
            mData[indexOf].selected = data.selected
            imageAdapter?.notifyItemChanged(mData.indexOf(data))
        }
    }
}