package com.github.easyguide.layer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

/**
 * Created by shenxl on 2018/8/17.
 */

public class ViewLocUtils {

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static Rect getViewAbsRect(Context context, View view) {
        int[] locView = new int[2];
        view.getLocationInWindow(locView);
        Rect rect = new Rect();
        rect.set(locView[0], locView[1], locView[0] + view.getMeasuredWidth(), locView[1] + view.getMeasuredHeight());
        rect.offset(0, Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT ? -getStatusBarHeight(context) : 0);
        return rect;
    }
}
