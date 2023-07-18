package com.zwc.filepicker.pre;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zwc.filepicker.utils.immersive.ImmersiveManage;
import com.zwc.filepicker.widget.dialog.PictureLoadingDialog;


/**
 * Created by Android Studio.
 * User: zuoweichen
 * Date: 2020/8/31
 * Time: 14:38
 */
public abstract class PictureBaseActivity extends AppCompatActivity {
    protected boolean openWhiteStatusBar;
    protected PictureLoadingDialog mLoadingDialog;
    protected int colorPrimary, colorPrimaryDark;
    protected boolean isHasMore = true;

    @Override
    public boolean isImmersive() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isImmersive()) {
            immersive();
        }
    }

    public void immersive() {
        colorPrimary = Color.parseColor("#000000");
        colorPrimaryDark = Color.parseColor("#000000");
        ImmersiveManage.immersiveAboveAPI23(this
                , colorPrimaryDark
                , colorPrimary
                , openWhiteStatusBar);
    }

    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        try {
            if (!isFinishing()) {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new PictureLoadingDialog(getContext());
                }
                if (mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                mLoadingDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        if (!isFinishing()) {
            try {
                if (mLoadingDialog != null
                        && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            } catch (Exception e) {
                mLoadingDialog = null;
                e.printStackTrace();
            }
        }
    }

    protected Context getContext() {
        return this;
    }
}
