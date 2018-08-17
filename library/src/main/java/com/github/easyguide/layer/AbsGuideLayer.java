package com.github.easyguide.layer;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class AbsGuideLayer {
    private ILayerCallback mCallback;
    private Activity mActivity;

    public ILayerCallback getCallback() {
        return mCallback;
    }

    public void setCallback(ILayerCallback callback) {
        mCallback = callback;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public abstract View makeView(Context context);

    public abstract AbsGuideLayer nextLayer();

    public interface ILayerCallback {
        void dismissCurrent();
        void dismissAll();
    }
}
