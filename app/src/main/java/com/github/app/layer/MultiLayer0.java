package com.github.app.layer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import com.github.app.R;
import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.layer.RelativeLayerView;
import com.github.easyguide.layer.RelativeGuideLayer;

/**
 * Created by shenxl on 2018/8/16.
 */

public class MultiLayer0 extends RelativeGuideLayer{

    public MultiLayer0(Activity activity) {
        super(activity);
    }

    @Override
    protected RelativeLayerView onCreateView(Context context){
        addTargetView(R.id.easy_guide_0);
        addTargetView(R.id.easy_guide_2);
        return (RelativeLayerView) LayoutInflater.from(context).inflate(R.layout.layer_multi_0, null);
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return new MultiLayer1(getActivity());
    }
}
