package com.github.easyguide;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideManager implements AbsGuideLayer.ILayerCallback {
    private FrameLayout mParentView;
    private Activity mActivity;
    private AbsGuideLayer mGuideLayer;

    public EasyGuideManager(Activity activity, AbsGuideLayer guideLayer) {
        this.mActivity = activity;
        this.mGuideLayer = guideLayer;
    }

    public void setParentView(FrameLayout parentView) {
        mParentView = parentView;
    }

    public FrameLayout getParentView() {
        return mParentView;
    }

    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayer;
    }

    public void showLayer() {
        if (mGuideLayer == null) {
            throw new IllegalArgumentException("the GuideLayer is null!");
        }

        if (mParentView == null) {
            mParentView = (FrameLayout) mActivity.getWindow().getDecorView();
        }
        mGuideLayer.setCallback(this);
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                mParentView.addView(mGuideLayer.getView(mActivity),
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
        });
    }

    @Override
    public void dismissCurrent() {
        if (mParentView != null) {
            View preView = mGuideLayer.getView(mActivity);
            AbsGuideLayer nextLayer = mGuideLayer.nextLayer();
            if (nextLayer != null) {
                mGuideLayer = nextLayer;
                showLayer();
            }
            mParentView.removeView(preView);
        }
    }

    @Override
    public void dismissAll() {
        if (mParentView != null) {
            mParentView.removeView(mGuideLayer.getView(mActivity));
        }
    }
}
