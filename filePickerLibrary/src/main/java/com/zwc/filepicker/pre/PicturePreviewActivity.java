package com.zwc.filepicker.pre;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.zwc.filepicker.FileLoaderManager;
import com.zwc.filepicker.R;
import com.zwc.filepicker.config.PictureConfig;
import com.zwc.filepicker.entity.LocalMedia;
import com.zwc.filepicker.filetype.FileType;
import com.zwc.filepicker.filetype.ImageFileType;
import com.zwc.filepicker.observable.ImagesObservable;
import com.zwc.filepicker.utils.ScreenUtils;
import com.zwc.filepicker.widget.preview.PreviewViewPager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PicturePreviewActivity extends PictureBaseActivity implements View.OnClickListener, PictureSimpleFragmentAdapter.OnCallBackActivity {
    private static final String TAG = PicturePreviewActivity.class.getSimpleName();
    protected ImageView pictureLeftBack;
    protected TextView tvTitle;
    protected PreviewViewPager viewPager;
    protected int position;
    protected List<LocalMedia> selectData = new ArrayList<>();
    protected PictureSimpleFragmentAdapter adapter;
    protected Animation animation;
    protected int index;
    protected int screenWidth;
    protected Handler mHandler;
    protected View titleViewBg;
    /**
     * 分页码
     */
    private int mPage = 0;
    private int pageSize = 30;
    private FileLoaderManager mLocalMediaPageLoader;
    protected boolean isBottomPreview;
    private int totalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);
        initWidgets();
    }

    protected void initWidgets() {
        mHandler = new Handler();
        titleViewBg = findViewById(R.id.titleViewBg);
        screenWidth = ScreenUtils.getScreenWidth(this);
        animation = AnimationUtils.loadAnimation(this, R.anim.picture_anim_modal_in);
        pictureLeftBack = findViewById(R.id.pictureLeftBack);
        viewPager = findViewById(R.id.preview_pager);
        pictureLeftBack.setOnClickListener(this);
        tvTitle = findViewById(R.id.picture_title);
        tvTitle.setVisibility(View.GONE);
        position = getIntent().getIntExtra(PictureConfig.EXTRA_POSITION, 0);
        selectData = getIntent().
                getParcelableArrayListExtra(PictureConfig.EXTRA_SELECT_LIST);
        isBottomPreview = getIntent().
                getBooleanExtra(PictureConfig.EXTRA_BOTTOM_PREVIEW, false);
        List<LocalMedia> data;
        if (isBottomPreview) {
            // 底部预览模式
            data = getIntent().
                    getParcelableArrayListExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST);
            initViewPageAdapterData(data);
        } else {
            mLocalMediaPageLoader = new FileLoaderManager();
            data = ImagesObservable.getInstance().readPreviewMediaData();
            boolean isEmpty = data.size() == 0;
            totalNumber = getIntent().getIntExtra(PictureConfig.EXTRA_DATA_COUNT, 0);
            // 分页模式
            if (isEmpty) {
                // 这种情况有可能是单例被回收了导致readPreviewMediaData();返回的数据为0，那就从第一页开始加载吧
                setNewTitle();
            } else {
                mPage = getIntent().getIntExtra(PictureConfig.EXTRA_PAGE, 0);
            }
            initViewPageAdapterData(data);
            loadData();
            setTitle();
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                position = i;
                setTitle();
                LocalMedia media = adapter.getItem(position);
                if (media == null) {
                    return;
                }
                index = media.getPosition();
                onPageSelectedChange(media);


                if (!isBottomPreview && isHasMore) {
                    // 滑到adapter.getSize() - PictureConfig.MIN_PAGE_SIZE时或最后一条时预加载
                    if (position == (adapter.getSize() - 1) - PictureConfig.MIN_PAGE_SIZE || position == adapter.getSize() - 1) {
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * ViewPage滑动数据变化回调
     *
     * @param media
     */
    protected void onPageSelectedChange(LocalMedia media) {

    }

    /**
     * 从本地获取数据
     */
    private void loadData() {
        mPage++;
        Observable.just("")
                .map(s -> {
                    ArrayList<FileType> fileTypes = new ArrayList<>();
                    fileTypes.add(ImageFileType.INSTANCE);
                    return mLocalMediaPageLoader.load(
                            PicturePreviewActivity.this,
                            fileTypes,
                            mPage,
                            pageSize);
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (!isFinishing()) {
                        this.isHasMore = result.isHasNextMore();
                        if (isHasMore) {
                            int size = result.getData().size();
                            if (size > 0 && adapter != null) {
                                adapter.getData().addAll(result.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                // 这种情况就是开启过滤损坏文件刚好导致某一页全是损坏的虽然result为0，但还要请求下一页数据
                                loadMoreData();
                            }
                        }
                    }

                }, throwable -> {

                });
    }

    /**
     * 加载更多
     */
    private void loadMoreData() {
        mPage++;
        Observable.just("")
                .map(s -> {
                    ArrayList<FileType> fileTypes = new ArrayList<>();
                    fileTypes.add(ImageFileType.INSTANCE);
                    return mLocalMediaPageLoader.load(
                            PicturePreviewActivity.this,
                            fileTypes,
                            mPage,
                            pageSize);
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (!isFinishing()) {
                        this.isHasMore = result.isHasNextMore();
                        if (isHasMore) {
                            int size = result.getData().size();
                            if (size > 0 && adapter != null) {
                                adapter.getData().addAll(result.getData());
                                adapter.notifyDataSetChanged();
                            } else {
                                // 这种情况就是开启过滤损坏文件刚好导致某一页全是损坏的虽然result为0，但还要请求下一页数据
                                loadMoreData();
                            }
                        }
                    }

                }, throwable -> {

                });
    }

    /**
     * 初始化ViewPage数据
     *
     * @param list
     */
    private void initViewPageAdapterData(List<LocalMedia> list) {
        adapter = new PictureSimpleFragmentAdapter(this);
        adapter.bindData(list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        setTitle();
    }

    /**
     * 重置标题栏和分页码
     */
    private void setNewTitle() {
        mPage = 0;
        position = 0;
        setTitle();
    }

    /**
     * 设置标题
     */
    private void setTitle() {
        if (!isBottomPreview) {
            tvTitle.setText(getString(R.string.picture_preview_image_num,
                    position + 1, totalNumber));
        } else {
            tvTitle.setText(getString(R.string.picture_preview_image_num,
                    position + 1, adapter.getSize()));
        }

    }

    @Override
    public void onBackPressed() {

        closeActivity();
    }

    /**
     * Close Activity
     */
    protected void closeActivity() {
        finish();
        overridePendingTransition(0, R.anim.picture_anim_exit);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pictureLeftBack) {
            onBackPressed();
        }
    }

    @Override
    public void onActivityBackPressed() {
        onBackPressed();
    }
}
