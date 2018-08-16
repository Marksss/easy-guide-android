package com.github.app;

import android.widget.RelativeLayout;

import com.github.easyguide.RelativeGuideLayer;

/**
 * Created by shenxl on 2018/8/16.
 */

public class CustomGuideLayer extends RelativeGuideLayer {
    @Override
    protected int getLayoutId() {
        return R.layout.leading_main;
    }
}
