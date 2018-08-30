package com.github.easyguide.layer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;

import com.github.easyguide.AbsGuideLayer;

/**
 * Created by shenxl on 2018/8/16.
 */

public class RelativeGuideLayer extends AbsGuideLayer
        implements RelativeLayerView.DrawCallBack, RelativeLayerView.LayerClickListener{
    private Activity mActivity;
    private RelativeLayerView mViewContainer;
    private SparseArray<Rect> mTargetCache = new SparseArray<>();
    private onFullClickListener mFullClickListener;
    private onSingleClickListener mSingleClickListener;

    public RelativeGuideLayer(Activity activity) {
        mActivity = activity;
    }

    protected RelativeLayerView onCreateView(Context context){
        return null;
    }

    protected Activity getActivity() {
        return mActivity;
    }

    public RelativeLayerView getViewContainer() {
        return mViewContainer;
    }

    public final RelativeGuideLayer addTargetView(int id){
        return addTargetView(mActivity.findViewById(id));
    }

    public final RelativeGuideLayer addTargetView(final View view){
        view.post(new Runnable() {
            @Override
            public void run() {
                addTargetView(view.getId(), ViewLocUtils.getViewAbsRect(mActivity, view));
            }
        });
        return this;
    }

    public final RelativeGuideLayer addTargetView(int id, Rect rect) {
        if (mViewContainer != null) {
            mViewContainer.addTargetsRect(id, rect);
            mViewContainer.requestLayout();
        } else {
            mTargetCache.put(id, rect);
        }
        return this;
    }

    public void setFullClickListener(onFullClickListener fullClickListener) {
        mFullClickListener = fullClickListener;
    }

    public void setSingleClickListener(onSingleClickListener singleClickListener) {
        mSingleClickListener = singleClickListener;
    }

    @Override
    public final View makeView(Context context) {
        RelativeLayerView view = onCreateView(context);
        if (view == null) {
            view = new RelativeLayerView(context);
        }
        mViewContainer = view;

        view.setDrawCallBack(this);
        view.setLayerClickListener(this);
        for (int i = 0; i < mTargetCache.size(); i++) {
            addTargetView(mTargetCache.keyAt(i), mTargetCache.valueAt(i));
        }

        return view;
    }

    @Override
    public void onDraw(int id, Rect rect, Canvas canvas, Paint paint){
        drawRect(rect, canvas, paint);
    }

    protected final void drawRect(Rect rect, Canvas canvas, Paint paint){
        canvas.drawRect(rect, paint);
    }

    @Override
    public final void onFullClick() {
        if (mSingleClickListener == null) {
            if (mFullClickListener == null) {
                getCallback().dismissCurrent();
            } else {
                mFullClickListener.onClick(mViewContainer, getCallback());
            }
        }
    }

    @Override
    public final void onSingleClick(int id) {
        if (mSingleClickListener != null) {
            mSingleClickListener.onClick(id, mViewContainer, getCallback());
        }
    }

    public interface onFullClickListener{
        void onClick(RelativeLayerView container, ILayerCallback callback);
    }

    public interface onSingleClickListener{
        void onClick(int id, RelativeLayerView container, ILayerCallback callback);
    }
}
