package com.zwc.filepicker.pre;

import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zwc.filepicker.R;
import com.zwc.filepicker.config.PictureConfig;
import com.zwc.filepicker.entity.LocalMedia;
import com.zwc.filepicker.utils.JumpUtils;
import com.zwc.filepicker.utils.MediaUtils;
import com.zwc.filepicker.utils.PictureMimeType;
import com.zwc.filepicker.widget.photoview.PhotoView;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author：luck
 * @data：2018/1/27 下午7:50
 * @describe:PictureSimpleFragmentAdapter
 */

public class PictureSimpleFragmentAdapter extends PagerAdapter {
    private List<LocalMedia> data;
    private OnCallBackActivity onBackPressed;
    /**
     * Maximum number of cached images
     */
    private static final int MAX_CACHE_SIZE = 20;
    /**
     * To cache the view
     */
    private SparseArray<View> mCacheView;

    public void clear() {
        if (null != mCacheView) {
            mCacheView.clear();
            mCacheView = null;
        }
    }

    public void removeCacheView(int position) {
        if (mCacheView != null && position < mCacheView.size()) {
            mCacheView.removeAt(position);
        }
    }

    public interface OnCallBackActivity {
        /**
         * Close Activity
         */
        void onActivityBackPressed();
    }

    public PictureSimpleFragmentAdapter(OnCallBackActivity onBackPressed) {
        super();
        this.onBackPressed = onBackPressed;
        this.mCacheView = new SparseArray<>();
    }

    /**
     * bind data
     *
     * @param data
     */
    public void bindData(List<LocalMedia> data) {
        this.data = data;
    }

    /**
     * get data
     *
     * @return
     */
    public List<LocalMedia> getData() {
        return data == null ? new ArrayList<>() : data;
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    public void remove(int currentItem) {
        if (getSize() > currentItem) {
            data.remove(currentItem);
        }
    }

    public LocalMedia getItem(int position) {
        return getSize() > 0 && position < getSize() ? data.get(position) : null;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
        if (mCacheView.size() > MAX_CACHE_SIZE) {
            mCacheView.remove(position);
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        View contentView = mCacheView.get(position);
        if (contentView == null) {
            contentView = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.picture_image_preview, container, false);
            mCacheView.put(position, contentView);
        }
        PhotoView imageView = contentView.findViewById(R.id.preview_image);
        SubsamplingScaleImageView longImg = contentView.findViewById(R.id.longImg);
        ImageView ivPlay = contentView.findViewById(R.id.iv_play);
        LocalMedia media = getItem(position);
        if (media != null) {
            final String mimeType = media.getMimeType();
            final String path;

            path = media.getPath();
            boolean isGif = PictureMimeType.isGif(mimeType);
            boolean isHasVideo = PictureMimeType.isHasVideo(mimeType);
            ivPlay.setVisibility(isHasVideo ? View.VISIBLE : View.GONE);
            ivPlay.setOnClickListener(v -> {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putBoolean(PictureConfig.EXTRA_PREVIEW_VIDEO, true);
                bundle.putString(PictureConfig.EXTRA_VIDEO_PATH, path);
                intent.putExtras(bundle);
                JumpUtils.startPictureVideoPlayActivity(container.getContext(), bundle, PictureConfig.PREVIEW_VIDEO_CODE);
            });
            boolean eqLongImg = MediaUtils.isLongImg(media);
            imageView.setVisibility(eqLongImg && !isGif ? View.GONE : View.VISIBLE);
            imageView.setOnViewTapListener((view, x, y) -> {
                if (onBackPressed != null) {
                    onBackPressed.onActivityBackPressed();
                }
            });
            longImg.setVisibility(eqLongImg && !isGif ? View.VISIBLE : View.GONE);
            longImg.setOnClickListener(v -> {
                if (onBackPressed != null) {
                    onBackPressed.onActivityBackPressed();
                }
            });

            if (isGif) {

                Glide.with(contentView.getContext()).load(path).into(imageView);
            } else {

                if (eqLongImg) {
                    displayLongPic(PictureMimeType.isContent(path)
                            ? Uri.parse(path) : Uri.fromFile(new File(path)), longImg);
                } else {
                    Glide.with(contentView.getContext()).load(path).into(imageView);
                }
            }
        }

        (container).addView(contentView, 0);
        return contentView;
    }

    /**
     * load long image
     *
     * @param uri
     * @param longImg
     */
    private void displayLongPic(Uri uri, SubsamplingScaleImageView longImg) {
        longImg.setQuickScaleEnabled(true);
        longImg.setZoomEnabled(true);
        longImg.setPanEnabled(true);
        longImg.setDoubleTapZoomDuration(100);
        longImg.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        longImg.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
        longImg.setImage(ImageSource.uri(uri), new ImageViewState(0, new PointF(0, 0), 0));
    }
}
