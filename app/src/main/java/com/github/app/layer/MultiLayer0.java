package com.github.app.layer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.app.R;
import com.github.easyguide.AbsGuideLayer;
import com.github.easyguide.RelativeLayerView;
import com.github.easyguide.RelativeGuideLayer;

/**
 * Created by shenxl on 2018/8/16.
 */

public class MultiLayer0 extends RelativeGuideLayer{

    @Override
    protected RelativeLayerView onCreateView(Context context){
        return (RelativeLayerView) LayoutInflater.from(context).inflate(R.layout.leading_0, null);
    }

    @Override
    public void onLayerAttached(RelativeLayerView view) {
        addTargetView(view, R.id.easy_guide_0);
        addTargetView(view, R.id.easy_guide_2);
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return new MultiLayer1();
    }
}
