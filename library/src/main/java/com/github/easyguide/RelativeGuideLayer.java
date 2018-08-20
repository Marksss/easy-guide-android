package com.github.easyguide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class RelativeGuideLayer extends AbsGuideLayer implements RelativeLayerView.DrawCallBack {

    protected abstract View onCreateView(Context context);
    protected abstract void onViewCreated(RelativeLayerView view);

    public boolean needFullScreenClick(){
        return true;
    }

    protected void addTargetView(RelativeLayerView container, int id){
        addTargetView(container, getActivity().findViewById(id));
    }

    protected void addTargetView(RelativeLayerView container, View view){
        container.addTargetsRect(view.getId(), ViewLocationUtils.getViewAbsRect(getActivity(), view));
    }

    @Override
    public final View makeView(Context context) {
        View view = onCreateView(context);
        if (view == null) {
            view = new RelativeLayerView(context);
        }
        if (!(view instanceof RelativeLayerView)) {
            throw new IllegalArgumentException("View that returns from onCreateView is null or isn't an instance of RelativeLayerView");
        }
        if (needFullScreenClick()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().dismissCurrent();
                }
            });
        }

        ((RelativeLayerView) view).setDrawCallBack(this);
        onViewCreated((RelativeLayerView) view);
        return view;
    }

    @Override
    public void onDraw(int id, Rect rect, Canvas canvas, Paint paint){
        drawRect(rect, canvas, paint);
    }

    protected void drawRect(Rect rect, Canvas canvas, Paint paint){
        canvas.drawRect(rect, paint);
    }
}
