package com.github.easyguide;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideManager implements AbsGuideLayer.ILayerCallback {
    private FrameLayout mParentView;
    private AbsGuideLayer mGuideLayer;
    private Context mContext;
    
    public static EasyGuideManager create(AbsGuideLayer guideLayer) {
        return new EasyGuideManager(guideLayer);
    }

    private EasyGuideManager(AbsGuideLayer guideLayer) {
        this.mGuideLayer = guideLayer;
    }
    
    public EasyGuideManager with(Activity activity) {
        this.mContext = activity;
        if (mParentView == null) {
            mParentView = (FrameLayout) activity.getWindow().getDecorView();
        }
        return this;
    }

    public EasyGuideManager setParentView(FrameLayout parentView) {
        this.mParentView = parentView;
        return this;
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

        mGuideLayer.setCallback(this);
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                mParentView.addView(mGuideLayer.getView(mContext),
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            }
        });
    }

    @Override
    public void dismissCurrent() {
        if (mParentView != null) {
            View preView = mGuideLayer.getView(mContext);
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
            mParentView.removeView(mGuideLayer.getView(mContext));
        }
    }
}
