package com.zwc.filepicker

import android.view.Window
import androidx.annotation.IntRange
import com.zwc.filepicker.filetype.*
import com.zwc.filepicker.listener.OnChooseListener
import com.zwc.filepicker.widget.dialog.MainFilePickDialog

/**
 * ClassName SelectionCreator
 * User: zuoweichen
 * Date: 2022/5/27 17:30
 * Description: 描述
 */
class SelectionCreator {
    private var onChooseListener: OnChooseListener? = null
    private val mSelectionSpec: SelectionSpec
    private val mYNFilePicker: YNFilePicker

    constructor(ynFilePicker: YNFilePicker) {
        this.mSelectionSpec = SelectionSpec.getCleanInstance()
        this.mYNFilePicker = ynFilePicker

    }

    /**
     * 设置最大选择数量 不设置默认为1 ，最大选择数量为20（产品设计）
     * @param maxSelectable Int
     * @return SelectionCreator
     */
    fun maxSelectable(maxSelectable: Int): SelectionCreator {
        val tempMaxSelectable = if (maxSelectable > 20) {
            20
        } else if (maxSelectable < 1) {
            1
        } else {
            maxSelectable
        }
        mSelectionSpec.maxSelectable = tempMaxSelectable
        return this
    }

    /**
     * 选择单个文件的最大 文件大小
     * @param maxSize Long 字节 默认为-1（表示不限制）maxSize传入参数必须大于0
     * @return SelectionCreator
     */
    fun maxFileSize(@IntRange(from = 1) maxSize: Long): SelectionCreator {
        mSelectionSpec.maxSize = maxSize
        return this
    }

    /**
     *
     * 如果没有设置选择类型就默认设置为选择全部
     * @param mimeTypes ArrayList<FileType> 要选择的文件类型 显示顺序和mimeTypes数组顺序一致
     * @return SelectionCreator
     */
    fun choose(mimeTypes: ArrayList<FileType>): SelectionCreator {
        mSelectionSpec.mimeTypeList = mimeTypes
        return this
    }
    fun setOnChooseListener(onChooseListener: OnChooseListener): SelectionCreator {
        this.onChooseListener = onChooseListener
        return this
    }

    fun launch() {
        /**
         * 如果没有设置选择类型就默认设置为选择全部
         */
        if (mSelectionSpec.mimeTypeList.isNullOrEmpty()) {
            mSelectionSpec.mimeTypeList = arrayListOf(
                ImageFileType,
                AudioFileType,
                VideoFileType,
                DocumentFileType,
                RARFileType
            )
        }
        mYNFilePicker.getActivity()?.let {
            val dialog =
                MainFilePickDialog(it, it.findViewById(Window.ID_ANDROID_CONTENT), onChooseListener)
            dialog.show()
        }

    }
}