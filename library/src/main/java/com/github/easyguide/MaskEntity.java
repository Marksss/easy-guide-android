package com.github.easyguide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by shenxl on 2018/8/17.
 */

public class MaskEntity {
    public int id;
    public Rect mRect;
    public boolean needHighLight = true;

    public MaskEntity(int id, Rect rect) {
        this.id = id;
        mRect = rect;
    }

    public static MaskEntity generateFromView(Activity activity, View view){
        return new MaskEntity(view.getId(), ViewLocationUtils.getViewAbsRect(activity, view));
    }

    public static MaskEntity generateFromId(Activity activity, int id){
        return generateFromView(activity, activity.findViewById(id));
    }
}
