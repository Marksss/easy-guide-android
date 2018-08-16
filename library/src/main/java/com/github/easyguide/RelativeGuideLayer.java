package com.github.easyguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class RelativeGuideLayer extends AbsGuideLayer {

    protected abstract View onCreateView(Context context);

    protected void onViewCreated(View view){

    }

    public boolean needFullScreenClick(){
        return true;
    }

    @Override
    protected final View makeView(Context context) {
        View view = onCreateView(context);
        if (view == null || !(view instanceof RelativeLayerView)) {
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
        onViewCreated(view);
        return view;
    }

    public class TargetEntity{
        float startX, startY;
        int width, height;
    }
}
