package com.github.easyguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class RelativeGuideLayer extends AbsGuideLayer {

    protected abstract int getLayoutId();

    public boolean needFullScreenClick(){
        return true;
    }

    protected void onViewCreated(View view){

    }

    @Override
    protected final View makeView(Context context) {
        View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
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
