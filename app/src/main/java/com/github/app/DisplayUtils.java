package com.github.app;

import android.content.Context;

/**
 * Created by shenxl on 2018/11/20.
 */
public class DisplayUtils {
    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
