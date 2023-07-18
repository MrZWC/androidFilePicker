package com.zwc.filepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zwc.filepicker.pre.PicturePreviewActivity;
import com.zwc.filepicker.pre.PictureVideoPlayActivity;

/**
 * Created by Android Studio.
 * User: zuoweichen
 * Date: 2020/8/29
 * Time: 15:50
 */
public class JumpUtils {
    /**
     * 启动视频播放页面
     *
     * @param context
     * @param bundle
     */
    public static void startPictureVideoPlayActivity(Context context, Bundle bundle, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(context, PictureVideoPlayActivity.class);
            intent.putExtras(bundle);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
    }

    /**
     * 启动预览界面
     *
     * @param context
     * @param bundle
     * @param requestCode
     */
    public static void startPicturePreviewActivity(Context context, Bundle bundle, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(context, PicturePreviewActivity.class);
            intent.putExtras(bundle);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                if (requestCode==0){
                    context.startActivity(intent);
                }else {
                    ((Activity) context).startActivityForResult(intent, requestCode);
                }
            }
        }
    }

    public static void startPicturePreviewActivity(Context context, Bundle bundle) {
        startPicturePreviewActivity(context, bundle,0);
    }
}
