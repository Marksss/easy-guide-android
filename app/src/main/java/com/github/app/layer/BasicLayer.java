package com.github.app.layer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.app.R;
import com.github.easyguide.AbsGuideLayer;
import com.github.easyguide.RelativeGuideLayer;
import com.github.easyguide.RelativeLayerView;

/**
 * Created by shenxl on 2018/8/23.
 */
public class BasicLayer extends RelativeGuideLayer {

    @Override
    protected RelativeLayerView onCreateView(Context context){
        return null;
    }

    @Override
    public void onLayerAttached(RelativeLayerView view) {
        addTargetView(view, R.id.btn_basic_usage);
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return null;
    }
}