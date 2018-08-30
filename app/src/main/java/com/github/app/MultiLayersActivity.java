package com.github.app;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.github.easyguide.layer.AbsGuideLayer;
import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.RelativeGuideLayer;
import com.github.easyguide.layer.RelativeLayerView;

/**
 * Created by shenxl on 2018/8/14.
 */
public class MultiLayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_layers);

        /*   EasyGuide starts  */
        MultiLayer0 layer = new MultiLayer0(MultiLayersActivity.this);
        layer.setSingleClickListener(new RelativeGuideLayer.onSingleClickListener() {
            @Override
            public void onClick(int id, RelativeLayerView container, AbsGuideLayer.ILayerCallback callback) {
                callback.dismissCurrent();
            }
        });
        layer.setNextLayer(new MultiLayer1(MultiLayersActivity.this));
        EasyGuideManager.create(layer).with(MultiLayersActivity.this).showLayer();
        /*   EasyGuide ends  */
    }

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
    }

    public class MultiLayer1 extends RelativeGuideLayer {
        public MultiLayer1(Activity activity) {
            super(activity);
        }

        @Override
        protected RelativeLayerView onCreateView(Context context){
            addTargetView(R.id.easy_guide_1);
            return (RelativeLayerView) LayoutInflater.from(context).inflate(R.layout.layer_multi_1, null);
        }
    }
}
