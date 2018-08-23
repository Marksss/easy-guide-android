package com.github.easyguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class RelativeGuideLayer extends AbsGuideLayer
        implements RelativeLayerView.DrawCallBack, RelativeLayerView.WindowCircleLinster {

    protected abstract RelativeLayerView onCreateView(Context context);

    protected void addTargetView(RelativeLayerView container, int id){
        addTargetView(container, getActivity().findViewById(id));
    }

    protected void addTargetView(RelativeLayerView container, View view){
        container.addTargetsRect(view.getId(), ViewLocationUtils.getViewAbsRect(getActivity(), view));
    }

    @Override
    public final View makeView(Context context) {
        RelativeLayerView view = onCreateView(context);
        if (view == null) {
            view = new RelativeLayerView(context);
        }

        view.setDrawCallBack(this);
        view.setWindowCircleLinster(this);
        return view;
    }

    @Override
    public void onLayerAttached(RelativeLayerView view) {
        // TODO: 2018/8/23 touch event && show oncreate
    }

    @Override
    public void onLayerDetached(RelativeLayerView view) {

    }

    @Override
    public void onDraw(int id, Rect rect, Canvas canvas, Paint paint){
        drawRect(rect, canvas, paint);
    }

    protected void drawRect(Rect rect, Canvas canvas, Paint paint){
        canvas.drawRect(rect, paint);
    }
}
