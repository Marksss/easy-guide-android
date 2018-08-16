package com.github.easyguide;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideBuilder {
    Activity mActivity;
    int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
    int mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
    int mXoff = 0, mYoff = 0;
    List<AbsGuideLayer> mGuideLayers = new ArrayList<>();
    OnLayerEndListener mOnLayerEndListener;

    public EasyGuideBuilder(Activity activity) {
        mActivity = activity;
    }

    public EasyGuideBuilder setWidth(int width) {
        mWidth = width;
        return this;
    }

    public EasyGuideBuilder setHeight(int height) {
        mHeight = height;
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
        mOnLayerEndListener = onLayerEndListener;
    }

    public EasyGuideBuilder addLayer(AbsGuideLayer guideLayer) {
        mGuideLayers.add(guideLayer);
        return this;
    }

    public LayerPopWindow build(){
        return new LayerPopWindow(this);
    }

    public interface OnLayerEndListener {
        void onLayerEnd(LayerPopWindow popWindow, int index);
    }
}
