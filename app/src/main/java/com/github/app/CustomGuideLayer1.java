package com.github.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.utils.MaskEntity;
import com.github.easyguide.layer.RelativeGuideLayer;

import java.util.Collections;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class CustomGuideLayer1 extends RelativeGuideLayer {
    @Override
    protected View onCreateView(Context context){
        return LayoutInflater.from(context).inflate(R.layout.leading_1, null);
    }

    @Override
    protected List<MaskEntity> getTargets() {
        return Collections.singletonList(MaskEntity.generateFromId(getActivity(), R.id.easy_guide_1));
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return null;
    }
}