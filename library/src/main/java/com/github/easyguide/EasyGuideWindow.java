package com.github.easyguide;

import android.content.Context;
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
    private Context mContext;
    private AbsGuideLayer mGuideLayer;
    private int mLayerIndex = 0;

    public EasyGuideWindow(Context context, AbsGuideLayer guideLayer) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.mContext = context;
        this.mGuideLayer = guideLayer;
    }

    public AbsGuideLayer getCurrentLayer() {
        return mGuideLayer;
    }

    public void show() {
        if (mGuideLayer == null) {
            throw new IllegalArgumentException("the GuideLayer is null!");
        }

        mGuideLayer.setCallback(this);
        View view = mGuideLayer.makeView(mContext);
        if (mParentView == null) {
            mParentView = new FrameLayout(mContext);
            mParentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(mParentView);
        super.showAsDropDown(mParentView);
    }

    @Override
    public void dismissCurrent() {
        if (!isShowing()){
            return;
        }
        AbsGuideLayer nextLayer = mGuideLayer.nextLayer();
        if (nextLayer == null) {
            mLayerIndex = 0;
            dismiss();
        } else {
            mLayerIndex++;
            nextLayer.setCallback(this);
            View view = nextLayer.makeView(mContext);
            mParentView.removeAllViews();
            mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            super.showAsDropDown(mParentView);
            mGuideLayer = nextLayer;
        }
    }

    @Override
    public void dismissAll() {
        if (!isShowing()){
            return;
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
}
