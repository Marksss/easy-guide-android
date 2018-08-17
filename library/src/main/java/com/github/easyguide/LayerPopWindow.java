package com.github.easyguide;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * Created by shenxl on 2018/8/16.
 */

public class LayerPopWindow extends PopupWindow implements AbsGuideLayer.ILayerCallback {
    private FrameLayout mParentView;
    private EasyGuideBuilder mBuilder;
    private int mLayerIndex = 0;

    LayerPopWindow(EasyGuideBuilder builder) {
        super(builder.mWidth, builder.mHeight);
        this.mBuilder = builder;
    }

    public EasyGuideBuilder getBuilder() {
        return mBuilder;
    }

    public void show() {
        if (mBuilder.mGuideLayer == null) {
            throw new IllegalArgumentException("the GuideLayer is null!");
        }
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(true);

        mBuilder.mGuideLayer.setCallback(this);
        View view = mBuilder.mGuideLayer.makeView(mBuilder.mActivity);
        if (mParentView == null) {
            mParentView = new FrameLayout(mBuilder.mActivity);
            mParentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        this.setContentView(mParentView);
        this.showAsDropDown(mParentView, mBuilder.mXoff, mBuilder.mYoff);
    }

    @Override
    public void dismissCurrent() {
        if (!isShowing()){
            return;
        }
        if (mBuilder.mOnLayerEndListener != null) {
            mBuilder.mOnLayerEndListener.onLayerEnd(mLayerIndex);
        }
        AbsGuideLayer nextLayer = mBuilder.mGuideLayer.nextLayer();
        if (nextLayer == null) {
            mLayerIndex = 0;
            dismiss();
        } else {
            mLayerIndex++;
            nextLayer.setCallback(this);
            View view = nextLayer.makeView(mBuilder.mActivity);
            mParentView.removeAllViews();
            mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            this.showAsDropDown(mParentView, mBuilder.mXoff, mBuilder.mYoff);
            mBuilder.mGuideLayer = nextLayer;
        }
    }

    @Override
    public void dismissAll() {
        if (!isShowing()){
            return;
        }
        if (mBuilder.mOnLayerEndListener != null) {
            mBuilder.mOnLayerEndListener.onLayerEnd(mLayerIndex);
        }
        dismiss();
        mLayerIndex = 0;
    }
}
