package com.zwc.filepicker.widget.empty

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.zwc.filepicker.R

/**
 * ClassName EmptyView
 * User: zuoweichen
 * Date: 2022/6/1 10:54
 * Description: 描述
 */
internal class EmptyView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        inflate(context, R.layout.filepick_empty_layout, this)
    }
}