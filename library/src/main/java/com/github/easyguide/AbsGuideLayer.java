package com.github.easyguide;

import android.content.Context;
import android.view.View;

import com.github.easyguide.layer.ILayerCallback;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class AbsGuideLayer {
    private ILayerCallback mCallback;
    private View mView;

    public final ILayerCallback getCallback() {
        return mCallback;
    }

    void setCallback(ILayerCallback callback) {
        mCallback = callback;
    }

    public final View getView(Context context){
        if (mView == null) {
            mView = makeView(context);
        }
        return mView;
    }

    protected abstract View makeView(Context context);
}
