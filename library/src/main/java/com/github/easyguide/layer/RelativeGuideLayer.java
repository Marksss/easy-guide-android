package com.github.easyguide.layer;

import android.content.Context;
import android.view.View;

import com.github.easyguide.utils.ViewLocationUtils;

import java.util.List;

/**
 * Created by shenxl on 2018/8/16.
 */

public abstract class RelativeGuideLayer extends AbsGuideLayer {

    protected abstract View onCreateView(Context context);
    protected abstract void onViewCreated(RelativeLayerView view);

    public boolean needFullScreenClick(){
        return true;
    }

    protected void addTargetView(RelativeLayerView container, int id){
        addTargetView(container, getActivity().findViewById(id));
    }

    protected void addTargetView(RelativeLayerView container, View view){
        container.addTargetsRect(view.getId(), ViewLocationUtils.getViewAbsRect(getActivity(), view));
    }

    @Override
    public final View makeView(Context context) {
        final View view = onCreateView(context);
        if (view == null || !(view instanceof RelativeLayerView)) {
            throw new IllegalArgumentException("View that returns from onCreateView is null or isn't an instance of RelativeLayerView");
        }
        if (needFullScreenClick()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCallback().dismissCurrent();
                }
            });
        }

        onViewCreated((RelativeLayerView) view);
        return view;
    }
}
