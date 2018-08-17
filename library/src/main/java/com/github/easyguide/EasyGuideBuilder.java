package com.github.easyguide;

import android.app.Activity;
import android.view.ViewGroup;

import com.github.easyguide.layer.AbsGuideLayer;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideBuilder {
    Activity mActivity;
    int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
    int mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
    int mXoff = 0, mYoff = 0;
    AbsGuideLayer mGuideLayer;
    OnLayerEndListener mOnLayerEndListener;

    public EasyGuideBuilder(Activity activity) {
        this.mActivity = activity;
    }

    public EasyGuideBuilder setWidth(int width) {
        this.mWidth = width;
        return this;
    }

    public EasyGuideBuilder setHeight(int height) {
        this.mHeight = height;
        return this;
    }

    public EasyGuideBuilder setXoff(int xoff) {
        this.mXoff = xoff;
        return this;
    }

    public EasyGuideBuilder setYoff(int yoff) {
        this.mYoff = yoff;
        return this;
    }

    public void withSingleLayerEnd(OnLayerEndListener onLayerEndListener) {
        this.mOnLayerEndListener = onLayerEndListener;
    }

    public EasyGuideBuilder setGuideLayer(AbsGuideLayer guideLayer) {
        this.mGuideLayer = guideLayer;
        return this;
    }

    public LayerPopWindow build(){
        return new LayerPopWindow(this);
    }

    public interface OnLayerEndListener {
        void onLayerEnd(int index);
    }
}
