package com.zwc.filepicker.widget.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.zwc.filepicker.utils.DensityUtil;


/**
 * calculateSpeedPerPixel
 *
 * @author zhy
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private float height;
    private Context context;

    /**
     * @param resId  分割线颜色id
     * @param height 分割线宽度
     * @author zuo
     * created at 2017/3/3 下午4:56
     **/
    public DividerGridItemDecoration(Context context, int resId, float height) {
        if (context == null) {
            return;
        }
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        //mDivider = a.getDrawable(0);
        mDivider = new ColorDrawable(context.getResources().getColor(resId));
        this.height = height;
        this.context = context;
        a.recycle();

    }

    /**
     * @param height 分割线宽度
     * @作者 zuo
     * @创建时间 2018/9/26 16:12
     **/
    public DividerGridItemDecoration(Context context, float height) {
        if (context == null) {
            return;
        }
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        //mDivider = a.getDrawable(0);
        mDivider = new ColorDrawable();
        this.height = height;
        this.context = context;
        a.recycle();

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);

    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getAdapter().getItemCount();
        int lineWidth = DensityUtil.dip2px(context, height);
        /*GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        int SpanCount = manager.getSpanCount();*/
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                return;
            }
            final int left = child.getLeft() + child.getWidth();
            final int top = child.getTop();
            final int right = child.getRight() + lineWidth;
            final int bottom = child.getBottom();
            mDivider.setBounds(left, top, right, bottom);
            //mDivider.setBounds(left+10, top+10, right+10, bottom+10);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getAdapter().getItemCount();
        int lineWidth = DensityUtil.dip2px(context, height);
       /* GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        int SpanCount = manager.getSpanCount();*/
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (child == null) {
                return;
            }
            final int left = child.getLeft();
            final int top = child.getTop() + child.getHeight();
            final int right = child.getRight() + lineWidth;
            final int bottom = child.getBottom() + lineWidth;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //判断是否是最后一列
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    //是否是第一列
    private boolean isFirstColum(RecyclerView parent, int pos, int spanCount,
                                 int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 1) {// 如果是第一列，则不需要绘制左边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 1)// 如果是第一列，则不需要绘制左边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos <= 1)// 如果是第一列，则不需要绘制左边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = (int) Math.ceil(childCount * 1.0 / spanCount);
            pos = (int) Math.ceil((pos + 1) * 1.0 / spanCount);
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    //是否是第一行
    private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount,
                               int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = (int) Math.ceil(childCount * 1.0 / spanCount);
            pos = (int) Math.ceil(((pos + 1) * 1.0 / spanCount));
            if (pos <= 1)// 如果是第一行，则不需要绘制顶部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是第一行，则不需要绘制顶部
                pos = (int) Math.ceil(((pos + 1) / (spanCount * 1.0)));
                if (pos <= 1)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是第一行，则不需要绘制顶部
                if ((pos + 1) % spanCount == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int itemPosition = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int lineWidth = DensityUtil.dip2px(context, height) / 2;
        //行
        boolean isFirstRaw = isFirstRaw(parent, itemPosition, spanCount, childCount);
        boolean isLastRaw = isLastRaw(parent, itemPosition, spanCount, childCount);
        //列
        boolean isFirstColum = isFirstColum(parent, itemPosition, spanCount, childCount);
        boolean isLastColum = isLastColum(parent, itemPosition, spanCount, childCount);
        if (isFirstRaw && isLastRaw) {//如果是第一行 也是最后一行
            if (isFirstColum && isLastColum) {//如果是第一列 也是最后一列
                outRect.set(0, 0, 0, 0);
            } else if (isLastColum) {//如果是最后一列
                outRect.set(lineWidth, 0, 0, 0);
            } else if (isFirstColum) {//如果是第一列
                outRect.set(0, 0, lineWidth, 0);
            } else {//
                outRect.set(lineWidth, 0, lineWidth, 0);
            }
        } else if (isLastRaw && isFirstColum && isLastColum) {//如果是最后一行 且是第一列 也是最后一列 只绘制上
            outRect.set(0, lineWidth, 0, 0);
        } else if (isFirstRaw && isFirstColum)// 如果是第一行第一列，则不绘制左上
        {
            outRect.set(0, 0, lineWidth, lineWidth);
        } else if (isFirstRaw && isLastColum) {//如果是第一行最后一列，则不绘制右上
            outRect.set(lineWidth, 0, 0, lineWidth);
        } else if (isFirstColum && isLastRaw)// 如果是第一列最后一行，则不需要绘制左下
        {
            outRect.set(0, lineWidth, lineWidth, 0);
        } else if (isLastRaw && isLastColum) {//如果是最后一列最后一行，则不需要绘制右下
            outRect.set(lineWidth, lineWidth, 0, 0);
        } else if (isFirstRaw) {  //如果是第一行不是最后一列 不是最后一列，则不绘制上
            outRect.set(lineWidth, 0, lineWidth, lineWidth);
        } else if (isLastRaw) {//如果是最后一行不是最后一列不是第一列，则不绘制下
            outRect.set(lineWidth, lineWidth, lineWidth, 0);
        } else if (isFirstColum) {//如果是第一列不是最后一行不是第一行，则不绘制左
            outRect.set(0, lineWidth, lineWidth, lineWidth);
        } else if (isLastColum) {//如果是最后一列不是最后一行不是第一行，则不绘制右
            outRect.set(lineWidth, lineWidth, 0, lineWidth);
        } else {
            outRect.set(lineWidth, lineWidth, lineWidth, lineWidth);
        }
    }
}
