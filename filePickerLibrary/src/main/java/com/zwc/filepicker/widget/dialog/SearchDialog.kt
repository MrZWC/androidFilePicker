package com.zwc.filepicker.widget.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding4.widget.textChanges
import com.zwc.filepicker.FileLoaderManager
import com.zwc.filepicker.R
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.listener.OnItemSelectListener
import com.zwc.filepicker.listener.OnPageFileSelectListener
import com.zwc.filepicker.utils.DensityUtil
import com.zwc.filepicker.widget.empty.EmptyView
import com.zwc.filepicker.widget.typeView.datapter.FileAdapter
import com.zwc.viewdialog.ViewDialog
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * ClassName SearchDialog
 * User: zuoweichen
 * Date: 2022/5/27 15:20
 * Description: 描述
 */
class SearchDialog {
    private var cancelBtn: View? = null
    private var rootView: View? = null
    private var mRecyclerView: RecyclerView? = null
    private var emptyView: EmptyView? = null
    private var editText: EditText? = null

    ///
    private var mActivity: Activity
    private var mViewDialog: ViewDialog
    private var mAdapter: FileAdapter? = null
    private val mData = ArrayList<LocalMedia>()
    private var onPageFileSelectListener: OnPageFileSelectListener? = null

    constructor(activity: Activity, parentView: ViewGroup) {
        this.mActivity = activity
        mViewDialog = ViewDialog.Builder(activity)
            .setParentView(parentView)
            .setCancelable(true)
            .setOnHideListener {
                closeKeyboard()
            }.setOnShowListener {
                editText?.isFocusable = true
                editText?.isFocusableInTouchMode = true
                editText?.requestFocus()
                editText?.post {
                    editText?.apply {
                        showKeyboard(mActivity, this)
                    }
                }
            }
            .setContentView(R.layout.filepick_widget_search_layout)
            .create()
        initView(mViewDialog)
        initData()
        initListener()
    }

    private fun initView(viewDialog: ViewDialog) {
        editText = viewDialog.findViewById(R.id.edit_text)
        cancelBtn = viewDialog.findViewById(R.id.cancel_btn)
        mRecyclerView = viewDialog.findViewById(R.id.recycler_view)
        emptyView = viewDialog.findViewById(R.id.empty_view)
        rootView = viewDialog.findViewById(R.id.root_view)
    }

    private fun initData() {
        mAdapter = FileAdapter(mActivity, mData)
        mRecyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            addItemDecoration(object : RecyclerView.ItemDecoration() {
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
    }

    private fun initListener() {
        //占位点击
        rootView?.setOnClickListener {
        }
        editText?.let { editText1 ->
            editText1.textChanges().debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isNotEmpty()) {
                        searchData(it.toString())
                    }
                }, {
                    Timber.e(it)
                })
        }
        cancelBtn?.setOnClickListener {
            hide()
        }
        mAdapter?.apply {
            setOnSelectListener(object : OnItemSelectListener {
                override fun onSelect(select: Boolean, data: LocalMedia, position: Int) {
                    if (select) {
                        if (onPageFileSelectListener?.onSelect(data) != false) {
                            data.selected = select
                            mAdapter?.notifyItemChanged(position)
                        }
                    } else {
                        onPageFileSelectListener?.onUnSelect(data)
                        data.selected = false
                        mAdapter?.notifyItemChanged(position)
                    }
                }
            })
        }
    }

    private fun searchData(keyWords: String) {
        Observable.just(keyWords)
            .map {
                return@map FileLoaderManager().load(mActivity, it)
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mData.clear()
                mData.addAll(it.getData())
                mAdapter?.notifyDataSetChanged()
                if (it.getData().size == 0) {
                    showEmpty(true)
                    return@subscribe
                } else {
                    showEmpty(false)
                }
            }, {
                Timber.e(it)
                showEmpty(true)
            })
    }

    fun show() {
        mViewDialog.show()
    }

    fun hide() {
        mViewDialog.hide(false)
    }

    fun setOnPageFileSelectListener(onPageFileSelectListener: OnPageFileSelectListener) {
        this.onPageFileSelectListener = onPageFileSelectListener
    }

    /**
     * 隐藏键盘
     */
    private fun closeKeyboard() {
        val view = mActivity.window.peekDecorView()
        val inputMethodManager =
            mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 显示软件盘
     *
     * @param context
     * @param editText
     */
    fun showKeyboard(context: Context, editText: EditText) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, 0)
    }

    private fun showEmpty(isVisibility: Boolean) {
        emptyView?.visibility = if (isVisibility) View.VISIBLE else View.GONE
    }
}