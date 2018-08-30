package com.github.easyguide.layer;

import android.content.Context;
import android.view.View;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class AbsGuideLayer {
    private ILayerCallback mCallback;
    private View mView;

    public final ILayerCallback getCallback() {
        return mCallback;
    }

    public final void setCallback(ILayerCallback callback) {
        mCallback = callback;
    }

    public final View getView(Context context){
        if (mView == null) {
            mView = makeView(context);
        }
        return mView;
    }

    protected abstract View makeView(Context context);

    public abstract AbsGuideLayer nextLayer();

    public interface ILayerCallback {
        void dismissCurrent();
        void dismissAll();
    }
}
