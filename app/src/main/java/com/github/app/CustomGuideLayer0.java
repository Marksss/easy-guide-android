package com.github.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.easyguide.AbsGuideLayer;
import com.github.easyguide.MaskEntity;
import com.github.easyguide.RelativeGuideLayer;
import com.github.easyguide.RelativeLayerView;

import java.util.Collections;
import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public class CustomGuideLayer0 extends RelativeGuideLayer {

    private Activity mActivity;

    public CustomGuideLayer0(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected View onCreateView(Context context){
        return LayoutInflater.from(context).inflate(R.layout.leading_0, null);
    }

    @Override
    protected List<MaskEntity> getTargets() {
        return Collections.singletonList(MaskEntity.generateFromId(mActivity, R.id.easy_guide_0));
    }

    @Override
    protected void onViewCreated(View view) {

    }

    @Override
    protected AbsGuideLayer nextLayer() {
        return new CustomGuideLayer1();
    }
}
