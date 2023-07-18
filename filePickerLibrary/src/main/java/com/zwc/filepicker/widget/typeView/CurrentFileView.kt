package com.zwc.filepicker.widget.typeView

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.zwc.filepicker.FileLoaderManager
import com.zwc.filepicker.R
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.entity.LocalFileData
import com.zwc.filepicker.filetype.FileType
import com.zwc.filepicker.listener.OnRecyclerViewPreloadMoreListener
import com.zwc.filepicker.listener.OnItemSelectListener
import com.zwc.filepicker.utils.DensityUtil
import com.zwc.filepicker.widget.RecyclerPreloadView
import com.zwc.filepicker.widget.typeView.datapter.FileAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

/**
 * ClassName CustomFileView
 * User: zuoweichen
 * Date: 2022/5/27 10:44
 * Description: 通用 用于有刷新的列表展示
 */
internal abstract class CurrentFileView(context: Context) : BaseFileView(context),
    OnRecyclerViewPreloadMoreListener {
    private var mRecyclerView: RecyclerPreloadView? = null
    private lateinit var mData: ArrayList<LocalMedia>
    private var page = 1
    private var pageSize = 30
    private var mAdapter: FileAdapter? = null
    override var emptyView: View? = null
    override fun initView(context: Context) {
        inflate(context, R.layout.widget_file_layout, this)
        mRecyclerView = findViewById(R.id.recycler_view)
        emptyView = findViewById(R.id.empty_view)
    }

    override fun initData() {
        setOf<LocalMedia>().apply {

        }
        mData = ArrayList()
        mAdapter = FileAdapter(context, mData)
        mRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            setEnabledLoadMore(true)
            setOnRecyclerViewPreloadListener(this@CurrentFileView)
            addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val position = parent.getChildAdapterPosition(view)
                    if (position == 0) {
                        outRect.top = DensityUtil.dip2px(context, 20f)
                    } else {
                        outRect.top = DensityUtil.dip2px(context, 10f)
                        outRect.bottom = DensityUtil.dip2px(context, 10f)
                    }
                }
            })
            adapter = mAdapter
        }
        mAdapter?.apply {
            setOnSelectListener(object : OnItemSelectListener {
                override fun onSelect(select: Boolean, data: LocalMedia, position: Int) {
                    if (select) {
                        if (addSelectData(data)) {
                            data.selected = select
                            mAdapter?.notifyItemChanged(position)
                        }
                    } else {
                        removeSelectData(data)
                        data.selected = false
                        mAdapter?.notifyItemChanged(position)
                    }
                }
            })
        }
    }

    override fun loadData() {
        Observable.just("")
            .map {
                return@map FileLoaderManager().load(
                    context,
                    arrayListOf(initFileType()),
                    page,
                    pageSize
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
                Timber.e(it)
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
        mAdapter?.notifyDataSetChanged()
    }

    /**
     * 加载更多
     */
    override fun onRecyclerViewPreloadMore() {
        page++
        loadData()
    }

    /**
     * 返回要加载的filetype
     * @return FileType
     */
    abstract fun initFileType(): FileType

    override fun notifyData(data: LocalMedia) {
        //如果没有加载过 就直接返回
        if (!isLoad()) {
            return
        }
        if (mData.contains(data)) {
            val indexOf = mData.indexOf(data)
            mData[indexOf].selected = data.selected
            mAdapter?.notifyItemChanged(indexOf)
        }
    }
}