package com.github.easyguide;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by shenxl on 2018/8/16.
 */

public class LayerPopWindow extends PopupWindow implements AbsGuideLayer.ILayerCallback {
    private EasyGuideBuilder mBuilder;
    private int mLayerIndex = 0;

    LayerPopWindow(EasyGuideBuilder builder) {
        super(builder.mWidth, builder.mHeight);
        this.mBuilder = builder;
        this.setBackgroundDrawable(new BitmapDrawable());
    }

    public void show() {
        AbsGuideLayer guideLayer = mBuilder.mGuideLayers.get(mLayerIndex);
        guideLayer.setCallback(this);
        View view = guideLayer.makeView(mBuilder.mActivity);
        this.setContentView(view);
        this.showAsDropDown(view, mBuilder.mXoff, mBuilder.mYoff);
    }

    @Override
    public void dismissCurrent() {
        if (mBuilder.mOnLayerEndListener != null) {
            mBuilder.mOnLayerEndListener.onLayerEnd(this, mLayerIndex);
        }
        mLayerIndex++;
        if (mLayerIndex >= mBuilder.mGuideLayers.size()) {
            dismiss();
        } else {
            show();
        }
    }

    @Override
    public void dismissAll() {
        if (mBuilder.mOnLayerEndListener != null) {
            mBuilder.mOnLayerEndListener.onLayerEnd(this, mLayerIndex);
        }
        dismiss();
    }
}
