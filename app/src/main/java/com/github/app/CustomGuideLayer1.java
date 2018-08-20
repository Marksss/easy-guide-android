package com.github.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;

import com.github.easyguide.AbsGuideLayer;
import com.github.easyguide.RelativeLayerView;
import com.github.easyguide.RelativeGuideLayer;

/**
 * Created by shenxl on 2018/8/16.
 */

public class CustomGuideLayer1 extends RelativeGuideLayer {
    @Override
    protected View onCreateView(Context context){
        return LayoutInflater.from(context).inflate(R.layout.leading_1, null);
    }

    @Override
    protected void onViewCreated(final RelativeLayerView view) {
        addTargetView(view, R.id.easy_guide_1);
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return null;
    }

    @Override
    public void onDraw(int id, Rect rect, Canvas canvas, Paint paint) {
//        super.onDraw(id, rect, canvas, paint);
    }
}