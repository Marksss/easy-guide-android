package com.github.easyguide;

import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.github.easyguide.layer.AbsGuideLayer;

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

        mBuilder.mGuideLayer.setCallback(this);
        mBuilder.mGuideLayer.setActivity(mBuilder.mActivity);
        View view = mBuilder.mGuideLayer.makeView(mBuilder.mActivity);
        if (mParentView == null) {
            mParentView = new FrameLayout(mBuilder.mActivity);
            mParentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        setContentView(mParentView);
        super.showAsDropDown(mParentView, mBuilder.mXoff, mBuilder.mYoff, Gravity.NO_GRAVITY);
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
            nextLayer.setActivity(mBuilder.mActivity);
            View view = nextLayer.makeView(mBuilder.mActivity);
            mParentView.removeAllViews();
            mParentView.addView(view, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            super.showAsDropDown(mParentView, mBuilder.mXoff, mBuilder.mYoff, Gravity.NO_GRAVITY);
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

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        throw new UnsupportedOperationException("showAtLocation is not supported in LayerPopWindow");
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        throw new UnsupportedOperationException("showAsDropDown is not supported in LayerPopWindow");
    }
}
