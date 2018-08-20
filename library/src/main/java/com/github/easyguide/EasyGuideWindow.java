package com.github.easyguide;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * Created by shenxl on 2018/8/16.
 */

public class EasyGuideWindow extends PopupWindow implements AbsGuideLayer.ILayerCallback {
    private FrameLayout mParentView;
    private Activity mActivity;
    private AbsGuideLayer mGuideLayer;
    private OnLayerEndListener mOnLayerEndListener;
    private int mLayerIndex = 0;

    public EasyGuideWindow(Activity activity, AbsGuideLayer guideLayer) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.mActivity = activity;
        this.mGuideLayer = guideLayer;
    }

    public EasyGuideWindow setOnLayerEndListener(OnLayerEndListener onLayerEndListener) {
        mOnLayerEndListener = onLayerEndListener;
        return this;
    }

    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayer;
    }

    public void show() {
        if (mGuideLayer == null) {
            throw new IllegalArgumentException("the GuideLayer is null!");
        }

        mGuideLayer.setCallback(this);
        mGuideLayer.setActivity(mActivity);
        View view = mGuideLayer.makeView(mActivity);
        if (mParentView == null) {
            mParentView = new FrameLayout(mActivity);
            mParentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(mParentView);
        super.showAsDropDown(mParentView, 0, 0, Gravity.NO_GRAVITY);
    }

    @Override
    public void dismissCurrent() {
        if (!isShowing()){
            return;
        }
        if (mOnLayerEndListener != null) {
            mOnLayerEndListener.onLayerEnd(mLayerIndex);
        }
        AbsGuideLayer nextLayer = mGuideLayer.nextLayer();
        if (nextLayer == null) {
            mLayerIndex = 0;
            dismiss();
        } else {
            mLayerIndex++;
            nextLayer.setCallback(this);
            nextLayer.setActivity(mActivity);
            View view = nextLayer.makeView(mActivity);
            mParentView.removeAllViews();
            mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            super.showAsDropDown(mParentView, 0, 0, Gravity.NO_GRAVITY);
            mGuideLayer = nextLayer;
        }
    }

    @Override
    public void dismissAll() {
        if (!isShowing()){
            return;
        }
        if (mOnLayerEndListener != null) {
            mOnLayerEndListener.onLayerEnd(mLayerIndex);
        }
        dismiss();
        mLayerIndex = 0;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        throw new UnsupportedOperationException("showAtLocation is not supported in LayerPopWindow");
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        throw new UnsupportedOperationException("showAsDropDown is not supported in LayerPopWindow");
    }
    
    public interface OnLayerEndListener {
        void onLayerEnd(int index);
    }
}
