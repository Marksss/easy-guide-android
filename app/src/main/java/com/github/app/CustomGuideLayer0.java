package com.github.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.layer.RelativeLayerView;
import com.github.easyguide.layer.RelativeGuideLayer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class CustomGuideLayer0 extends RelativeGuideLayer{

    @Override
    protected View onCreateView(Context context){
        return LayoutInflater.from(context).inflate(R.layout.leading_0, null);
    }

    @Override
    protected void onViewCreated(RelativeLayerView view) {
        addTargetView(view, R.id.easy_guide_0);
        addTargetView(view, R.id.easy_guide_2);
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return new CustomGuideLayer1();
    }
}
