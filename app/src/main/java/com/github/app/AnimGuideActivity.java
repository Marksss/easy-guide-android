package com.github.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.github.easyguide.EasyGuideManager;
import com.github.easyguide.layer.GuideLayerImpl;
import com.github.easyguide.layer.Location;

/**
 * Created by shenxl on 2018/11/20.
 */
public class AnimGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*   EasyGuide starts  */
                GuideLayerImpl layer = GuideLayerImpl.Companion.coverActivity(AnimGuideActivity.this).
                        addHighlightTarget(findViewById(R.id.btn_share)).
                        withExtraView(LayoutInflater.from(AnimGuideActivity.this).inflate(R.layout.layer_share, null),
                                (int) DisplayUtils.dp2px(AnimGuideActivity.this, 20f),
                                (int) DisplayUtils.dp2px(AnimGuideActivity.this, -40f),
                                Location.TO_BOTTOM);

                Animation enterAnimation = new AlphaAnimation(0f, 1f);
                enterAnimation.setDuration(600);
                enterAnimation.setFillAfter(true);
                layer.setEnterAnimation(enterAnimation);

                Animation exitAnimation = new AlphaAnimation(1f, 0f);
                exitAnimation.setDuration(600);
                exitAnimation.setFillAfter(true);
                layer.setExitAnimation(exitAnimation);

                new EasyGuideManager().addLayer(layer).show();
                /*   EasyGuide ends  */

            }
        });
    }
}
