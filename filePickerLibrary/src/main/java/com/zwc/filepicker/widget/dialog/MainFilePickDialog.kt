package com.zwc.filepicker.widget.dialog

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.zwc.filepicker.R
import com.zwc.filepicker.SelectionSpec
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.filetype.*
import com.zwc.filepicker.listener.OnChooseListener
import com.zwc.filepicker.listener.OnPageFileSelectListener
import com.zwc.filepicker.utils.FileUtils
import com.zwc.filepicker.widget.FilePageAdapter
import com.zwc.filepicker.widget.FileTabEntity
import com.zwc.filepicker.widget.typeView.*
import com.zwc.viewdialog.ViewDialog

/**
 * ClassName MainFilePickDialog
 * User: zuoweichen
 * Date: 2022/5/30 15:46
 * Description: 描述
 */
internal class MainFilePickDialog : OnPageFileSelectListener {
    private var tabLayout: CommonTabLayout? = null
    private var backBtn: View? = null
    private var viewPager: ViewPager2? = null
    private var countText: TextView? = null
    private var confirmBtn: View? = null
    private var searchBtn: View? = null
    private var rootView: View? = null

    //
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private var adapter: FilePageAdapter? = null
    private val mFileViewData = ArrayList<BaseFileView>()

    /**
     * 选中的数据
     */
    private var mActivity: Activity
    private var mViewDialog: ViewDialog
    private val onChooseListener: OnChooseListener?

    constructor(activity: Activity, parentView: ViewGroup, onChooseListener: OnChooseListener?) {
        this.mActivity = activity
        this.onChooseListener = onChooseListener
        mViewDialog = ViewDialog.Builder(activity)
            .setParentView(parentView)
            .setCancelable(true)
            .setContentView(R.layout.pickfile_main_dialog)
            .create()
        initView(mViewDialog)
        initData()
        initListener()
    }

    private fun initView(viewDialog: ViewDialog) {
        backBtn = viewDialog.findViewById(R.id.back_btn)
        tabLayout = viewDialog.findViewById(R.id.tabLayout)
        viewPager = viewDialog.findViewById(R.id.view_pager)
        countText = viewDialog.findViewById(R.id.count_text)
        confirmBtn = viewDialog.findViewById(R.id.confirm_btn)
        searchBtn = viewDialog.findViewById(R.id.search_btn)
        rootView = viewDialog.findViewById(R.id.root_view)

    }

    private fun initData() {
        //禁止viewpager滑动
        //viewPager?.isUserInputEnabled = false
        SelectionSpec.getInstance().mimeTypeList?.let {
            for (fileType in it) {
                mTabEntities.add(FileTabEntity(fileType.getFileTypeTitle()))
                var fileView: BaseFileView? = null
                when (fileType) {
                    is ImageFileType -> {
                        fileView = ImageFileView(mActivity)
                    }
                    is AudioFileType -> {
                        fileView = AudioFileView(mActivity)
                    }
                    is VideoFileType -> {
                        fileView = VideoFileView(mActivity)
                    }
                    is DocumentFileType -> {
                        fileView = DocumentFileView(mActivity)
                    }
                    is RARFileType -> {
                        fileView = RARFileView(mActivity)

                    }
                    else -> {
                        fileView = CustomFileView(mActivity, fileType)
                    }
                }
                fileView.setOnPageFileSelectListener(this)
                mFileViewData.add(fileView)

            }
        }
        if (mTabEntities.size <= 1) {
            tabLayout?.visibility = View.GONE
        }
        tabLayout?.setTabData(mTabEntities)
        tabLayout?.currentTab = 0
        adapter = FilePageAdapter(mFileViewData)
        viewPager?.adapter = adapter
    }

    private fun initListener() {
        //事件占位
        rootView?.setOnClickListener {

        }
        backBtn?.setOnClickListener {
            hide()
        }

        tabLayout?.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager?.setCurrentItem(position, false)
                mFileViewData[position].load()
            }

            override fun onTabReselect(position: Int) {

            }

        })
        viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout?.currentTab = position
                mFileViewData[position].load()
            }
        })
        confirmBtn?.setOnClickListener {
            onChooseListener?.onChoose(SelectionSpec.getInstance().selectDataList)
            hide()
        }
        searchBtn?.setOnClickListener {
            val searchDialog =
                SearchDialog(mActivity, mActivity.findViewById(Window.ID_ANDROID_CONTENT))
            searchDialog.setOnPageFileSelectListener(object : OnPageFileSelectListener {
                override fun onSelect(data: LocalMedia): Boolean {
                    val add = this@MainFilePickDialog.addSelectData(data)
                    if (add) {
                        data.selected = true
                        notifyFileTypeView(data)
                    }
                    return add
                }

                override fun onUnSelect(data: LocalMedia) {
                    this@MainFilePickDialog.removeSelectData(data)
                    data.selected = false
                    notifyFileTypeView(data)
                }

            })
            searchDialog.show()
        }
    }

    fun show() {
        mViewDialog.show()
    }

    fun hide() {
        mViewDialog.hide(false)
    }

    //最终添加数据的地方
    private fun addSelectData(data: LocalMedia): Boolean {
        if (SelectionSpec.getInstance().selectDataList.size >= SelectionSpec.getInstance().maxSelectable) {
            Toast.makeText(mActivity, "已超过最大选择数量", Toast.LENGTH_SHORT).show()
            return false
        }
        if (SelectionSpec.getInstance().maxSize > -1 && data.size > SelectionSpec.getInstance().maxSize) {
            Toast.makeText(
                mActivity,
                "最大只能选择${FileUtils.FormatFileSize(SelectionSpec.getInstance().maxSize)}的文件",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        SelectionSpec.getInstance().selectDataList.add(data)
        countText?.text = SelectionSpec.getInstance().selectDataList.size.toString()
        return true
    }

    //最终删除数据的地方
    private fun removeSelectData(data: LocalMedia) {
        SelectionSpec.getInstance().selectDataList.remove(data)
        countText?.text = SelectionSpec.getInstance().selectDataList.size.toString()

    }

    /**
     * 更新filetype view
     * @param data FileData
     */
    private fun notifyFileTypeView(data: LocalMedia) {
        for (mFileViewDatum in mFileViewData) {
            mFileViewDatum.notifyData(data)
        }
    }

    override fun onSelect(data: LocalMedia): Boolean {
        return addSelectData(data)
    }

    override fun onUnSelect(data: LocalMedia) {
        removeSelectData(data)
    }
}