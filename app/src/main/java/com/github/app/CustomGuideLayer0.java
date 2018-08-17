package com.github.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.utils.MaskEntity;
import com.github.easyguide.layer.RelativeGuideLayer;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class CustomGuideLayer0 extends RelativeGuideLayer implements View.OnClickListener{

    @Override
    public boolean needFullScreenClick() {
        return false;
    }

    @Override
    protected View onCreateView(Context context){
        return LayoutInflater.from(context).inflate(R.layout.leading_0, null);
    }

    @Override
    protected List<MaskEntity> getTargets() {
        return Arrays.asList(
                MaskEntity.generateFromId(getActivity(), R.id.easy_guide_0).setOnClickListener(this),
                MaskEntity.generateFromId(getActivity(), R.id.easy_guide_2).setOnClickListener(this));
    }

    @Override
    protected void onViewCreated(View view) {
    }

    @Override
    public AbsGuideLayer nextLayer() {
        return new CustomGuideLayer1();
    }

    @Override
    public void onClick(View v) {
        getCallback().dismissCurrent();
    }
}
